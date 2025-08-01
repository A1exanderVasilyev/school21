package org.example.domain.characters.enemies;

import org.example.domain.Coords;
import org.example.domain.characters.Character;
import org.example.domain.characters.DamageResult;
import org.example.domain.characters.Enemy;
import org.example.domain.enums.EnemyType;

public class Zombie extends Enemy {

    public Zombie() {
        super();
    }

    public Zombie(Coords position, int level) {
        super(position, EnemyType.ZOMBIE, level);
        setMaxHealth(getMaxHealth() + level * 10);
        setHealth(getMaxHealth());
        setDexterity(getDexterity() + level);
        setStrength(getStrength() + level);
    }

    @Override
    public DamageResult attack(Character target) {
        return dealDamage(target, null);
    }

    public char toDraw(){
        return 'Z';
    }

    @Override
    public String toString() {
        return "Zombie " + super.toString();
    }
}
