package com.catinasw.T03.domain.service;

import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.util.GameState;

public interface GameService {
    CurrentGame getNextMove(CurrentGame currentGame);

    void validateGameField(CurrentGame oldGame, CurrentGame newGame);

    GameState checkGameState(CurrentGame currentGame);

    String getGameEndResultMessage(GameState gameState);

    void saveGame(CurrentGame currentGame);

    int createNewGame();
}
