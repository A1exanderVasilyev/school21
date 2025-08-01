package org.example.domain.items;

import org.example.domain.Coords;
import org.example.domain.interfaces.Drawable;
import org.example.domain.characters.Player;
import org.example.domain.enums.ElixirSubType;
import org.example.domain.enums.ItemType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Elixir extends Item implements Drawable {
    private final int DURATION = 60;
    private int boostDuration;
    public char toDraw(){
        return '^';
    }

    @JsonCreator
    public Elixir(@JsonProperty("position") Coords position,
                  @JsonProperty("subType") ElixirSubType subType) {
        super(position, subType.getElixirSymbol(), subType.getElixirName(),
                ItemType.ELIXIR.name(), subType.name(), 0, 0, 0, 0, 0);
        switch (subType) {
            case MAX_HEALTH -> setMaxHealth(subType.getBuffAmount());
            case STRENGTH -> setStrength(subType.getBuffAmount());
            case DEXTERITY -> setDexterity(subType.getBuffAmount());
            default -> throw new IllegalArgumentException("Illegal Elixir subtype: " + subType);
        }
        boostDuration = DURATION;
    }

    public int getBoostDuration() {
        return boostDuration;
    }

    public void setBoostDuration(int boostDuration) {
        this.boostDuration = boostDuration;
    }

    @Override
    public String use(Player player) {
        player.elixirConsumingHandler(this, false);
        player.incrementConsumedElixirsNumber();
        String resSubStr;
        switch (getSubType()) {
            case "MAX_HEALTH" -> resSubStr = " health and increase your max health by " + getMaxHealth();
            case "DEXTERITY" -> resSubStr = " dexterity and increase your dexterity by " + getDexterity();
            case "STRENGTH" -> resSubStr = " strength and increase your strength by " + getStrength();
            default -> throw new IllegalArgumentException("Illegal Elixir subtype: " + getSubType());
        }
        return "You used the " + this.getType().toLowerCase() + " of " + resSubStr;
    }
}
