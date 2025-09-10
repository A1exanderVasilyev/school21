package com.catinasw.T03.domain.model;

import com.catinasw.T03.domain.util.GameState;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "games")
public class CurrentGame {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Convert(converter = GameFieldConverter.class)
    private GameField gameField;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state")
    private GameState gameState;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private User player2;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "winner_id")
    private UUID winnerId;

    public CurrentGame() {}

    public CurrentGame(GameField gameField, GameState gameState, User player1, User player2, Date createdAt,  UUID winnerId) {
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

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }
}
