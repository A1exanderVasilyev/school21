package org.example.domain;

import org.example.domain.characters.Player;
import org.example.domain.enums.Direction;
import org.example.domain.enums.ItemType;
import org.example.domain.items.Item;
import org.example.domain.items.Weapon;
import org.example.domain.map.Cell;
import org.example.domain.map.Level;
import org.example.domain.space.RoomSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Backpack {
    private final int MAX_TYPE_ITEMS = 9;
    private List<Item> items;
    private int treasuresCost;
    private List<Item> itemsOfType;
    private boolean isOpen;
    private boolean isOpenWeaponList;

    public Backpack() {
        items = new ArrayList<>();
        treasuresCost = 0;
        isOpen = false;
        itemsOfType = null;
        isOpenWeaponList = false;
    }

    @JsonIgnore
    public boolean isOpenWeaponList() {
        return isOpenWeaponList;
    }

    public void setOpenWeaponList(boolean openWeaponList) {
        isOpenWeaponList = openWeaponList;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<Item> getItemsOfType() {
        return itemsOfType;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getTreasuresCost() {
        return treasuresCost;
    }

    public boolean add(Item item) {
        boolean isAdded = false;
        String itemType = item.getType();
        if (itemType.equals(ItemType.TREASURE.name())) {
            treasuresCost += item.getCost();
            return true;
        }
        long backpackItemsCountWithItemType = items.stream()
                .filter(backpackItem -> backpackItem.getType().equals(itemType))
                .count();
        if (backpackItemsCountWithItemType < MAX_TYPE_ITEMS) {
            items.add(item);
            isAdded = true;
        }
        return isAdded;
    }

    public String useItem(Item item, GameSession gameSession) {
        Player player = gameSession.getPlayer();
        String res;
        if (player.getWeapon() != null && item instanceof Weapon weapon) {
            Level currentLevel = gameSession.getCurrentLevel();
            Cell[][] field = currentLevel.getField();
            Cell playerCell = field[player.getPosition().y][player.getPosition().x];
            res = switchWeaponHandler(player, playerCell, currentLevel, weapon);
            playerCell.setItem(null);
        } else {
            res = item.use(player);
        }

        items.remove(item);
        return res;
    }

    public void clearBackpack() {
        items.clear();
        treasuresCost = 0;
    }

    public void setItemsToShowByType(ItemType type) {
        if (type == ItemType.WEAPON) {
            isOpenWeaponList = true;
        }
        setOpen(true);
        itemsOfType = items.stream().
                filter(item -> item.getType().equals(type.name())).
                collect(Collectors.toList());
    }


    private String switchWeaponHandler(Player player, Cell playerCell, Level currentLevel,  Weapon weapon) {
        String res = "Cant throw out the weapon";
        Cell cellToDropOldWeapon = getFreeNeigborCell(playerCell, currentLevel);
        if (cellToDropOldWeapon != null) {
            Weapon oldWeapon = player.getWeapon();
            cellToDropOldWeapon.setItem(oldWeapon);
            currentLevel.addItem(oldWeapon);

            currentLevel.getItems().remove(weapon);
            player.setWeapon(weapon);
            res = oldWeapon.getName() + " switched on " + weapon.getName();
        }
        return res;
    }

    Cell getFreeNeigborCell(Cell playerCell, Level currentLevel) {
        Cell neigborCell = null;
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT,
                Direction.DOWN_LEFT, Direction.DOWN_RIGHT, Direction.UP_RIGHT, Direction.UP_LEFT};
        Cell[][] field = currentLevel.getField();
        Coords playerCoords = playerCell.getCoords();
        for (Direction direction : directions) {
            Cell cell = field[playerCoords.y + direction.getDY()][playerCoords.x + direction.getDX()];
            if (cell.getSpaceType() instanceof RoomSpace && cell.getItem() == null) {
                neigborCell = cell;
                return neigborCell;
            }
        }
        return neigborCell;
    }

    @Override
    public String toString() {
        return "Backpack{" + "items=" + items + ", treasuresCost=" + treasuresCost + '}';
    }
}
