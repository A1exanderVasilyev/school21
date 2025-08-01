package org.example.domain.characters.enemies;

import org.example.domain.Coords;
import org.example.domain.characters.Character;
import org.example.domain.characters.DamageResult;
import org.example.domain.characters.Enemy;
import org.example.domain.enums.EnemyType;

public class Vampire extends Enemy {
    private static final double TARGET_MAX_HEALTH_DAMAGE_PERCENT = 0.05;
    private boolean isAttackMissable = true;

    public Vampire() {
        super();
    }

    public Vampire(Coords position, int level) {
        super(position, EnemyType.VAMPIRE, level);
        setMaxHealth(getMaxHealth() + level * 10);
        setHealth(getMaxHealth());
        setDexterity(getDexterity() + level);
        setStrength(getStrength() + level);
    }

    public boolean isAttackMissable() {
        return isAttackMissable;
    }

    public void setAttackMissable(boolean attackMissable) {
        isAttackMissable = attackMissable;
    }

    public char toDraw(){
        return 'V';
    }

    @Override
    public DamageResult attack(Character target) {
        DamageResult damageResult = dealDamage(target, null);
        String message = damageResult.getMessage();
        if (damageResult.isDamageDealt()) {
            int healthReduction = (int) (target.getMaxHealth() * TARGET_MAX_HEALTH_DAMAGE_PERCENT);
            target.setMaxHealth(Math.max(1, target.getMaxHealth() - healthReduction));
            message += ", " + target.getName() + " lost " + healthReduction + " max health";
        }
        damageResult.setMessage(message);
        return damageResult;
    }

    @Override
    public String toString() {
        return "Vampire " +
                super.toString() +
                "{isAttackMissable=" + isAttackMissable +
                '}';
    }
}
