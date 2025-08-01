package org.example.domain.characters;

import org.example.domain.Coords;
import org.example.domain.enums.Direction;
import org.example.domain.enums.EnemyState;
import org.example.domain.enums.EnemyType;
import org.example.domain.enums.Hostility;
import org.example.domain.items.Treasure;
import org.example.domain.map.Level;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Enemy extends Character {
    private final EnemyType enemyType;
    private final Hostility hostility;
    private boolean isChasing;
    private final int level;
    private EnemyState state;
    private final Treasure treasure;

    public Enemy() {
        super(null, ' ', "", 0,0,0,0);
        this.enemyType = null;
        this.hostility = null;
        this.level = 0;
        this.treasure = null;
    }

    public Enemy(Coords position, EnemyType enemyType, int level) {
        super(position, enemyType.getSymbol(), enemyType.getName(), enemyType.getMaxHealth(),
                enemyType.getHealth(), enemyType.getDexterity(), enemyType.getStrength());
        this.enemyType = enemyType;
        this.hostility = enemyType.getHostility();
        this.isChasing = enemyType.isChasing();
        this.level = level;
        this.state = EnemyState.ROAM;
        this.treasure = new Treasure();
        treasure.setCost((int) (0.25 * (getMaxHealth() + getHostility().ordinal() +
                getStrength() + getDexterity())));
    }

    public String getColor(){return "CYAN";}

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public Hostility getHostility() {
        return hostility;
    }

    public boolean isChasing() {
        return isChasing;
    }

    public int getLevel() {
        return level;
    }

    public void setChasing(boolean chasing) {
        isChasing = chasing;
    }

    public EnemyState getState() {
        return state;
    }

    public void setState(EnemyState state) {
        this.state = state;
    }

    public void updateState(Player player) {
        final double MAX_DISTANCE_FOR_ATTACK = 1.5;
        double distanceToPlayer = getPosition().distanceToPoint(player.getPosition());
        if (distanceToPlayer < MAX_DISTANCE_FOR_ATTACK) {
            setState(EnemyState.ATTACK);
        } else if (distanceToPlayer <= hostility.getRadiusInCell()) {
            setState(EnemyState.CHASE);
        } else {
            setState(EnemyState.ROAM);
        }
    }

    public DamageResult makeTurn(Level level, Player player) {
        updateState(player);
        DamageResult res = new DamageResult(false, null);
        switch (state) {
            case ROAM -> roam(level);
            case CHASE -> chasePlayer(level, player);
            case ATTACK -> res = attack(player);
            default -> throw new IllegalStateException("Unexpected value: " + state);
        }
        return res;
    }

    public void roam(Level level) {
        Direction[] directions = Direction.values();
        List<Direction> shuffleDirections = Arrays.asList(directions);
        Collections.shuffle(shuffleDirections);

        for (Direction d : shuffleDirections) {
            if (move(d, level, false)) {
                return;
            }
        }
    }

    public void chasePlayer(Level level, Player player) {
        Direction direction = calculateDirection(player);
        if (!move(direction, level, true)) {
            setState(EnemyState.ROAM);
        }
    }

    private Direction calculateDirection(Player player) {
        Coords playerPos = player.getPosition();
        Coords enemyPos = getPosition();

        int dx = Integer.compare(playerPos.x, enemyPos.x);
        int dy = Integer.compare(playerPos.y, enemyPos.y);

        if (dx > 0 && dy > 0) {
            return Direction.DOWN_RIGHT;
        } else if (dx > 0 && dy < 0) {
            return Direction.UP_RIGHT;
        } else if (dx < 0 && dy > 0) {
            return Direction.DOWN_LEFT;
        } else if (dx < 0 && dy < 0) {
            return Direction.UP_LEFT;
        } else if (dx > 0) {
            return Direction.RIGHT;
        } else if (dx < 0) {
            return Direction.LEFT;
        } else if (dy > 0) {
            return Direction.DOWN;
        } else if (dy < 0) {
            return Direction.UP;
        } else {
            return Direction.NONE;
        }
    }

    public Treasure getTreasure() {
        return treasure;
    }

    @Override
    public String toString() {
        return "Enemy{level=" + level + ", hostility=" + hostility + ", isChasing=" + isChasing + "} " +
                super.toString();
    }
}
