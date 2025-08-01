package org.example.domain.generation;

import org.example.domain.Coords;
import org.example.domain.characters.Enemy;
import org.example.domain.enums.Direction;
import org.example.domain.enums.EnemyType;
import org.example.domain.enums.ItemType;
import org.example.domain.items.Item;
import org.example.domain.map.Cell;
import org.example.domain.map.Room;
import org.example.domain.map.Hall;
import org.example.domain.map.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelFactory {
    Integer size;
    Integer roomsCount;
    RoomFactory rg;
    PathFactory pg;
    HallFactory hg;
    DoorFactory dg;
    private int generatedLevelsCount;

    private Random rand = new Random();

    public LevelFactory(Integer roomsCount, Integer roomSize){
        this.size = roomSize;
        this.roomsCount = roomsCount;
        hg = new HallFactory();
        rg = new RoomFactory(roomsCount, roomSize);
        pg = new PathFactory(roomsCount);
        dg = new DoorFactory();
        generatedLevelsCount = 1;
    }

    //TODO should be set while load game save
    public void setGeneratedLevelsCount(int generatedLevelsCount) {
        this.generatedLevelsCount = generatedLevelsCount;
    }

    public int getGeneratedLevelsCount() {
        return generatedLevelsCount;
    }

    public Level generateLevel(){
        Level level;
        Room[] rooms = rg.generateRooms();
        List<Hall> halls = new ArrayList<>();
        Integer startIndex = (int) (Math.random() * roomsCount);
        Integer endIndex = (int) (Math.random() * roomsCount);
        while (endIndex.equals(startIndex)){
            endIndex = (int) (Math.random() * roomsCount);
        }
        generateHallAndDoors(rooms, halls, startIndex);
        Room startRoom = rooms[startIndex];
        startRoom.setStartRoom(true);
        Room endRoom = rooms[endIndex];
        Coords spawn = getRandomCoords(startRoom.getOrigin(), startRoom.getRightDown());
        Coords exit = getRandomCoords(endRoom.getOrigin(), endRoom.getRightDown());
        endRoom.setEndRoom(true);

        List<Enemy> enemies = generateEnemies(rooms);
        List<Item> items = generateItems(rooms, exit);

        level = new Level(rooms, halls,enemies,items, startIndex, spawn, exit, size * (int) Math.sqrt(roomsCount), generatedLevelsCount);
        generatedLevelsCount++;
        return level;
    }

    private void generateHallAndDoors(Room[] rooms, List<Hall> halls, Integer startIndex){
        var paths = pg.generatePaths(startIndex);
        for(int i = 0; i < paths.size(); i++){
            Direction d1;
            Direction d2;
            Integer indexRoom1 = paths.get(i)[0];
            Integer indexRoom2 = paths.get(i)[1];
            if(indexRoom2 - indexRoom1 == 1){
                d1 = Direction.RIGHT;
                d2 = Direction.LEFT;
            }else{
                d1 = Direction.DOWN;
                d2 = Direction.UP;
            }
            Coords door1 = dg.generateDoor(rooms[indexRoom1], d1);
            Coords door2 = dg.generateDoor(rooms[indexRoom2], d2);

            Coords startHall = null;
            switch (d1){
                case Direction.RIGHT:
                    startHall = new Coords(door1.x + 1, door1.y);
                    break;
                case Direction.DOWN:
                    startHall = new Coords(door1.x, door1.y + 1);
                    break;
            }

            Coords endHall = null;
            switch (d2){
                case Direction.LEFT:
                    endHall = new Coords(door2.x - 1, door2.y);
                    break;
                case Direction.UP:
                    endHall = new Coords(door2.x, door2.y - 1);
                    break;
            }

            Coords metaStart;
            Coords metaEnd;

            if(d1 == Direction.RIGHT){
                metaStart = new Coords(startHall.x, rooms[indexRoom1].getOrigin().y);
                metaEnd = new Coords(endHall.x, rooms[indexRoom1].getRightDown().y);
            }else{
                metaStart = new Coords(rooms[indexRoom1].getOrigin().x, startHall.y);
                metaEnd = new Coords(rooms[indexRoom1].getRightDown().x, endHall.y);
            }

            Hall hall = hg.generateHall(startHall, endHall, metaStart, metaEnd);
            halls.add(hall);
        }
    }

    private List<Enemy> generateEnemies(Room[] rooms) {
        List<Enemy> enemies = new ArrayList<>();
        EnemyType[] enemyTypes = EnemyType.values();
        for (Room room : rooms) {
            if (!room.isStartRoom()) {
                int enemyNumber = randomNumber(0, 2 + generatedLevelsCount / 7);
                for (int i = 0; i < enemyNumber; i++) {
                    int enemyTypeIdx = rand.nextInt(enemyTypes.length);
                    enemies.add(EnemyFactory.createEnemy(getRandomCoords(room.getOrigin(), room.getRightDown()),
                            enemyTypes[enemyTypeIdx], generatedLevelsCount));
                }
            }
        }
        return enemies;
    }

    private List<Item> generateItems(Room[] rooms, Coords exitPoint) {
        List<Item> items = new ArrayList<>();
        final int baseMaxItemPerLevel = 10;
        final int baseMaxItemPerRoom = 2;
        final double decreaseRate = 0.2;

        int maxItemsAtLevel = (int) (Math.max(0, baseMaxItemPerLevel - (generatedLevelsCount * decreaseRate)));

        for (Room room : rooms) {
            if (maxItemsAtLevel <= 0) {
                break;
            }
            if (!room.isStartRoom()) {
                int roomItemCount = (int) (Math.max(0, baseMaxItemPerRoom - (generatedLevelsCount * decreaseRate)));
                for (int i = 0; i < roomItemCount; i++) {
                    Coords spawnPoint = getRandomCoords(room.getOrigin(), room.getRightDown());
                    while (spawnPoint.equals(exitPoint)) {
                        spawnPoint = getRandomCoords(room.getOrigin(), room.getRightDown());
                    }
                    items.add(ItemFactory.generateRandomItem(spawnPoint));
                    maxItemsAtLevel--;
                }
            }
        }
        return items;
    }

    private Coords getRandomCoords(Coords upperLeft, Coords lowerRight){
        int x = upperLeft.x + 1 + rand.nextInt(lowerRight.x - upperLeft.x - 1);
        int y = upperLeft.y + 1 + rand.nextInt(lowerRight.y - upperLeft.y - 1);
        return new Coords(x, y);
    }

    private int randomNumber(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }
}
