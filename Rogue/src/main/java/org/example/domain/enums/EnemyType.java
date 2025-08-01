package org.example.domain.enums;

public enum EnemyType {
    ZOMBIE('z', "Zombie", 100, 10, 15, Hostility.AVERAGE),
    VAMPIRE('v', "Vampire", 100, 30, 10, Hostility.HIGH),
    GHOST('g', "Ghost", 50, 30, 5, Hostility.LOW),
    OGRE('o', "Ogre", 150, 10, 25, Hostility.AVERAGE),
    SNAKE('s', "Snake", 30, 55, 5, Hostility.HIGH),
    MIMIC('m', "Mimic", 100, 30, 8, Hostility.LOW);

    private final char symbol;
    private final String name;
    private final int maxHealth;
    private final int health;
    private final int dexterity;
    private final int strength;
    private final Hostility hostility;
    private final boolean isChasing;

    EnemyType(char symbol, String name, int maxHealth, int dexterity, int strength, Hostility hostility) {
        this.symbol = symbol;
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.dexterity = dexterity;
        this.strength = strength;
        this.hostility = hostility;
        this.isChasing = false;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public Hostility getHostility() {
        return hostility;
    }

    public boolean isChasing() {
        return isChasing;
    }
}
