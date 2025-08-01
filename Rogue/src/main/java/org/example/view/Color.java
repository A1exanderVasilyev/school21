package org.example.view;

import com.googlecode.lanterna.TextColor;

public enum Color {
    GREEN (TextColor.ANSI.GREEN),
    BLACK (TextColor.ANSI.BLACK),
    CYAN (TextColor.ANSI.CYAN),
    RED (TextColor.ANSI.RED),
    YELLOW(TextColor.ANSI.YELLOW),
    WHITE(TextColor.ANSI.WHITE),
    GRAY (TextColor.ANSI.BLACK_BRIGHT),
    ORANGE (TextColor.ANSI.RED_BRIGHT);

    public TextColor.ANSI getCol() {
        return col;
    }

    private final TextColor.ANSI col;

    Color (TextColor.ANSI col) {
        this.col = col;
    }
}
