package com.catinasw.T03.domain.service;

import com.catinasw.T03.datasource.repository.Repository;
import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.model.GameField;
import com.catinasw.T03.domain.util.CellState;
import com.catinasw.T03.domain.util.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
    private final Repository repository;
    private int idCounter = 0;

    @Autowired
    public GameServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public CurrentGame getNextMove(CurrentGame game) {
        GameField field = game.getGameField();
        int[][] gameField = field.getGameField();

        int turnValue = Integer.MAX_VALUE;
        int turnCol = -1;
        int turnRow = -1;
        int minValue = CellState.MIN.getValue();
        int emptyValue = CellState.EMPTY.getValue();
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                if (gameField[i][j] == emptyValue) {
                    gameField[i][j] = minValue;
                    int currentTurnValue = evaluationMinMax(gameField, false);
                    gameField[i][j] = emptyValue;
                    if (currentTurnValue < turnValue) {
                        turnValue = currentTurnValue;
                        turnCol = i;
                        turnRow = j;
                    }
                }
            }
        }
        if (turnCol >= 0) {
            gameField[turnCol][turnRow] = minValue;
            field.setGameField(gameField);
        }

        return game;
    }

    private int evaluationMinMax(int[][] gameField, boolean isMinimizePlayer) {
        int maxValue = CellState.MAX.getValue();
        int minValue = CellState.MIN.getValue();
        int emptyValue = CellState.EMPTY.getValue();
        int fieldLength = gameField.length;

        int turnValue = getTurnValue(gameField);
        if (turnValue == maxValue ||
                turnValue == minValue ||
                isGameFieldFilled(gameField)) {
            return turnValue;
        }


        if (isMinimizePlayer) {
            turnValue = Integer.MAX_VALUE;

            for (int i = 0; i < fieldLength; i++) {
                for (int j = 0; j < fieldLength; j++) {
                    if (gameField[i][j] == emptyValue) {
                        gameField[i][j] = minValue;
                        int currentTurnValue = evaluationMinMax(gameField, false);
                        gameField[i][j] = emptyValue;
                        if (currentTurnValue < turnValue) {
                            turnValue = currentTurnValue;
                        }
                    }
                }
            }
        } else {
            turnValue = Integer.MIN_VALUE;

            for (int i = 0; i < fieldLength; i++) {
                for (int j = 0; j < fieldLength; j++) {
                    if (gameField[i][j] == emptyValue) {
                        gameField[i][j] = maxValue;
                        int currentTurnValue = evaluationMinMax(gameField, true);
                        gameField[i][j] = emptyValue;
                        if (currentTurnValue > turnValue) {
                            turnValue = currentTurnValue;
                        }
                    }
                }
            }
        }

        return turnValue;
    }

    @Override
    public void validateGameField(CurrentGame oldGame, CurrentGame newGame) {
        GameField oldField = oldGame.getGameField();
        GameField newField = newGame.getGameField();
        int[][] newMatrix = newField.getGameField();
        int[][] oldMatrix = oldField.getGameField();

        if (isNewGameStateNotValid(oldMatrix, newMatrix)) {
            throw new IllegalStateException("Game state is not valid");
        }
    }

    private boolean isNewGameStateNotValid(int[][] oldMatrix, int[][] newMatrix) {
        int maxValue = CellState.MAX.getValue();
        int emptyValue = CellState.EMPTY.getValue();
        int madeTurnsCounter = 0;
        int validTurnsNumber = 1;
        int fieldLength = oldMatrix.length;

        for (int i = 0; i < fieldLength; i++) {
            for (int j = 0; j < fieldLength; j++) {
                int oldValue = oldMatrix[i][j];
                int newValue = newMatrix[i][j];
                if (oldValue != newValue) {
                    madeTurnsCounter++;
                    if (oldValue == emptyValue && newValue == maxValue) {
                        continue;
                    }
                    return true;
                }
            }
        }
        return madeTurnsCounter != validTurnsNumber;
    }

    @Override
    public GameState checkGameState(CurrentGame currentGame) {
        GameState currentState = currentGame.getGameState();
        GameState executingState = GameState.EXECUTING;
        if (currentState != executingState) {
            return currentState;
        }

        GameField gameField = currentGame.getGameField();
        int[][] field = gameField.getGameField();

        if (isGameFieldFilled(field)) {
            return GameState.DRAW;
        }

        currentState = checkColumnComplete(field);
        if (currentState != executingState) {
            return currentState;
        }

        currentState = checkLineComplete(field);
        if (currentState != executingState) {
            return currentState;
        }

        return checkDiagonalComplete(field);
    }

    private int getTurnValue(int[][] field) {
        GameState executing = GameState.EXECUTING;
        GameState state = checkLineComplete(field);
        if (state != executing) {
            return getWinnerValue(state);
        }

        state = checkColumnComplete(field);
        if (state != executing) {
            return getWinnerValue(state);
        }

        state = checkDiagonalComplete(field);
        if (state != executing) {
            return getWinnerValue(state);
        }

        return CellState.EMPTY.getValue();
    }

    private int getWinnerValue(GameState state) {
        if (state == GameState.MAX_WIN) {
            return CellState.MAX.getValue();
        } else {
            return CellState.MIN.getValue();
        }
    }

    private boolean isGameFieldFilled(int[][] field) {
        int length = field.length;
        for (int[] rows : field) {
            for (int j = 0; j < length; j++) {
                if (rows[j] == CellState.EMPTY.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public String getGameEndResultMessage(GameState gameState) {
        StringBuilder result = new StringBuilder();
        result.append("Result: ");
        switch (gameState) {
            case DRAW:
                result.append("draw!");
                break;
            case MAX_WIN:
                result.append(" player wins!");
                break;
            case MIN_WIN:
                result.append(" AI wins!");
                break;
            case EXECUTING:
                result.append(" game in progress...");
                break;
            default:
                throw new RuntimeException("Unknown game state " + gameState);
        }

        return result.toString();
    }

    @Override
    public void saveGame(CurrentGame currentGame) {
        repository.updateGame(currentGame);
    }

    @Override
    public int createNewGame() {
        repository.createGame(new CurrentGame(++idCounter, new GameField(), GameState.EXECUTING));
        return idCounter;
    }

    private GameState checkDiagonalComplete(int[][] field) {
        GameState mainValue = checkMainDiagonalComplete(field);
        if (mainValue != GameState.EXECUTING) {
            return mainValue;
        }
        return checkSecondaryDiagonalComplete(field);
    }

    private GameState checkSecondaryDiagonalComplete(int[][] field) {
        GameState result = GameState.EXECUTING;
        int length = field.length;
        int prevCellValue = field[0][length - 1];
        if (prevCellValue == CellState.EMPTY.getValue()) {
            return result;
        }
        for (int i = 1, j = length - 2; i < length; i++, j--) {
            if (prevCellValue != field[i][j]) {
                return result;
            }
        }
        return getWinnerByCellValue(field[0][length - 1]);
    }

    private GameState checkMainDiagonalComplete(int[][] field) {
        GameState result = GameState.EXECUTING;
        int length = field.length;
        int prevCellValue = field[0][0];
        if (prevCellValue == CellState.EMPTY.getValue()) {
            return result;
        }
        for (int i = 1, j = 1; i < length; i++, j++) {
            if (prevCellValue != field[i][j]) {
                return result;
            }
        }
        return getWinnerByCellValue(field[0][0]);
    }

    private GameState checkColumnComplete(int[][] field) {
        int length = field.length;

        for (int j = 0; j < length; j++) {
            int firstValue = field[0][j];
            if (firstValue == CellState.EMPTY.getValue()) {
                continue;
            }
            boolean isColumnComplete = true;
            for (int[] rows : field) {
                if (rows[j] != firstValue) {
                    isColumnComplete = false;
                    break;
                }
            }
            if (isColumnComplete) {
                return getWinnerByCellValue(firstValue);
            }
        }
        return GameState.EXECUTING;
    }

    private GameState checkLineComplete(int[][] field) {
        for (int[] rows : field) {
            int firstValue = rows[0];
            if (firstValue == CellState.EMPTY.getValue()) {
                continue;
            }
            boolean isLineComplete = true;
            for (int cellValue : rows) {
                if (cellValue != firstValue) {
                    isLineComplete = false;
                    break;
                }
            }
            if (isLineComplete) {
                return getWinnerByCellValue(firstValue);
            }
        }
        return GameState.EXECUTING;
    }

    public CurrentGame getGameById(int UUID) {
        return repository.getGameById(UUID);
    }

    private GameState getWinnerByCellValue(int value) {
        if (value == CellState.MAX.getValue()) {
            return GameState.MAX_WIN;
        } else {
            return GameState.MIN_WIN;
        }
    }

}
