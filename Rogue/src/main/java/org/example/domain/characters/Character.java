package org.example.domain.characters;

import org.example.domain.Coords;
import org.example.domain.Entity;
import org.example.domain.enums.Direction;
import org.example.domain.interfaces.Drawable;
import org.example.domain.interfaces.Moveable;
import org.example.domain.items.Weapon;
import org.example.domain.map.Cell;
import org.example.domain.map.Level;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.domain.characters.enemies.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Player.class),
        @JsonSubTypes.Type(value = Zombie.class),
        @JsonSubTypes.Type(value = Vampire.class),
        @JsonSubTypes.Type(value = Ghost.class),
        @JsonSubTypes.Type(value = Ogre.class),
        @JsonSubTypes.Type(value = Snake.class),
        @JsonSubTypes.Type(value = Mimic.class)
})

public abstract class Character extends Entity implements Moveable, Drawable {
    private int maxHealth;
    private int health;
    private int dexterity;
    private int strength;


    @JsonCreator
    public Character(@JsonProperty("position") Coords position,
                     @JsonProperty("symbol") char symbol,
                     @JsonProperty("name") String name,
                     @JsonProperty("maxHealth") int maxHealth,
                     @JsonProperty("health") int health,
                     @JsonProperty("dexterity") int dexterity,
                     @JsonProperty("strength") int strength) {
        super(position, symbol, name);
        this.maxHealth = maxHealth;
        this.health = health;
        this.dexterity = dexterity;
        this.strength = strength;
    }

    public abstract char toDraw();

    @JsonIgnore
    public abstract String getColor();

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @JsonIgnore
    public boolean isAlive() {
        return health > 0;
    }

    public DamageResult dealDamage(Character target, Weapon weapon) {
        boolean isDamageDealSuccess = false;
        Random random = new Random();
        String outputMessage;
        final double baseHitChance = 0.65;
        // % chance for 1 point dexterity diff
        final double dexterityFactor = 0.02;
        double hitChance = baseHitChance + (getDexterity() - target.getDexterity()) * dexterityFactor;
        hitChance = Math.max(0.1, Math.min(hitChance, 0.9));
        double chance = (random.nextInt(99) + 1) / 100.0;
        int damage = getStrength();

        if (hitChance > chance) {
            if (weapon != null) {
                damage += weapon.getStrength();
            }
            target.setHealth(target.getHealth() - damage);
            outputMessage = String.format("%s deals %d damage to %s", getName(), damage, target.getName());
            isDamageDealSuccess = true;
        } else {
            outputMessage = String.format("%s missed while attack %s", getName(), target.getName());
        }
        return new DamageResult(isDamageDealSuccess, outputMessage);
    }

    public abstract DamageResult attack(Character target);

    @Override
    public boolean move(Direction direction, Level level, boolean isChasing) {
        if (direction == Direction.NONE) {
            return false;
        }

        Coords currentPosition = getPosition();
        Coords nextPosition = getNewPosition(currentPosition, direction);
        Cell[][] field = level.getField();

        if (!isValidPosition(nextPosition, field)) {
            return false;
        }

        Cell toMoveCell = field[nextPosition.y][nextPosition.x];

        if (isChasing) {
            if (!toMoveCell.isPassable()) {
                return false;
            }
        } else {
            if (!toMoveCell.isRoom() || toMoveCell.isCharacterOn()) {
                return false;
            }
        }

        Cell currentCell = field[currentPosition.y][currentPosition.x];
        if (this instanceof Player) {
            currentCell.setVisited(true);
            currentCell.setPlayerOn(false);
            if (currentCell.getRoom() != null) {
                currentCell.getRoom().setVisited(true);
                currentCell.getRoom().setPlayerOn(false);

            }
            toMoveCell.setPlayerOn(true);
            toMoveCell.setVisited(true);
            if (toMoveCell.getRoom() != null) {
                toMoveCell.getRoom().setPlayerOn(true);
                toMoveCell.getRoom().setVisited(true);
            }
        }

        currentCell.setCharacter(null);
        toMoveCell.setCharacter(this);
        setPosition(nextPosition);
        return true;
    }

    public Coords getNewPosition(Coords currentPosition, Direction direction) {
        return new Coords(currentPosition.x + direction.getDX(),
                currentPosition.y + direction.getDY());
    }

    private boolean isValidPosition(Coords position, Cell[][] field) {
        return position.x >= 0 && position.x < field.length
                && position.y >= 0 && position.y < field.length;
    }

    @Override
    public String toString() {
        return "Character{" +
                "maxHealth=" + maxHealth +
                ", health=" + health +
                ", dexterity=" + dexterity +
                ", strength=" + strength +
                super.toString() +
                '}';
    }
}
