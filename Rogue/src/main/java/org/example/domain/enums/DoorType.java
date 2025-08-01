package org.example.domain.enums;

import org.example.constants.Colors;

public enum DoorType {
    WHITE(Colors.WHITE),
    RED(Colors.RED),
    GREEN(Colors.GREEN),
    BLUE(Colors.BLUE);

    private final String color;

    DoorType(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
