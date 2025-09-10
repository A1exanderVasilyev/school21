package com.catinasw.T03.web.model;

import com.catinasw.T03.domain.model.User;
import com.catinasw.T03.domain.util.GameState;

import java.util.Date;
import java.util.UUID;

public class WebCurrentGame {
    private UUID uuid;
    private WebGameField gameField;
    private GameState gameState;
    private final User player1;
    private final User player2;
    private final Date createdAt;
    private final UUID winnerId;

    public WebCurrentGame(UUID uuid, WebGameField gameField, GameState gameState, User player1, User player2, Date createdAt, UUID winnerId) {
        this.uuid = uuid;
        this.gameField = gameField;
        this.gameState = gameState;
        this.player1 = player1;
        this.player2 = player2;
        this.createdAt = createdAt;
        this.winnerId = winnerId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getWinnerId() {
        return winnerId;
    }
}
