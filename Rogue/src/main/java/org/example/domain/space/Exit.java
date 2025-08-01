package org.example.domain.space;

import org.example.view.Color;

public class Exit extends Space{

    @Override
    public char getSymbol() {
        return 'E';
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }
}

