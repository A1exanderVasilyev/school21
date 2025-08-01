package org.example.datalayer;

import org.example.domain.Backpack;
import org.example.domain.Coords;
import org.example.domain.characters.Player;
import org.example.domain.map.Level;

public class SavedGame {
    private Player player;
    private Coords playerPosition;
    private Backpack backpack;
    private Level currentLevel;
    private int generatedLevelsCount; // Важно для LevelFactory

    public SavedGame() {
    }

    public SavedGame(Player player, Coords playerPosition, Backpack backpack, Level currentLevel, int generatedLevelsCount) {
        this.player = player;
        this.playerPosition = playerPosition;
        this.backpack = backpack;
        this.currentLevel = currentLevel;
        this.generatedLevelsCount = generatedLevelsCount;
    }

    public Coords getPlayerPosition() { // <-- ДОБАВЛЕНО
        return playerPosition;
    }

    public void setPlayerPosition(Coords playerPosition) { // <-- ДОБАВЛЕНО
        this.playerPosition = playerPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getGeneratedLevelsCount() {
        return generatedLevelsCount;
    }

    public void setGeneratedLevelsCount(int generatedLevelsCount) {
        this.generatedLevelsCount = generatedLevelsCount;
    }
}
