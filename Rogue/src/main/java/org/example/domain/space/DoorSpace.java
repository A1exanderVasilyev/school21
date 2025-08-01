package org.example.domain.space;


import org.example.domain.map.Door;
import org.example.view.Color;

public class DoorSpace extends Space{

    Door link;

    public DoorSpace(){
        link = null;
    }

    public DoorSpace(Door door){
        link = door;
    }

    @Override
    public char getSymbol() {
        return '+';
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }
}
