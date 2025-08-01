package org.example.domain.space;


import org.example.view.Color;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WallSpace.class),
        @JsonSubTypes.Type(value = RoomSpace.class),
        @JsonSubTypes.Type(value = HallSpace.class),
        @JsonSubTypes.Type(value = DoorSpace.class),
        @JsonSubTypes.Type(value = Empty.class),
        @JsonSubTypes.Type(value = Exit.class)
})

public abstract class Space {

    @JsonIgnore
    public abstract char getSymbol();

    @JsonIgnore
    public abstract Color getColor();
}
