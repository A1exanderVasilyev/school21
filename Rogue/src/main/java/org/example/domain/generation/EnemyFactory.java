package org.example.domain.generation;

import org.example.domain.Coords;
import org.example.domain.characters.Enemy;
import org.example.domain.characters.enemies.*;
import org.example.domain.enums.EnemyType;

public class EnemyFactory {
    public static Enemy createEnemy(Coords position, EnemyType enemyType, int level) {
        switch (enemyType) {
            case ZOMBIE -> {
                return new Zombie(position, level);
            }
            case VAMPIRE -> {
                return new Vampire(position, level);
            }
            case GHOST -> {
                return new Ghost(position, level);
            }
            case OGRE -> {
                return new Ogre(position, level);
            }
            case SNAKE -> {
                return new Snake(position, level);
            }
            case MIMIC -> {
                return new Mimic(position, level);
            }
            default -> throw new IllegalArgumentException("Unknown enemy type");
        }
    }
}
