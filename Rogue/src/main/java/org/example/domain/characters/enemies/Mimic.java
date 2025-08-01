package org.example.domain.characters.enemies;

import org.example.domain.Coords;
import org.example.domain.characters.Character;
import org.example.domain.characters.DamageResult;
import org.example.domain.characters.Enemy;
import org.example.domain.characters.Player;
import org.example.domain.enums.EnemyType;
import org.example.domain.generation.ItemFactory;
import org.example.domain.items.Item;
import org.example.domain.map.Level;

public class Mimic extends Enemy {
    private boolean isImitate;
    private final Item itemToImitate;

    public Item getItemToImitate() {
        return itemToImitate;
    }

    public Mimic() {
        super();
        this.itemToImitate = null;
    }


    public Mimic(Coords position, int level) {
        super(position, EnemyType.MIMIC, level);
        setMaxHealth(getMaxHealth() + level * 10);
        setHealth(getMaxHealth());
        setDexterity(getDexterity() + level);
        setStrength(getStrength() + level);
        isImitate = true;
        itemToImitate = ItemFactory.generateRandomItem(new Coords(0, 0));
    }

    public char toDraw() {
        if (isImitate) {
            return itemToImitate.getSymbol();
        }
        return getSymbol();
    }

    public boolean isImitate() {
        return isImitate;
    }

    public void setImitate(boolean imitate) {
        isImitate = imitate;
    }

    @Override
    public void roam(Level level) {
        if (!isImitate) {
            super.roam(level);
        }
    }

    @Override
    public void chasePlayer(Level level, Player player) {
        if (!isImitate()) {
            super.chasePlayer(level, player);
        }
    }

    @Override
    public DamageResult attack(Character target) {
        DamageResult damageResult = new DamageResult(false, null);
        if (!isImitate()) {
            damageResult = dealDamage(target, null);
        }
        return damageResult;
    }

    @Override
    public String toString() {
        return "Ghost " +
                super.toString() +
                "{isImitate=" + isImitate +
                '}';
    }
}
