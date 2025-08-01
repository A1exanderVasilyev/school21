package org.example.datalayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.Coords;
import org.example.domain.GameSession;
import org.example.domain.characters.Player;
import org.example.domain.generation.LevelFactory;
import org.example.domain.map.Cell;
import org.example.domain.map.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class    DataManager {
    private static final String SAVE_DIR = "data/";
    private static final String SESSION_FILE = "save.json";
    private static final String RECORDS_FILE = "records.json";
    private static final ObjectMapper MAPPER = GameStateSerializer.getMapper();

    public static boolean saveFileExists() {
        return Files.exists(Paths.get(SAVE_DIR, SESSION_FILE));
    }

    public static void saveGame(GameSession session) {
        try {
            File saveDir = new File(SAVE_DIR);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            SavedGame savedGame = new SavedGame(
                    session.getPlayer(),
                    session.getPlayer().getPosition(),
                    session.getBackpack(),
                    session.getCurrentLevel(),
                    session.getLevelFactory().getGeneratedLevelsCount()
            );
            MAPPER.writeValue(Paths.get(SAVE_DIR, SESSION_FILE).toFile(), savedGame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static GameSession loadGame() {
        File saveFile = Paths.get(SAVE_DIR, SESSION_FILE).toFile();
        if (!saveFile.exists()) {
            return null;
        }

        try {
            SavedGame savedGame = MAPPER.readValue(saveFile, SavedGame.class);
            Player loadedPlayer = savedGame.getPlayer();
            Coords playerPosition = savedGame.getPlayerPosition();
            if (loadedPlayer != null && playerPosition != null) {
                loadedPlayer.setPosition(playerPosition); // Принудительно устанавливаем позицию
            }
            LevelFactory levelFactory = new LevelFactory(9, 10);
            levelFactory.setGeneratedLevelsCount(savedGame.getGeneratedLevelsCount());

            GameSession session = new GameSession(
                    savedGame.getPlayer(),
                    savedGame.getBackpack(),
                    savedGame.getCurrentLevel(),
                    levelFactory,
                    false
            );
            Level currentLevel = session.getCurrentLevel();
            if (currentLevel != null) {
                currentLevel.initializeField();
                currentLevel.fullField();
            }
            Coords playerCoords = session.getPlayer().getPosition();
            if (playerCoords != null && currentLevel != null) {
                Cell[][] field = currentLevel.getField();
                // Добавим проверку границ на всякий случай
                if (playerCoords.y >= 0 && playerCoords.y < field.length &&
                        playerCoords.x >= 0 && playerCoords.x < field[0].length) {
                    field[playerCoords.y][playerCoords.x].setCharacter(session.getPlayer());
                }
            }
            return session;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addRecord(PlaythroughStatistics stats) {
        try {
            GameRecords records = loadRecords();
            records.addPlaythroughStatistics(stats);
            saveRecords(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameRecords loadRecords() throws IOException {
        File recordsFile = Paths.get(SAVE_DIR, RECORDS_FILE).toFile();
        if (recordsFile.exists()) {
            return MAPPER.readValue(recordsFile, GameRecords.class);
        }
        return new GameRecords();
    }

    private static void saveRecords(GameRecords records) throws IOException {
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        records.sortRecords();
        MAPPER.writeValue(Paths.get(SAVE_DIR, RECORDS_FILE).toFile(), records);
    }

    public static List<PlaythroughStatistics> getLeaderboard() {
        try {
            return loadRecords().getTopStatistics();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
