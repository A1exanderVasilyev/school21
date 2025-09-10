package com.catinasw.T03.domain.service;

import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.model.UserWinRatio;
import com.catinasw.T03.domain.util.GameState;

import java.util.List;
import java.util.UUID;

public interface GameService {
    CurrentGame getNextMove(CurrentGame currentGame);

    void validateGameField(CurrentGame oldGame, CurrentGame newGame);

    GameState checkGameState(CurrentGame currentGame);

    String getGameEndResultMessage(GameState gameState);

    void saveGame(CurrentGame repositoryGame);

    CurrentGame createNewGame(UUID creatorId, boolean isSingleGame);

    List<CurrentGame> getCompletedGamesByUserId(UUID userId);

    List<UserWinRatio> getFirstNWinRatio(int recordsAmount);
}
