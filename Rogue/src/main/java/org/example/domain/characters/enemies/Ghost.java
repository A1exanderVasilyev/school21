package org.example.domain.characters.enemies;

import org.example.domain.Coords;
import org.example.domain.characters.Character;
import org.example.domain.characters.DamageResult;
import org.example.domain.characters.Enemy;
import org.example.domain.characters.Player;
import org.example.domain.enums.Direction;
import org.example.domain.enums.EnemyType;
import org.example.domain.map.Level;

import java.util.Random;

public class Ghost extends Enemy {
    private boolean isInvisible = false;

    public Ghost() {
        super();
    }

    public Ghost(Coords position, int level) {
        super(position, EnemyType.GHOST, level);
        setMaxHealth(getMaxHealth() + level * 10);
        setHealth(getMaxHealth());
        setDexterity(getDexterity() + level);
        setStrength(getStrength() + level);
    }

    public char toDraw(){
        if (isInvisible){
            return ' ';
        }
        return 'G';
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    @Override
    public void roam(Level level) {
        Direction[] directions = Direction.values();
        Random random = new Random();
        Direction direction;
        int randomNum = random.nextInt(10, 20);
        for (int i = 0; i < randomNum; i++) {
            direction = directions[random.nextInt(directions.length)];
            move(direction, level, false);
        }

        setInvisible(!isInvisible());
    }

    @Override
    public void chasePlayer(Level level, Player player) {
        if (isInvisible()) {
            setInvisible(false);
        }

        super.chasePlayer(level, player);
    }

    @Override
    public DamageResult attack(Character target) {
        if (isInvisible()) {
            setInvisible(false);
        }
        return dealDamage(target, null);
    }

    @Override
    public String toString() {
        return "Ghost " +
                super.toString() +
                "{isInvisible=" + isInvisible +
                '}';
    }
}
