package org.example.datalayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameRecords {
    private List<PlaythroughStatistics> gameRecords;

    public GameRecords() {
        gameRecords = new ArrayList<>();
    }

    public void addPlaythroughStatistics(PlaythroughStatistics playthroughStatistics) {
        if (gameRecords == null) {
            gameRecords = new ArrayList<>();
        }
        gameRecords.add(playthroughStatistics);
    }

    public void sortRecords() {
        if (gameRecords != null) {
            gameRecords.sort(Comparator.comparingInt(PlaythroughStatistics::getTreasuresCost).reversed());
        }
    }

    public List<PlaythroughStatistics> getTopStatistics() {
        final int outputRecordsNumber = 10;
        sortRecords();
        return gameRecords.stream().limit(outputRecordsNumber).collect(Collectors.toList());
    }
    public List<PlaythroughStatistics> getGameRecords() {
        return gameRecords;
    }

    public void setGameRecords(List<PlaythroughStatistics> gameRecords) {
        this.gameRecords = gameRecords;
    }

    @Override
    public String toString() {
        return "GameRecords{" +
                "gameRecords=" + gameRecords +
                '}';
    }
}
