package org.example.domain.enums;

public enum WeaponSubType {
    AXE('T', "axe", 20),
    SWORD('±', "sword", 30),
    LONGSWORD('/', "longsword", 40);

    private final char weaponSymbol;
    private final String weaponName;
    private final int weaponDamage;

    WeaponSubType(char weaponSymbol, String weaponName, int weaponDamage) {
        this.weaponSymbol = weaponSymbol;
        this.weaponName = weaponName;
        this.weaponDamage = weaponDamage;
    }

    public char getWeaponSymbol() {
        return weaponSymbol;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public int getWeaponDamage() {
        return weaponDamage;
    }
}
