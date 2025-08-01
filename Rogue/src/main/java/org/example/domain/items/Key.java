package org.example.domain.items;

import org.example.domain.Coords;
import org.example.domain.interfaces.Drawable;
import org.example.domain.characters.Player;
import org.example.domain.enums.DoorType;
import org.example.domain.enums.ItemType;

public class Key extends Item implements Drawable {
    private final DoorType doorType;
    private static final char KEY_SYMBOL = 'K';
    private static final String KEY_NAME = "key";

    public Key(Coords position, DoorType doorType) {
        super(position, KEY_SYMBOL, KEY_NAME, ItemType.KEY.name(), doorType.name(),
                0, 0, 0, 0, 0);
        this.doorType = doorType;
    }

    public char toDraw(){
        return '~';
    }

    public DoorType getDoorType() {
        return doorType;
    }

    @Override
    public String use(Player player) {
        return "";
    }
}
