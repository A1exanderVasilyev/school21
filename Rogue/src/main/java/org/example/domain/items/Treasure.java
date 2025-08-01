package org.example.domain.items;

import org.example.domain.Coords;
import org.example.domain.characters.Player;
import org.example.domain.enums.ItemType;

public class Treasure extends Item {
    public Treasure() {
        super(new Coords(0, 0), '$', "treasure", ItemType.TREASURE.name(),
               "gold", 0, 0, 0, 0, 0);
    }

    public char toDraw(){
        return getSymbol();
    }

    @Override
    public String use(Player player) {
        return "";
    }
}
