package org.example.domain.generation;

import org.example.domain.Coords;
import org.example.domain.enums.Direction;
import org.example.domain.map.Room;
import org.example.domain.map.Door;
import org.example.domain.enums.DoorType;

public class DoorFactory {


    public Coords generateDoor(Room room, Direction d){
        Coords originD;
        Integer newX;
        Integer newY;
        switch (d){
            case Direction.LEFT:
                newX = room.getOrigin().x;
                newY = (int) (Math.random()*(room.getRightDown().y - room.getOrigin().y - 2)) + room.getOrigin().y + 1;
                break;
            case Direction.RIGHT:
                newX = room.getRightDown().x;
                newY = (int) (Math.random()*(room.getRightDown().y - room.getOrigin().y - 2)) + room.getOrigin().y + 1;
                break;
            case Direction.UP:
                newX = (int) (Math.random()*(room.getRightDown().x - room.getOrigin().x - 2)) + room.getOrigin().x + 1;
                newY = room.getOrigin().y;
                break;
            default:
                newX = (int) (Math.random()*(room.getRightDown().x - room.getOrigin().x - 2)) + room.getOrigin().x + 1;
                newY = room.getRightDown().y;
                break;

        }
        originD = new Coords(newX, newY);
        Door door = new Door(originD, DoorType.WHITE);
        room.setDoor(door);

        return originD;
    }
}
