package org.example.domain.map;

import org.example.domain.Coords;
import org.example.domain.characters.Character;
import org.example.domain.items.Item;
import org.example.domain.space.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cell {
    public Space st;
    public Coords coords;
    public Character ch;
    public Item item;
    private boolean visited = false;
    private boolean playerOn = false;
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void updateVisited (){
        if (room != null){
            if (!(this.visited && !room.isVisited())){
                this.visited = room.isVisited();
                this.playerOn = room.isPlayerOn();
            }
        }
    }

    public void setRoom(Room room) {
        this.room = room;
        this.visited = room.isVisited();
    }

    @JsonIgnore
    public boolean isCharacterOn() {
        return ch != null;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isPlayerOn() {
        return playerOn;
    }

    public void setPlayerOn(boolean playerOn) {
        this.playerOn = playerOn;
    }

    public Cell(){
        coords = new Coords(0,0);
        st = Empty.INSTANCE;
    }

    //TODO for my tests
    public Cell(Coords coords, Space st, Character ch, Item item) {
        this.st = st;
        this.coords = coords;
        this.ch = ch;
        this.item = item;
    }

    public Cell(Integer x, Integer y){
        coords = new Coords(x,y);
        st = Empty.INSTANCE;
        this.visited = false;
        this.playerOn = false;
    }

    public Space getSt() {
        return st;
    }

    public void setSt(Space st) {
        this.st = st;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        if (item != null) {
            item.setPosition(coords);
        }
    }

    public Character getCharacter() {
        return ch;
    }

    @JsonIgnore
    public Space getSpaceType(){return st;}

    public void setCharacter(Character ch) {
        this.ch = ch;
    }

    @JsonIgnore
    public boolean isPassable() {
        return (isRoom() || st == HallSpace.INSTANCE ||
                st instanceof DoorSpace || st instanceof Exit) && !isCharacterOn();
    }

    @JsonIgnore
    public boolean isRoom() {
        return st instanceof RoomSpace;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "st=" + st +
                ", coords=" + coords +
                ", ch=" + ch +
                ", item=" + item +
                '}';
    }
}
