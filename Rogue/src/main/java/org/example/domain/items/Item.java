package org.example.domain.items;

import org.example.domain.Coords;
import org.example.domain.Entity;
import org.example.domain.characters.Player;
import org.example.domain.interfaces.Drawable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Weapon.class),
        @JsonSubTypes.Type(value = Food.class),
        @JsonSubTypes.Type(value = Elixir.class),
        @JsonSubTypes.Type(value = Scroll.class),
        @JsonSubTypes.Type(value = Treasure.class),
        @JsonSubTypes.Type(value = Key.class)
})

//TODO maybe make usable for certain Items (Not for Item class) and calls use methods for player boosts or open doors or etc
public abstract class Item extends Entity implements Drawable {
    private final String type;
    private final String subType;
    private int health;
    private int maxHealth;
    private int dexterity;
    private int strength;
    private int cost;

    public Item() {
        super(null, ' ', "");
        this.type = "";
        this.subType = "";
    }

    @JsonCreator
    public Item(@JsonProperty("position") Coords position,
                @JsonProperty("symbol") char symbol,
                @JsonProperty("name") String name,
                @JsonProperty("type") String type,
                @JsonProperty("subType") String subType,
                @JsonProperty("health") int health,
                @JsonProperty("maxHealth") int maxHealth,
                @JsonProperty("dexterity") int dexterity,
                @JsonProperty("strength") int strength,
                @JsonProperty("cost") int cost) {
        super(position, symbol, name);
        this.type = type;
        this.subType = subType;
        this.health = health;
        this.maxHealth = maxHealth;
        this.dexterity = dexterity;
        this.strength = strength;
        this.cost = cost;
    }

    public abstract char toDraw();

    @JsonIgnore
    public String getColor(){return "WHITE";};

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public int getCost() {
        return cost;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                ", subType=" + subType +
                ", health=" + health +
                ", maxHealth=" + maxHealth +
                ", dexterity=" + dexterity +
                ", strength=" + strength +
                ", cost=" + cost +
                "} " + super.toString();
    }

    public abstract String use(Player player);
}
