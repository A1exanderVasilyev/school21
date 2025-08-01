package org.example.domain.space;

import org.example.view.Color;

public class Empty extends Space{
    public static final Empty INSTANCE = new Empty(); // Единственный экземпляр
    private Empty() {}
    @Override
    public char getSymbol() {
        return ' ';
    }
    @Override
    public Color getColor() {
        return Color.BLACK;
    }
}
