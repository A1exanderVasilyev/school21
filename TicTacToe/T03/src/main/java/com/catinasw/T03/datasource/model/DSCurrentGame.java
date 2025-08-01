package com.catinasw.T03.datasource.model;

import com.catinasw.T03.domain.util.GameState;

public class DSCurrentGame {
    private int UUID;
    private DSGameField gameField;
    private GameState gameState;

    public DSCurrentGame(int UUID, DSGameField gameField, GameState gameState) {
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

    public DSGameField getGameField() {
        return gameField;
    }

    public void setGameField(DSGameField gameField) {
        this.gameField = gameField;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
