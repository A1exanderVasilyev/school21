package org.example.domain.space;

import org.example.view.Color;

public class RoomSpace extends Space{
    public static final RoomSpace INSTANCE = new RoomSpace();
    private RoomSpace() {}
    @Override
    public char getSymbol() {
        return '.';
    }
    @Override
    public Color getColor() {
        return Color.GREEN;
    }
}
