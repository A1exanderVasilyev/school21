package org.example.domain.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.domain.Backpack;
import org.example.domain.Coords;
import org.example.domain.characters.Enemy;
import org.example.domain.items.Item;
import org.example.domain.space.*;

import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Level {
    private int levelNumber;
    private Room[] rooms;
    private List<Hall> halls;
    private List<Enemy> enemies;
    private List<Item> items;
    private Cell[][] field;
    private Integer startRoomIndex;
    private Coords startCoords;

    public Integer getStartRoomIndex() {
        return startRoomIndex;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    private Coords exitCoords;
    private int fieldSize;

    public Level() {}

    public Level(Room[] rooms, List<Hall> halls, List<Enemy> enemies, List<Item> items, Integer startRoomIndex, Coords startCoords, Coords exitCoords, Integer fieldSize, int levelNumber){
        this.fieldSize = fieldSize;
        this.levelNumber = levelNumber;
        field = new Cell[fieldSize][fieldSize];
        this.rooms = rooms;
        this.halls = halls;
        this.startRoomIndex = startRoomIndex;
        this.startCoords = startCoords;
        this.exitCoords = exitCoords;
        numerateField();
        this.enemies = enemies;
        this.items = items;
    }

    @JsonCreator
    public Level(@JsonProperty("levelNumber") int levelNumber,
                 @JsonProperty("rooms") Room[] rooms,
                 @JsonProperty("halls") List<Hall> halls,
                 @JsonProperty("enemies") List<Enemy> enemies,
                 @JsonProperty("items") List<Item> items,
                 @JsonProperty("field") Cell[][] field,
                 @JsonProperty("startRoomIndex") Integer startRoomIndex,
                 @JsonProperty("startCoords") Coords startCoords,
                 @JsonProperty("exitCoords") Coords exitCoords) {
        this.levelNumber = levelNumber;
        this.rooms = rooms;
        this.halls = halls;
        this.enemies = enemies;
        this.items = items;
        this.field = field;
        this.startRoomIndex = startRoomIndex;
        this.startCoords = startCoords;
        this.exitCoords = exitCoords;
    }
    public Room[] getRooms() {
        return rooms;
    }

    public List<Hall> getHalls() {
        return halls;
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Coords getExitCoords() {
        return exitCoords;
    }

    public Coords getStartCoords() {
        return startCoords;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void setHalls(List<Hall> halls) {
        this.halls = halls;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setField(Cell[][] field) {
        this.field = field;
    }

    public void setStartRoomIndex(Integer startRoomIndex) {
        this.startRoomIndex = startRoomIndex;
    }

    public void setStartCoords(Coords startCoords) {
        this.startCoords = startCoords;
    }

    public void setExitCoords(Coords exitCoords) {
        this.exitCoords = exitCoords;
    }

    public Level(Integer fieldSize){
        field = new Cell[fieldSize][fieldSize];
        numerateField();
    }

    private void numerateField(){
        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field.length; j++){
                field[i][j] = new Cell(j ,i);
            }
        }
    }

    public void initializeField() {
        if (this.field == null) {
            this.field = new Cell[fieldSize][fieldSize];
            numerateField();
        }
    }

    public void fullField(){
        if (rooms != null) {
            roomsToField();
        }
        if (halls != null) {
            hallsToField();
        }
        if (rooms != null && halls != null) { // Двери зависят от комнат и коридоров
            doorsToFiled();
        }
        if (enemies != null) {
            enemiesToField();
        }
        if (items != null) {
            itemsToField();
        }
        if (exitCoords != null) {
            exitToField();
        }
    }

    public void setRooms(Room[] rooms){
        this.rooms = rooms;
    }

    public Cell[][] getField(){
        for (int i = 0; i < field.length; ++i){
            for (int j = 0; j < field[i].length; ++j){
                field[i][j].updateVisited();
                if (field[i][j].getSt() == HallSpace.INSTANCE && field[i][j].isPlayerOn()){
                    if (i + 1 < field.length && (field[i+1][j].getSt() == HallSpace.INSTANCE || field[i+1][j].getSt() == WallSpace.INSTANCE || field[i+1][j].getSt() instanceof DoorSpace)){field[i+1][j].setVisited(true);}
                    if (i - 1 >= 0 && (field[i-1][j].getSt() == HallSpace.INSTANCE || field[i-1][j].getSt() == WallSpace.INSTANCE || field[i-1][j].getSt() instanceof DoorSpace)){field[i-1][j].setVisited(true);}
                    if (j + 1 < field[i].length && (field[i][j+1].getSt() == HallSpace.INSTANCE || field[i][j+1].getSt() == WallSpace.INSTANCE|| field[i][j+1].getSt() instanceof DoorSpace)){field[i][j+1].setVisited(true);}
                    if (j - 1 >= 0 && (field[i][j-1].getSt() == HallSpace.INSTANCE || field[i][j-1].getSt() == WallSpace.INSTANCE || field[i][j-1].getSt() instanceof DoorSpace)){field[i][j-1].setVisited(true);}
                }
            }
        }
        return field;
    }

    private void exitToField(){
        field[exitCoords.y][exitCoords.x].st = new Exit();
    }

    private void enemiesToField(){
        if (enemies == null) {
            return;
        }
        for (Enemy e : enemies) {
            Coords enemyPos = e.getPosition();
            field[enemyPos.y][enemyPos.x].ch = e;
        }
    }

    private void itemsToField(){
        if (items == null) {
            return;
        }
        for (Item item : items) {
            Coords itemPos = item.getPosition();
            field[itemPos.y][itemPos.x].item = item;
        }
    }

    private void roomsToField(){
        for(int i = 0; i < rooms.length;i++){
            roomToField(rooms[i]);
        }
    }

    private void roomToField(Room room){
        Coords origin = room.getOrigin();
        Integer length = room.getLength();
        Integer width = room.getWidth();
        Integer xLeftBorder = room.getOrigin().x;
        Integer xRightBorder = room.getRightDown().x;
        Integer yUoBorder = room.getOrigin().y;
        Integer yDownBorder = room.getRightDown().y;

        for(int y = room.getOrigin().y; y <= room.getRightDown().y; y++){
            for(int x = room.getOrigin().x; x <= room.getRightDown().x; x++){
                if(y == yDownBorder || y == yUoBorder || x == xLeftBorder || x == xRightBorder){
                    field[y][x].st = WallSpace.INSTANCE;
                }else{
                    field[y][x].st = RoomSpace.INSTANCE;
                }
                field[y][x].setRoom(room);
            }
        }
    }

    private void hallsToField(){
        for(int i = 0; i < halls.size(); i++){
            hallToField(halls.get(i));
        }
    }

    private void hallToField(Hall hall){
        List<Coords> path= hall.getHall();
        for(int i =0; i < path.size() - 1; i++){
            Coords firstPoint = path.get(i);
            Coords secondPoint = path.get(i+1);
            hallLineToField(firstPoint, secondPoint, hall);
        }
    }

    private void hallLineToField(Coords firstPoint, Coords secondPoint, Hall hall){
        if (firstPoint.x == secondPoint.x) {
            int yStart = Math.min(firstPoint.y, secondPoint.y);
            int yEnd = Math.max(firstPoint.y, secondPoint.y);
            for (int y = yStart; y <= yEnd; y++) {
                field[y][firstPoint.x].st = HallSpace.INSTANCE;
            }
        } else {
            int xStart = Math.min(firstPoint.x, secondPoint.x);
            int xEnd = Math.max(firstPoint.x, secondPoint.x);
            for (int x = xStart; x <= xEnd; x++) {
                field[firstPoint.y][x].st = HallSpace.INSTANCE;
            }
        }
    }

    private void doorsToFiled(){
        for(int i = 0; i < rooms.length; i++){
            var doors = rooms[i].getDoors();
            for(int j = 0; j < doors.size(); j++){
                doorToField(doors.get(j));
            }
        }
    }

    public long handleDeadEnemiesAndGetTheirNumber(Backpack backpack) {
        if (enemies.isEmpty()) {
            return 0;
        }
        long countDead = enemies.stream().
                filter(enemy -> !enemy.isAlive()).
                count();
        enemies.removeIf(enemy -> {
            if (!enemy.isAlive()) {
                field[enemy.getPosition().y][enemy.getPosition().x].ch = null;
                backpack.add(enemy.getTreasure());
                return true;
            }
            return false;
        });
        return countDead;
    }

    private void doorToField(Door door){
        field[door.getPosition().y][door.getPosition().x].st = new DoorSpace(door);
    }

    @Override
    public String toString() {    return "Level{" +
            "levelNumber=" + levelNumber +            ", rooms=" + Arrays.toString(rooms) +
            ", halls=" + halls +            ", enemies=" + enemies +
            ", items=" + items +            ", field=" + Arrays.toString(field) +
            ", startRoomIndex=" + startRoomIndex +            ", startCoords=" + startCoords +
            ", exitCoords=" + exitCoords +            '}';
    }
}
