package com.catinasw.T03.domain.model;

import com.catinasw.T03.domain.util.GameState;

public class CurrentGame {
    private int UUID;
    private GameField gameField;
    private GameState gameState;

    public CurrentGame(int UUID, GameField gameField, GameState gameState) {
        this.UUID = UUID;
        this.gameField = gameField;
        this.gameState = gameState;
    }

    public int getUUID() {
        return UUID;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    public GameField getGameField() {
        return gameField;
    }

    public void setGameField(GameField gameField) {
        this.gameField = gameField;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
