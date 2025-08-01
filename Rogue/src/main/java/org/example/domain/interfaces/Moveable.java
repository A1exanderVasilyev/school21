package org.example.domain.interfaces;

import org.example.domain.enums.Direction;
import org.example.domain.map.Level;

public interface Moveable {
    boolean move(Direction direction, Level level, boolean isChasing);
}
