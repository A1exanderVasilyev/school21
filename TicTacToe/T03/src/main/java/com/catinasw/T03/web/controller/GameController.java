package com.catinasw.T03.web.controller;

import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.service.GameServiceImpl;
import com.catinasw.T03.domain.util.GameState;
import com.catinasw.T03.web.mapper.DomainWebMapper;
import com.catinasw.T03.web.model.WebCurrentGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameServiceImpl gameServiceImpl;
    private final DomainWebMapper domainWebMapper;

    @Autowired
    public GameController(GameServiceImpl gameServiceImpl, DomainWebMapper domainWebMapper) {
        this.gameServiceImpl = gameServiceImpl;
        this.domainWebMapper = domainWebMapper;
    }

    @PostMapping()
    public ResponseEntity<WebCurrentGame> createGame() {
        int createdGameId = gameServiceImpl.createNewGame();
        CurrentGame repositoryGame = gameServiceImpl.getGameById(createdGameId);
        return new ResponseEntity<>(domainWebMapper.toWeb(repositoryGame), HttpStatus.CREATED);
    }


    @PostMapping("/{UUID}")
    public ResponseEntity<?> getUpdatedField(@PathVariable("UUID") int id,
                                             @RequestBody WebCurrentGame webGame) {
        if (id != webGame.getUUID()) {
            String errorMessage = "Post id not matched with request body id";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        CurrentGame domainGame = domainWebMapper.toDomain(webGame);
        CurrentGame repositoryGame;

        try {
            repositoryGame = gameServiceImpl.getGameById(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            gameServiceImpl.validateGameField(repositoryGame, domainGame);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        GameState repositoryState = gameServiceImpl.checkGameState(repositoryGame);
        if (repositoryState != GameState.EXECUTING) {
            return getEndGameResponseEntity(repositoryState, repositoryGame);
        }

        CurrentGame updatedGame = gameServiceImpl.getNextMove(domainGame);
        gameServiceImpl.saveGame(updatedGame);

        repositoryState = gameServiceImpl.checkGameState(updatedGame);
        if (repositoryState != GameState.EXECUTING) {
            return getEndGameResponseEntity(repositoryState, updatedGame);
        }

        return new ResponseEntity<>(domainWebMapper.toWeb(updatedGame), HttpStatus.OK);
    }

    public ResponseEntity<WebCurrentGame> getEndGameResponseEntity(GameState repositoryState,
                                                                   CurrentGame currentGame) {
        currentGame.setGameState(repositoryState);
        String gameEndMessage = gameServiceImpl.getGameEndResultMessage(repositoryState);
        System.out.println(gameEndMessage);
        return new ResponseEntity<>(domainWebMapper.toWeb(currentGame), HttpStatus.OK);
    }
}
