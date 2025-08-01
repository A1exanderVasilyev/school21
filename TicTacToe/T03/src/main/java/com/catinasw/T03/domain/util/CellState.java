package com.catinasw.T03.domain.util;

public enum CellState {
    MAX(10),
    MIN(-10),
    EMPTY(0);

    private final int value;

    CellState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
