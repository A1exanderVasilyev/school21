package org.example.domain.enums;

public enum Hostility {
    LOW(2),
    AVERAGE(3),
    HIGH(4);

    private final int radiusInCell;

    Hostility(int radiusInCell) {
        this.radiusInCell = radiusInCell;
    }

    public int getRadiusInCell() {
        return radiusInCell;
    }
}
