package com.catinasw.T03.domain.model;

import com.google.gson.Gson;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GameFieldConverter implements AttributeConverter<GameField, String> {
    private static final Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(GameField gameField) {
        if (gameField == null) {
            return null;
        }
        int[][] field = gameField.getGameField();

        if (field == null) {
            return null;
        }

        try {
            return GSON.toJson(field);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert GameField to JSON");
        }
    }

    @Override
    public GameField convertToEntityAttribute(String s) {
        GameField field = new GameField();
        try {
            field.setGameField(GSON.fromJson(s, int[][].class));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert JSON to GameField");
        }

        return field;
    }
}
