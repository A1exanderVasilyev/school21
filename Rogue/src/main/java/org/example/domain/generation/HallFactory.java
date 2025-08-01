package org.example.domain.generation;

import org.example.domain.Coords;
import org.example.domain.map.Hall;

import java.util.*;

public class HallFactory {

    public HallFactory(){

    }

    public Coords newPoint(Coords first, Coords second){
        if(first.x == second.x || first.y == second.y){
            return null;
        }

        return new Coords(second.x, first.y);
    }

    public Hall generateHall(Coords start, Coords end, Coords leftUp, Coords rightDown){
        Hall newHall;
        Integer randPoints = 1;

        List<Coords> path = new ArrayList<>();
        path.add(start);

        List<Coords> points = new ArrayList<>();

        for(int i = 0; i < randPoints; i++){
            Integer newX = (int) (Math.random() * (rightDown.x - leftUp.x) + leftUp.x);
            Integer newY = (int) (Math.random() * (rightDown.y - leftUp.y) + leftUp.y);
            points.add(new Coords(newX, newY));
        }

        for(int i = 0; i < randPoints; i++){
            Coords newPoint = newPoint(path.get(path.size() - 1), points.get(i));
            if(newPoint != null){
                path.add(newPoint);
            }
            path.add(points.get(i));
        }

        Coords newPoint = newPoint(path.get(path.size() - 1), end);
        if(newPoint != null){
            path.add(newPoint);
        }
        path.add(end);

        newHall = new Hall(path);


        return newHall;
    }
}
