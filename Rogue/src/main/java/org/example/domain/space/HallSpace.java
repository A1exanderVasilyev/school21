package org.example.domain.space;

import org.example.view.Color;

public class HallSpace extends Space{
    public static final HallSpace INSTANCE = new HallSpace();
    private HallSpace() {}
    @Override
    public char getSymbol() {
        return '.';
    }
    @Override
    public Color getColor() {
        return Color.GRAY;
    }
}
