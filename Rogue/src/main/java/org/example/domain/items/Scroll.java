package org.example.domain.items;

import org.example.domain.Coords;
import org.example.domain.characters.Player;
import org.example.domain.enums.ItemType;
import org.example.domain.enums.ScrollSubType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public class Scroll extends Item {
    @JsonCreator
    public Scroll(@JsonProperty("position") Coords position,
                  @JsonProperty("subType") ScrollSubType subType) {
        super(position, subType.getScrollSymbol(), subType.getScrollName(), ItemType.SCROLL.name(),
                subType.name(), 0, 0, 0, 0, 0);
        switch (subType) {
            case MAX_HEALTH -> setMaxHealth(subType.getBuffAmount());
            case STRENGTH -> setStrength(subType.getBuffAmount());
            case DEXTERITY -> setDexterity(subType.getBuffAmount());
            default -> throw new IllegalArgumentException("Illegal Elixir subtype: " + subType);
        }
    }

    public char toDraw(){
        return '`';
    }

    @Override
    public String use(Player player) {
        String resSubStr;
        int increaseAmount;
        switch (getSubType()) {
            case "MAX_HEALTH":
                increaseAmount = getMaxHealth();
                player.updateMaxHealth(increaseAmount);
                player.incrementReadScrollsNumber();
                resSubStr = "health";
                break;
            case "STRENGTH":
                increaseAmount = getStrength();
                player.updateStrength(increaseAmount);
                player.incrementReadScrollsNumber();
                resSubStr = getSubType().toLowerCase();
                break;
            case "DEXTERITY":
                increaseAmount = getDexterity();
                player.updateDexterity(increaseAmount);
                player.incrementReadScrollsNumber();
                resSubStr = getSubType().toLowerCase();
                break;
            default:
                throw new IllegalArgumentException("Illegal Elixir subtype: " + getSubType());
        }
        return "You have permanently improved your " + resSubStr + " by " + increaseAmount;
    }
}
