package com.catinasw.T03.datasource.mapper;

import com.catinasw.T03.datasource.model.DSCurrentGame;
import com.catinasw.T03.datasource.model.DSGameField;
import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.model.GameField;

public class DomainDataSourceMapper {
    public DSCurrentGame toDataSource(CurrentGame domain) {
        DSGameField dsGameField = new DSGameField();
        dsGameField.setGameField(domain.getGameField().getGameField());

        return new DSCurrentGame(
                domain.getUUID(),
                dsGameField,
                domain.getGameState()
        );
    }

    public CurrentGame toDomain(DSCurrentGame dataSource) {
        GameField gameField = new GameField();
        gameField.setGameField(dataSource.getGameField().getGameField());

        return new CurrentGame(
                dataSource.getUUID(),
                gameField,
                dataSource.getGameState()
        );
    }
}
