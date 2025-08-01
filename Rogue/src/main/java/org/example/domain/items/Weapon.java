package org.example.domain.items;

import org.example.domain.Coords;
import org.example.domain.interfaces.Drawable;
import org.example.domain.characters.Player;
import org.example.domain.enums.ItemType;
import org.example.domain.enums.WeaponSubType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Weapon extends Item implements Drawable {
    @JsonCreator
    public Weapon(@JsonProperty("position") Coords position,
                  @JsonProperty("subType") WeaponSubType subType) {
        super(position, subType.getWeaponSymbol(), subType.getWeaponName(),
                ItemType.WEAPON.name(), subType.name(), 0, 0, 0,
                subType.getWeaponDamage(), 0);
    }

    public char toDraw(){
        return super.getSymbol();
    }

    @Override
    public String use(Player player) {
        player.setWeapon(this);
        return "You equip a weapon - " + getSubType().toLowerCase() +
                " that gives " + getStrength() + " bonus to your damage";
    }
}
