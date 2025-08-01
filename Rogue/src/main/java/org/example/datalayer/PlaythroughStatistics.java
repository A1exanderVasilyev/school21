package org.example.datalayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaythroughStatistics {
    private String playerName;
    private int treasuresCost;
    private int reachedLevelCount;
    private long defeatedEnemiesCount;
    private int consumedFoodAmount;
    private int consumedElixirsNumber;
    private int readScrollsNumber;
    private int hitsNumber;
    private int missesNumber;
    private int traversedCellsNumber;
    private boolean isGameComplete;

    public PlaythroughStatistics() {
    }

    @JsonCreator
    public PlaythroughStatistics(
            @JsonProperty("playerName") String playerName,
            @JsonProperty("treasuresCost") int treasuresCost,
            @JsonProperty("reachedLevelCount") int reachedLevelCount,
            @JsonProperty("defeatedEnemiesCount") long defeatedEnemiesCount,
            @JsonProperty("consumedFoodAmount") int consumedFoodAmount,
            @JsonProperty("consumedElixirsNumber") int consumedElixirsNumber,
            @JsonProperty("readScrollsNumber") int readScrollsNumber,
            @JsonProperty("hitsNumber") int hitsNumber,
            @JsonProperty("missesNumber") int missesNumber,
            @JsonProperty("traversedCellsNumber") int traversedCellsNumber,
            @JsonProperty("gameComplete") boolean isGameComplete) {
        this.playerName = playerName;
        this.treasuresCost = treasuresCost;
        this.reachedLevelCount = reachedLevelCount;
        this.defeatedEnemiesCount = defeatedEnemiesCount;
        this.consumedFoodAmount = consumedFoodAmount;
        this.consumedElixirsNumber = consumedElixirsNumber;
        this.readScrollsNumber = readScrollsNumber;
        this.hitsNumber = hitsNumber;
        this.missesNumber = missesNumber;
        this.traversedCellsNumber = traversedCellsNumber;
        this.isGameComplete = isGameComplete;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTreasuresCost() {
        return treasuresCost;
    }

    public int getReachedLevelCount() {
        return reachedLevelCount;
    }

    public long getDefeatedEnemiesCount() {
        return defeatedEnemiesCount;
    }

    public int getConsumedFoodAmount() {
        return consumedFoodAmount;
    }

    public int getConsumedElixirsNumber() {
        return consumedElixirsNumber;
    }

    public int getReadScrollsNumber() {
        return readScrollsNumber;
    }

    public int getHitsNumber() {
        return hitsNumber;
    }

    public int getMissesNumber() {
        return missesNumber;
    }

    public int getTraversedCellsNumber() {
        return traversedCellsNumber;
    }

    public boolean isGameComplete() {
        return isGameComplete;
    }

    @Override
    public String toString() {
        return "PlaythroughStatistics{" +
                "playerName='" + playerName + '\'' +
                ", treasuresCost=" + treasuresCost +
                ", reachedLevelCount=" + reachedLevelCount +
                ", defeatedEnemiesCount=" + defeatedEnemiesCount +
                ", consumedFoodAmount=" + consumedFoodAmount +
                ", consumedElixirsNumber=" + consumedElixirsNumber +
                ", readScrollsNumber=" + readScrollsNumber +
                ", hitsNumber=" + hitsNumber +
                ", missesNumber=" + missesNumber +
                ", traversedCellsNumber=" + traversedCellsNumber +
                ", isGameComplete=" + isGameComplete +
                '}';
    }
}
