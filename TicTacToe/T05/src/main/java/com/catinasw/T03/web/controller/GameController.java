package com.catinasw.T03.web.controller;

import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.model.User;
import com.catinasw.T03.domain.model.UserWinRatio;
import com.catinasw.T03.domain.service.GameServiceImpl;
import com.catinasw.T03.domain.util.GameState;
import com.catinasw.T03.security.JwtProvider;
import com.catinasw.T03.web.mapper.DomainWebMapper;
import com.catinasw.T03.web.model.WebCurrentGame;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameServiceImpl gameServiceImpl;
    private final DomainWebMapper domainWebMapper;
    private final JwtProvider jwtProvider;

    @Autowired
    public GameController(GameServiceImpl gameServiceImpl, DomainWebMapper domainWebMapper, JwtProvider jwtProvider) {
        this.gameServiceImpl = gameServiceImpl;
        this.domainWebMapper = domainWebMapper;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping()
    public ResponseEntity<?> createGame(
            @RequestParam(required = false, defaultValue = "false") boolean isMultiplayer,
            HttpServletRequest httpServletRequest
    ) {
        CurrentGame repositoryGame;
        try {
            UUID creatorId = (UUID) httpServletRequest.getAttribute("creatorId");
            repositoryGame = gameServiceImpl.createNewGame(creatorId, isMultiplayer);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(domainWebMapper.toWeb(repositoryGame), HttpStatus.CREATED);
    }


    @PostMapping("/{UUID}")
    public ResponseEntity<?> getUpdatedField(@PathVariable("UUID") UUID id,
                                             @RequestBody WebCurrentGame webGame,
                                             @RequestAttribute UUID creatorId) {
        if (!id.equals(webGame.getUuid())) {
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

        GameState repositoryState = repositoryGame.getGameState();
        if (gameServiceImpl.isGameOverState(repositoryState)) {
            return getEndGameResponseEntity(repositoryState, repositoryGame);
        }
        if (repositoryState == GameState.WAITING_FOR_PLAYERS) {
            return new ResponseEntity<>("Waiting for players", HttpStatus.OK);
        }
        boolean isSingleplayerGame = repositoryGame.getPlayer2() == null;

        try {
            gameServiceImpl.validateGameField(repositoryGame, domainGame);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (isNotValidTurnMaker(domainGame, creatorId)) {
            return new ResponseEntity<>("It's not your turn", HttpStatus.BAD_REQUEST);
        }

        try {
            repositoryGame.setGameField(domainGame.getGameField());
            if (gameServiceImpl.isGameOverState(repositoryState)) {
                repositoryGame.setGameState(repositoryState);

                return getEndGameResponseEntity(repositoryState, repositoryGame);
            } else {
                gameServiceImpl.saveGame(repositoryGame);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (isSingleplayerGame) {
            CurrentGame updatedGame = gameServiceImpl.getNextMove(domainGame);
            repositoryGame.setGameField(updatedGame.getGameField());
            try {
                gameServiceImpl.saveGame(repositoryGame);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            if (repositoryState == GameState.MAX_TURN) {
                repositoryGame.setGameState(GameState.MIN_TURN);
            } else {
                repositoryGame.setGameState(GameState.MAX_TURN);
            }
            try {
                gameServiceImpl.saveGame(repositoryGame);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        repositoryState = gameServiceImpl.checkGameState(repositoryGame);
        if (gameServiceImpl.isGameOverState(repositoryState)) {
            return getEndGameResponseEntity(repositoryState, repositoryGame);
        }

        return new ResponseEntity<>(domainWebMapper.toWeb(repositoryGame), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<?> availableGames() {
        List<CurrentGame> games = gameServiceImpl.getAvailableGames();
        if (games.isEmpty()) {
            return new ResponseEntity<>("No available games", HttpStatus.OK);
        }

        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/{UUID}/get")
    public ResponseEntity<?> getCurrentGame(
            @PathVariable("UUID") UUID uuid
    ) {
        CurrentGame game;
        try {
            game = gameServiceImpl.getGameById(uuid);
        } catch (Exception e) {
            return new ResponseEntity<>("Cant find current game with id: " + uuid, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(domainWebMapper.toWeb(game), HttpStatus.OK);
    }

    @GetMapping("/user/{UUID}")
    public ResponseEntity<?> getUser(
            @PathVariable("UUID") UUID uuid
    ) {
        User user;
        try {
            user = gameServiceImpl.getUserById(uuid);
        } catch (Exception e) {
            return new ResponseEntity<>("Cant find user with id: " + uuid, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/completed-games")
    public ResponseEntity<?> getCompletedGames(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = jwtProvider.validationHandlerOfAuthHeaderWithAccessToken(authHeader);
            Claims claims = jwtProvider.retrieveClaims(token);
            UUID uuid = jwtProvider.getValidatedUUID(claims);
            List<CurrentGame> completedGames = gameServiceImpl.getCompletedGamesByUserId(uuid);
            if (completedGames.isEmpty()) {
                return new ResponseEntity<>("No completed games", HttpStatus.OK);
            }
            return new ResponseEntity<>(completedGames, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Cant find user by access token", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/join/{UUID}")
    public ResponseEntity<?> joinToGame(
            @PathVariable("UUID") UUID gameId,
            @RequestAttribute UUID creatorId
    ) {
        CurrentGame repositoryGame;
        try {
            repositoryGame = gameServiceImpl.getGameById(gameId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        User secondPlayer;
        try {
            secondPlayer = gameServiceImpl.getUserById(creatorId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("userId is null", HttpStatus.BAD_REQUEST);
        }
        if (repositoryGame.getGameState() != GameState.WAITING_FOR_PLAYERS) {
            return new ResponseEntity<>("Game is not available to join", HttpStatus.NOT_FOUND);
        }
        if (repositoryGame.getPlayer1().equals(secondPlayer)) {
            return new ResponseEntity<>("You are already in the game", HttpStatus.BAD_REQUEST);
        }

        //preparing multiplayer game
        repositoryGame.setGameState(GameState.MAX_TURN);
        repositoryGame.setPlayer2(secondPlayer);
        try {
            gameServiceImpl.saveGame(repositoryGame);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(domainWebMapper.toWeb(repositoryGame), HttpStatus.OK);
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfoByAccessToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtProvider.validationHandlerOfAuthHeaderWithAccessToken(authHeader);
        Claims claims = jwtProvider.retrieveClaims(token);
        UUID uuid;
        try {
            uuid = jwtProvider.getValidatedUUID(claims);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user;
        try {
            user = gameServiceImpl.getUserById(uuid);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Cant find user with id: " + uuid, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/leaderboard/{n}")
    public ResponseEntity<?> getFirstNWinRatio(@PathVariable("n") int num) {
        List<UserWinRatio> res = gameServiceImpl.getFirstNWinRatio(num);
        if (res.isEmpty()) {
            return new ResponseEntity<>("No records", HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<WebCurrentGame> getEndGameResponseEntity(GameState repositoryState,
                                                                   CurrentGame currentGame) {
        currentGame.setGameState(repositoryState);
        UUID winnerUUID = gameServiceImpl.getWinnerUUID(currentGame);
        if (winnerUUID != null) {
            currentGame.setWinnerId(winnerUUID);
        }
        gameServiceImpl.saveGame(currentGame);
        String gameEndMessage = gameServiceImpl.getGameEndResultMessage(repositoryState);
        System.out.println(gameEndMessage);
        return new ResponseEntity<>(domainWebMapper.toWeb(currentGame), HttpStatus.OK);
    }

    private boolean isNotValidTurnMaker(CurrentGame game, UUID turnMakerId) {
        GameState state = game.getGameState();
        if (state == GameState.MAX_TURN && !turnMakerId.equals(game.getPlayer1().getUuid())) {
            return true;
        }
        if (state == GameState.MIN_TURN && !turnMakerId.equals(game.getPlayer2().getUuid())) {
            return true;
        }
        return false;
    }
}
