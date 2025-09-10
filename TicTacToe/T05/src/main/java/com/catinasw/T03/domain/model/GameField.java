package com.catinasw.T03.domain.model;

import static com.catinasw.T03.constant.Size.MATRIX_SIZE;

public class GameField {
    private int[][] gameField;

    public GameField() {
        gameField = new int[MATRIX_SIZE][MATRIX_SIZE];
    }

    public int[][] getGameField() {
        return gameField;
    }

    public void setGameField(int[][] gameField) {
        this.gameField = gameField;
    }
}
