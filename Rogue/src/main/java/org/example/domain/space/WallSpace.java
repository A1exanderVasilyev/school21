package org.example.domain.space;

import org.example.view.Color;

public class WallSpace extends Space{
    public static final WallSpace INSTANCE = new WallSpace();
    private WallSpace() {}
    @Override
    public char getSymbol() {
        return '█';
    }
    @Override
    public Color getColor() {
        return Color.ORANGE;
    }
}
