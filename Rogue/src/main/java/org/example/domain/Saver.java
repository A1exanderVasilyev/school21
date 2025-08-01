package org.example.domain;

import org.example.domain.characters.Enemy;
import org.example.domain.characters.Player;
import org.example.domain.items.Item;
import org.example.domain.map.Hall;
import org.example.domain.map.Room;

import java.util.List;

public class Saver {
    private String filePath;

    public Saver(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    private void savePlayer(Player player) {}
    private void saveRooms(Room[] room) {}
    private void saveHalls(Hall[] hall) {}
    private void saveEnemies(List<Enemy> enemies) {}
    private void saveItems(List<Item> items) {}
}
