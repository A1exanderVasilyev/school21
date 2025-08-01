package org.example.domain.items;

import org.example.domain.Coords;
import org.example.domain.interfaces.Drawable;
import org.example.domain.characters.Player;
import org.example.domain.enums.FoodSubType;
import org.example.domain.enums.ItemType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Food extends Item implements Drawable {
    @JsonCreator
    public Food(@JsonProperty("position") Coords position,
                @JsonProperty("subType") FoodSubType subType) {
        super(position, subType.getFoodSymbol(), subType.getFoodName(),
                ItemType.FOOD.name(), subType.name(), subType.getHealthRestoreAmount(),
                0, 0, 0, 0);
    }

    public char toDraw(){
        return '}';
    }

    @Override
    public String use(Player player) {
        player.restoreHealth(this);
        player.incrementConsumedFoodAmount();
        return "You ate " + this.getSubType().toLowerCase() + " and restored health by " + getHealth();
    }
}
