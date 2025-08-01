package org.example.domain.generation;

import org.example.domain.Coords;
import org.example.domain.enums.*;
import org.example.domain.items.*;

import java.util.Random;

public class ItemFactory {
    private static Random RANDOM = new Random();

    public static Item generateRandomItem(Coords position) {
        ItemType[] itemTypes = {ItemType.WEAPON, ItemType.FOOD, ItemType.ELIXIR, ItemType.SCROLL};
        ItemType itemType = itemTypes[RANDOM.nextInt(itemTypes.length)];
        return switch (itemType) {
            case FOOD -> new Food(position, getRandomItemOfSubType(FoodSubType.values()));
            case ELIXIR -> new Elixir(position, getRandomItemOfSubType(ElixirSubType.values()));
            case SCROLL -> new Scroll(position, getRandomItemOfSubType(ScrollSubType.values()));
            case WEAPON -> new Weapon(position, getRandomItemOfSubType(WeaponSubType.values()));
            default -> throw new IllegalStateException("Unexpected value: " + itemType);
        };
    }

    private static <T> T getRandomItemOfSubType(T[] items) {
        return items[RANDOM.nextInt(items.length)];
    }
}
