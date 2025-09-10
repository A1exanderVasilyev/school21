package com.catinasw.T03.domain.util;

public enum CellState {
    MAX(10, 'X'),
    MIN(-10, '0'),
    EMPTY(0, ' ');

    private final int value;
    private final char symbol;

    CellState(int value, char symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    public int getValue() {
        return value;
    }

    public char getSymbol() {
        return symbol;
    }
}
