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

public class Snake extends Enemy {
    private static final int CHANCE_TO_SLEEP_TARGET = 30;
    private final Random RANDOM = new Random();
    private final int SUCCEED_CHANCE_VALUE = 50;
    private final Direction lastDirection = Direction.NONE;

    public Snake() {
        super();
    }

    public Snake(Coords position, int level) {
        super(position, EnemyType.SNAKE, level);
        setMaxHealth(getMaxHealth() + level * 10);
        setHealth(getMaxHealth());
        setDexterity(getDexterity() + level);
        setStrength(getStrength() + level);
    }

    public char toDraw(){
        return 'S';
    }

    @Override
    public void roam(Level level) {
        Direction[] directions = {Direction.DOWN_LEFT, Direction.DOWN_RIGHT, Direction.UP_LEFT, Direction.UP_RIGHT};
        Random random = new Random();
        Direction direction = directions[random.nextInt(directions.length)];
        while (direction == lastDirection) {
            direction = directions[random.nextInt(directions.length)];
        }
        move(direction, level, false);
    }

    @Override
    public DamageResult attack(Character target) {
        DamageResult damageResult = dealDamage(target, null);
        String msg = damageResult.getMessage();
        Player targetPlayer = (Player) target;
        if (damageResult.isDamageDealt() && !targetPlayer.isSleep()) {
            if (RANDOM.nextInt(100) >= SUCCEED_CHANCE_VALUE) {
                targetPlayer.setSleep(true);
                msg += ", Player poisoned";
                damageResult.setMessage(msg);
            }
        } else {
            targetPlayer.setSleep(false);
        }
        return damageResult;
    }

    @Override
    public String toString() {
        return "Snake " + super.toString();
    }
}
