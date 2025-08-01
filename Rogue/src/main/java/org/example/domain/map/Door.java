package org.example.domain.map;

import org.example.domain.Coords;
import org.example.domain.enums.DoorType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Door {
    private DoorType color;
    private Coords position;

    @JsonCreator
    public Door(@JsonProperty("position") Coords position,
                @JsonProperty("color") DoorType color){
        this.position = position;
        this.color = color;
    }

    public Coords getPosition(){
        return position;
    }

    public void setColor(DoorType color) {
        this.color = color;
    }

    public void setPosition(Coords position) {
        this.position = position;
    }

    public Door(){
    }
}
