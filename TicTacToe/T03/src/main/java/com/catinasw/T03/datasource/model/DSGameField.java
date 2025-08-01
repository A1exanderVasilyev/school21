package com.catinasw.T03.datasource.model;

import static com.catinasw.T03.constant.Size.MATRIX_SIZE;

public class DSGameField {
    private int[][] gameField;

    public DSGameField() {
        gameField = new int[MATRIX_SIZE][MATRIX_SIZE];
    }

    public int[][] getGameField() {
        return gameField;
    }

    public void setGameField(int[][] gameField) {
        this.gameField = gameField;
    }
}
