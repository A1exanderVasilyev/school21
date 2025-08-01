package org.example.domain.generation;

import org.example.domain.Coords;
import org.example.domain.map.Room;

public class RoomFactory {
    private Integer partAreaFieldSize;
    private Integer roomsCount;
    final Integer MINSIZE = 3;
    final Integer MAXSIZE = 9;

    public RoomFactory(Integer roomsCount, Integer partAreaFieldSize){
        this.roomsCount = roomsCount;
        this.partAreaFieldSize= partAreaFieldSize;
    }

    public Room[] generateRooms(){
        Room[] rooms = new Room[roomsCount];
        for(int i = 0; i < rooms.length;i++){
            rooms[i] = generateRoom(i);
        }
        return rooms;
    }

    private Room generateRoom(Integer index){
        Integer length = (int) (Math.random()*(MAXSIZE - MINSIZE)) + MINSIZE;
        Integer width = (int) (Math.random()*(MAXSIZE - MINSIZE)) + MINSIZE;
        Integer xOrigin = (int) (Math.random()*(MAXSIZE - length));
        Integer yOrigin = (int) (Math.random()*(MAXSIZE - width));
        Integer xAbsolutOrigin =  xOrigin +
                (index % (int) Math.sqrt(roomsCount)) * partAreaFieldSize;
        Integer yAbsolutOrigin = yOrigin +
                (index / (int) Math.sqrt(roomsCount)) * partAreaFieldSize;
        return new Room(index, new Coords(xAbsolutOrigin, yAbsolutOrigin), length, width);
    }
}
