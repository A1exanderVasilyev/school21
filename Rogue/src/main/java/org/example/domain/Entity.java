package org.example.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.domain.characters.Character;
import org.example.domain.items.Item;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Character.class),
        @JsonSubTypes.Type(value = Item.class)
})

public abstract class Entity {
    private Coords position;
    private char symbol;
    private String name;

    @JsonCreator
    public Entity(@JsonProperty("position") Coords position,
                  @JsonProperty("symbol") char symbol,
                  @JsonProperty("name") String name) {
        this.position = position;
        this.symbol = symbol;
        this.name = name;
    }

    public Coords getPosition() {
        return position;
    }

    public void setPosition(Coords position) {
        this.position = position;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  " position=" + position +
                ", symbol=" + symbol +
                ", name='" + name + "' ";
    }
}
