package com.catinasw.T03.domain.service;

import com.catinasw.T03.datasource.GamesRepository;
import com.catinasw.T03.datasource.UserRepository;
import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.model.GameField;
import com.catinasw.T03.domain.model.User;
import com.catinasw.T03.domain.model.UserWinRatio;
import com.catinasw.T03.domain.util.CellState;
import com.catinasw.T03.domain.util.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {
    private final GamesRepository repository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameServiceImpl(GamesRepository repository, UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
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
                    int currentTurnValue = evaluationMinMax(gameField, GameState.MAX_TURN);
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

    private int evaluationMinMax(int[][] gameField, GameState state) {
        if (state != GameState.MIN_TURN && state != GameState.MAX_TURN) {
            throw new IllegalArgumentException("Invalid state for min max");
        }
        int maxValue = CellState.MAX.getValue();
        int minValue = CellState.MIN.getValue();
        int emptyValue = CellState.EMPTY.getValue();
        int fieldLength = gameField.length;
        GameState prevTurnState = (state == GameState.MAX_TURN) ? GameState.MIN_TURN : GameState.MAX_TURN;

        int turnValue = getTurnValue(gameField, prevTurnState);
        if (turnValue == maxValue ||
                turnValue == minValue ||
                isGameFieldFilled(gameField)) {
            return turnValue;
        }


        if (state == GameState.MIN_TURN) {
            turnValue = Integer.MAX_VALUE;

            for (int i = 0; i < fieldLength; i++) {
                for (int j = 0; j < fieldLength; j++) {
                    if (gameField[i][j] == emptyValue) {
                        gameField[i][j] = minValue;
                        int currentTurnValue = evaluationMinMax(gameField, GameState.MAX_TURN);
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
                        int currentTurnValue = evaluationMinMax(gameField, GameState.MIN_TURN);
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
        GameState oldState = oldGame.getGameState();
        GameState newState = newGame.getGameState();


        if (oldState != newState || isNewGameStateNotValid(oldMatrix, newMatrix, oldState)) {
            throw new IllegalStateException("Game state is not valid");
        }
    }

    private boolean isNewGameStateNotValid(int[][] oldMatrix, int[][] newMatrix, GameState state) {
        int turnValue = (state == GameState.MAX_TURN) ? CellState.MAX.getValue() : CellState.MIN.getValue();
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
                    if (oldValue == emptyValue && newValue == turnValue) {
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
        if (isGameOverState(currentState)) {
            return currentState;
        }

        GameField gameField = currentGame.getGameField();
        int[][] field = gameField.getGameField();

        if (isGameFieldFilled(field)) {
            return GameState.DRAW;
        }

        currentState = checkColumnComplete(field, currentState);
        if (isGameOverState(currentState)) {
            return currentState;
        }

        currentState = checkLineComplete(field, currentState);
        if (isGameOverState(currentState)) {
            return currentState;
        }

        return checkDiagonalComplete(field, currentState);
    }

    private int getTurnValue(int[][] field, GameState prevTurnState) {
        GameState state = checkLineComplete(field, prevTurnState);
        if (state != prevTurnState) {
            return getWinnerValue(state);
        }

        state = checkColumnComplete(field, prevTurnState);
        if (state != prevTurnState) {
            return getWinnerValue(state);
        }

        state = checkDiagonalComplete(field, prevTurnState);
        if (state != prevTurnState) {
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
            default:
                throw new RuntimeException("Unknown game state " + gameState);
        }

        return result.toString();
    }

    @Override
    public void saveGame(CurrentGame repositoryGame) {
        repository.save(repositoryGame);
    }

    @Override
    public CurrentGame createNewGame(UUID creatorId, boolean isMultiplayer) {
        Optional<User> optionalMaxPlayer = userRepository.findById(creatorId);
        if (optionalMaxPlayer.isEmpty()) {
            throw new IllegalArgumentException("User with id: " + creatorId + " not found");
        }
        User maxPlayer = optionalMaxPlayer.get();
        CurrentGame gameToSave;
        if (isMultiplayer) {
            gameToSave = new CurrentGame(new GameField(), GameState.WAITING_FOR_PLAYERS, maxPlayer, null, new Date(), null);
        } else {
            gameToSave = new CurrentGame(new GameField(), GameState.MAX_TURN, maxPlayer, null, new Date(), null);
        }

        return repository.save(gameToSave);
    }

    private GameState checkDiagonalComplete(int[][] field, GameState state) {
        GameState newState = checkMainDiagonalComplete(field, state);
        if (newState != state) {
            return newState;
        }
        return checkSecondaryDiagonalComplete(field, state);
    }

    private GameState checkSecondaryDiagonalComplete(int[][] field, GameState state) {
        int length = field.length;
        int prevCellValue = field[0][length - 1];
        if (prevCellValue == CellState.EMPTY.getValue()) {
            return state;
        }
        for (int i = 1, j = length - 2; i < length; i++, j--) {
            if (prevCellValue != field[i][j]) {
                return state;
            }
        }
        return getWinnerByCellValue(field[0][length - 1]);
    }

    private GameState checkMainDiagonalComplete(int[][] field, GameState state) {
        int length = field.length;
        int prevCellValue = field[0][0];
        if (prevCellValue == CellState.EMPTY.getValue()) {
            return state;
        }
        for (int i = 1, j = 1; i < length; i++, j++) {
            if (prevCellValue != field[i][j]) {
                return state;
            }
        }
        return getWinnerByCellValue(field[0][0]);
    }

    private GameState checkColumnComplete(int[][] field, GameState state) {
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
        return state;
    }

    private GameState checkLineComplete(int[][] field, GameState state) {
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
        return state;
    }

    public CurrentGame getGameById(UUID uuid) {
        Optional<CurrentGame> game = repository.findById(uuid);
        return game.orElseThrow(() -> new IllegalArgumentException("Cant find game with id: " + uuid));
    }

    public List<CurrentGame> getAvailableGames() {
        Optional<List<CurrentGame>> games = repository.findAllByGameState(GameState.WAITING_FOR_PLAYERS);
        return games.orElse(Collections.emptyList());
    }

    private GameState getWinnerByCellValue(int value) {
        if (value == CellState.MAX.getValue()) {
            return GameState.MAX_WIN;
        } else {
            return GameState.MIN_WIN;
        }
    }

    public boolean isGameOverState(GameState state) {
        return state == GameState.DRAW || state == GameState.MIN_WIN || state == GameState.MAX_WIN;
    }

    public User getUserById(UUID uuid) {
        Optional<User> user = userRepository.findById(uuid);
        return user.orElseThrow(() -> new NoSuchElementException("Cant find user with id: " + uuid));
    }

    public List<CurrentGame> getCompletedGamesByUserId(UUID userId) {
        return repository.findAllCompletedGamesByUserId(userId).orElse(Collections.emptyList());
    }

    public UUID getWinnerUUID(CurrentGame game) {
        UUID winnerUUID = null;
        GameState state = game.getGameState();
        if (state == GameState.MAX_WIN) {
            winnerUUID = game.getPlayer1().getUuid();
        } else if (state == GameState.MIN_WIN) {
            if (game.getPlayer2() != null) {
                winnerUUID = game.getPlayer2().getUuid();
            }
        }
        return winnerUUID;
    }

    @Override
    public List<UserWinRatio> getFirstNWinRatio(int recordsAmount) {
        String sql = """
                SELECT
                    user_id,
                    CASE
                        WHEN loss_draw_count = 0 THEN wins
                        ELSE CAST(wins AS FLOAT) / loss_draw_count
                        END AS ratio
                FROM (
                         SELECT
                             u.uuid AS user_id,
                             SUM(CASE WHEN (g.winner_id = u.uuid) THEN 1 ELSE 0 END) AS wins,
                             SUM(CASE
                                     WHEN g.game_state = 'DRAW' THEN 1
                                     WHEN g.game_state IN ('MAX_WIN', 'MIN_WIN') THEN
                                         CASE
                                             WHEN g.winner_id IS NULL THEN 1
                                             WHEN g.winner_id != u.uuid THEN 1
                                             ELSE 0
                                         END
                                     ELSE 0
                                 END) AS loss_draw_count
                         FROM
                             games g
                         LEFT JOIN users u on u.uuid IN (g.player1_id, g.player2_id)
                         WHERE
                             g.game_state IN ('MAX_WIN', 'MIN_WIN', 'DRAW')
                         GROUP BY
                             u.uuid
                     ) AS user_stats
                ORDER BY
                    ratio DESC
                LIMIT ?;""";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new UserWinRatio(UUID.fromString(rs.getString("user_id")),
                                rs.getFloat("ratio")),
                recordsAmount
        );
    }
}
