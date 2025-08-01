package org.example.domain.map;

import org.example.domain.Coords;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hall {
    private List<Coords> path;

    @JsonCreator
    public Hall(@JsonProperty("path") List<Coords> path){
        this.path = path;
    }

    public List<Coords> getHall(){
        return path;
    }

    public List<Coords> getPath() {
        return path;
    }

    public void setHall(List<Coords> path) {
        this.path = path;
    }

    public void setHall() {
        this.path = null;
    }
}
