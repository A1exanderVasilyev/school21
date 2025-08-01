package org.example.domain.enums;

public enum ElixirSubType {
    MAX_HEALTH('P', "max health potion", 30),
    DEXTERITY('P', "dexterity potion", 30),
    STRENGTH('P', "strength potion", 30);

    private final char elixirSymbol;
    private final String elixirName;
    private final int buffAmount;

    ElixirSubType(char elixirSymbol, String elixirName, int buffAmount) {
        this.elixirSymbol = elixirSymbol;
        this.elixirName = elixirName;
        this.buffAmount = buffAmount;
    }

    public char getElixirSymbol() {
        return elixirSymbol;
    }

    public String getElixirName() {
        return elixirName;
    }

    public int getBuffAmount() {
        return buffAmount;
    }
}
