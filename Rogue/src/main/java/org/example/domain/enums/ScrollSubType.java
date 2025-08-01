package org.example.domain.enums;

public enum ScrollSubType {
    MAX_HEALTH('S', "health scroll", 10),
    DEXTERITY('S', "dexterity scroll", 10),
    STRENGTH('S', "strength scroll", 10),;

    private final char scrollSymbol;
    private final String scrollName;
    private final int buffAmount;

    ScrollSubType(char scrollSymbol, String scrollName, int buffAmount) {
        this.scrollSymbol = scrollSymbol;
        this.scrollName = scrollName;
        this.buffAmount = buffAmount;
    }

    public char getScrollSymbol() {
        return scrollSymbol;
    }

    public String getScrollName() {
        return scrollName;
    }

    public int getBuffAmount() {
        return buffAmount;
    }
}
