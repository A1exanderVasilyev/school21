package org.example.domain.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.domain.Coords;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Room {
    private Integer index;
    private Coords origin;
    private Integer length;
    private Integer width;
    private List<Door> doors;
    private boolean visited = false;
    private boolean playerOn = false;
    private boolean isStartRoom;
    private boolean isEndRoom;

    public Room() {
        this.doors = new ArrayList<>();
    }

    public Room(Integer index, Coords origin, Integer length, Integer width){
        this.index = index;
        this.origin = origin;
        this.length = length;
        this.width = width;
        doors = new ArrayList<>();
        this.visited = false;
        this.playerOn = false;
    }

    @JsonCreator
    public Room(@JsonProperty("index") Integer index,
                @JsonProperty("origin") Coords origin,
                @JsonProperty("length") Integer length,
                @JsonProperty("width") Integer width,
                @JsonProperty("doors") List<Door> doors,
                @JsonProperty("visited") boolean visited,
                @JsonProperty("playerOn") boolean playerOn,
                @JsonProperty("startRoom") boolean isStartRoom,
                @JsonProperty("endRoom") boolean isEndRoom) {
        this.index = index;
        this.origin = origin;
        this.length = length;
        this.width = width;
        this.doors = (doors != null) ? doors : new ArrayList<>();
        this.visited = visited;
        this.playerOn = playerOn;
        this.isStartRoom = isStartRoom;
        this.isEndRoom = isEndRoom;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setPlayerOn(boolean playerOn) {
        this.playerOn = playerOn;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isPlayerOn() {
        return playerOn;
    }

    public List<Door> getDoors(){
        return doors;
    }

    public Coords getOrigin() {
        return origin;
    }

    @JsonIgnore
    public Coords getRightDown() {
        return new Coords(origin.x + length, origin.y + width);
    }

    public Integer getLength(){
        return length;
    }

    public Integer getWidth(){
        return width;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setOrigin(Coords origin) {
        this.origin = origin;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }


    public void setDoor(Door door){
        doors.add(door);
    }

    public boolean isStartRoom() {
        return isStartRoom;
    }

    public void setStartRoom(boolean startRoom) {
        isStartRoom = startRoom;
    }

    public boolean isEndRoom() {
        return isEndRoom;
    }

    public void setEndRoom(boolean endRoom) {
        isEndRoom = endRoom;
    }
}