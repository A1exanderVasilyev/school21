package org.example.domain.characters.enemies;

import org.example.domain.Coords;
import org.example.domain.characters.Character;
import org.example.domain.characters.DamageResult;
import org.example.domain.characters.Enemy;
import org.example.domain.enums.EnemyType;
import org.example.domain.map.Level;

public class Ogre extends Enemy {
    private boolean isSleep = false;
    private boolean isReadyToCounterattack = false;

    public Ogre() {
        super();
    }

    public Ogre(Coords position, int level) {
        super(position, EnemyType.OGRE, level);
        setMaxHealth(getMaxHealth() + level * 10);
        setHealth(getMaxHealth());
        setDexterity(getDexterity() + level);
        setStrength(getStrength() + level);
    }

    public char toDraw(){
        return 'O';
    }

    @Override
    public void roam(Level level) {
        int steps = 2;
        while (steps > 0) {
            super.roam(level);
            steps--;
        }
    }

    @Override
    public DamageResult attack(Character target) {
        DamageResult damageResult = new DamageResult(false, null);
        if (isSleep) {
            damageResult.setDamageDealt(false);
            damageResult.setMessage("Ogre sleep");
            isSleep = false;
            isReadyToCounterattack = true;
            return damageResult;
        }

        if (isReadyToCounterattack) {
            damageResult.setDamageDealt(true);
            damageResult.setMessage("Ogre counterattack: deals " + getStrength() + " damage to " + target.getName());
            target.setHealth(target.getHealth() - getStrength());
            isReadyToCounterattack = false;
        } else {
            damageResult = dealDamage(target, null);
            isSleep = true;
        }
        return damageResult;
    }

    @Override
    public String toString() {
        return "Ogre " + super.toString();
    }
}
