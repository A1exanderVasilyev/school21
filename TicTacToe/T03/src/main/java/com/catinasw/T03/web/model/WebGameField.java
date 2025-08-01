package com.catinasw.T03.web.model;

import static com.catinasw.T03.constant.Size.MATRIX_SIZE;

public class WebGameField {
    private int[][] gameField;

    public WebGameField() {
        gameField = new int[MATRIX_SIZE][MATRIX_SIZE];
    }

    public int[][] getGameField() {
        return gameField;
    }

    public void setGameField(int[][] gameField) {
        this.gameField = gameField;
    }
}
