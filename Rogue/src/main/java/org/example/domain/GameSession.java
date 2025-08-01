package org.example.domain;

import org.example.datalayer.GameRecords;
import org.example.datalayer.PlaythroughStatistics;
import org.example.datalayer.DataManager;
import org.example.domain.characters.Character;
import org.example.domain.characters.DamageResult;
import org.example.domain.characters.Enemy;
import org.example.domain.characters.Player;
import org.example.domain.enums.Direction;
import org.example.domain.generation.LevelFactory;
import org.example.domain.items.Item;
import org.example.domain.map.Cell;
import org.example.domain.map.Level;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class GameSession {
    private Player player;
    private Backpack backpack;
    private Level currentLevel;
    @JsonIgnore
    private LevelFactory levelFactory;
    private boolean isGameOver;
    private GameRecords gameRecords;
    private int playerStepsCount;
    private long deadEnemiesCount;
    private final int MAX_LEVEL = 21;
    private String takeItemMessage;
    private String usedItemMessage;
    private List<DamageResult> gameTurnAttackResults;

    public GameRecords getGameRecords() {
        return gameRecords;
    }

    public long getDeadEnemiesCount() {
        return deadEnemiesCount;
    }

    public int getPlayerStepsCount() {
        return playerStepsCount;
    }


    public GameSession(Player player, Backpack backpack, Level currentLevel, LevelFactory levelFactory, boolean isGameOver) {
        this.player = player;
        this.backpack = backpack;
        this.currentLevel = currentLevel;
        this.levelFactory = levelFactory;
        this.isGameOver = isGameOver;
        this.gameRecords = new GameRecords();
        this.playerStepsCount = 0;
        this.deadEnemiesCount = 0;
        this.takeItemMessage = null;
        this.usedItemMessage = null;
        this.gameTurnAttackResults = new ArrayList<>();
    }

    public String getUsedItemMessage() {
        return usedItemMessage;
    }

    public void setUsedItemMessage(String usedItemMessage) {
        this.usedItemMessage = usedItemMessage;
    }

    public String getTakeItemMessage() {
        return takeItemMessage;
    }

    public List<DamageResult> getGameTurnAttackResults() {
        return gameTurnAttackResults;
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

    public LevelFactory getLevelFactory() {
        return levelFactory;
    }

    public void setLevelFactory(LevelFactory levelFactory) {
        this.levelFactory = levelFactory;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public String[] getAttackMessages() {
        if (gameTurnAttackResults != null) {
            String[] messages = new String[gameTurnAttackResults.size()];
            int i = 0;
            for (DamageResult dr : gameTurnAttackResults) {
                messages[i] = dr.getMessage();
                ++i;
            }
            return messages;
        } else {
            return null;
        }

    }

    public void startNewGame() {
        backpack.clearBackpack();
        playerStepsCount = 0;
        deadEnemiesCount = 0;
        setupLevel();
    }

    public void setupLevel() {
        currentLevel = levelFactory.generateLevel();
        currentLevel.fullField();
        Coords playerCoords = currentLevel.getStartCoords();
        player.setPosition(playerCoords);
        currentLevel.getField()[playerCoords.y][playerCoords.x].setCharacter(player);
    }

    public void setupLevelLoad() {
        currentLevel = levelFactory.generateLevel();
        currentLevel.fullField();
        Coords playerCoords = currentLevel.getStartCoords();
        player.setPosition(playerCoords);
        currentLevel.getField()[playerCoords.y][playerCoords.x].setCharacter(player);
    }

    void takeItemHandler() {
        Cell playerCell = currentLevel.getField()[player.getPosition().y][player.getPosition().x];
        Item itemToTake = playerCell.getItem();

        if (itemToTake == null) {
            takeItemMessage = null;
            return;
        }

        if (backpack.add(itemToTake)) {
            playerCell.setItem(null);
            currentLevel.getItems().remove(itemToTake);
            takeItemMessage = "The " + itemToTake.getType() + " - " + itemToTake.getSubType() + " was taken!";
        }
    }

    public void gameTurnHandler(Direction playerMoveDirection) {
        gameTurnAttackResults.clear();
        playerTurnHandler(playerMoveDirection);
        if (isGameEnd()) {
            endGameHandler();
        }
        if (isExitReached()) {
            if (isGameEnd()) {
                endGameHandler();
            } else {
                setupLevel();
            }
        } else {
            enemiesTurnHandler();
        }
        if (isGameEnd()) {
            endGameHandler();
        }
    }

    private void playerTurnHandler(Direction playerMoveDirection) {
        Cell[][] field = currentLevel.getField();
        if (playerMoveDirection != Direction.NONE && !player.move(playerMoveDirection, currentLevel, true)) {
            Coords playerPosition = player.getPosition();
            int moveY = playerPosition.y + playerMoveDirection.getDY();
            int moveX = playerPosition.x + playerMoveDirection.getDX();
            if (moveX > 0 && moveY > 0 && moveX < field.length && moveY < field[0].length) {
                Character character = field[moveY][moveX].getCharacter();
                if (character != null) {
                    gameTurnAttackResults.add(player.attack(character));
                }
            }
        } else {
            playerStepsCount++;
        }
        takeItemHandler();
        player.updateElixirs();
    }

    private void enemiesTurnHandler() {
        if (currentLevel.getEnemies() == null) {
            return;
        }
        deadEnemiesCount += currentLevel.handleDeadEnemiesAndGetTheirNumber(backpack);
        for (Enemy enemy : currentLevel.getEnemies()) {
            gameTurnAttackResults.add(enemy.makeTurn(currentLevel, player));
        }
    }

    private boolean isExitReached() {
        return player.getPosition().equals(currentLevel.getExitCoords());
    }

    private boolean isGameEnd() {
        if (!player.isAlive()) {
            return true;
        }
        return currentLevel.getLevelNumber() == MAX_LEVEL;
    }

    public void endGameHandler() {
        PlaythroughStatistics stats = new PlaythroughStatistics(
                player.getName(),
                backpack.getTreasuresCost(),
                currentLevel.getLevelNumber(),
                deadEnemiesCount,
                player.getConsumedFoodAmount(),
                player.getConsumedElixirsNumber(),
                player.getReadScrollsNumber(),
                player.getHitsNumber(),
                player.getMissesNumber(),
                playerStepsCount,
                player.isAlive()
        );
        DataManager.addRecord(stats);
        isGameOver = true;
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "player=" + player +
                ", backpack=" + backpack +
                ", currentLevel=" + currentLevel +
                ", levelFactory=" + levelFactory +
                ", isGameOver=" + isGameOver +
                ", gameRecords=" + gameRecords +
                '}';
    }
}
