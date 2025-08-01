package com.catinasw.T03.web.model;

import com.catinasw.T03.domain.util.GameState;

public class WebCurrentGame {
    private int UUID;
    private WebGameField gameField;
    private GameState gameState;

    public WebCurrentGame(int UUID, WebGameField gameField, GameState gameState) {
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

    public WebGameField getGameField() {
        return gameField;
    }

    public void setGameField(WebGameField gameField) {
        this.gameField = gameField;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
