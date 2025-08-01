package com.catinasw.T03.web.mapper;

import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.model.GameField;
import com.catinasw.T03.web.model.WebCurrentGame;
import com.catinasw.T03.web.model.WebGameField;

public class DomainWebMapper {
    public WebCurrentGame toWeb(CurrentGame domain) {
        WebGameField dsGameField = new WebGameField();
        dsGameField.setGameField(domain.getGameField().getGameField());

        return new WebCurrentGame(
                domain.getUUID(),
                dsGameField,
                domain.getGameState()
        );
    }

    public CurrentGame toDomain(WebCurrentGame webGame) {
        GameField gameField = new GameField();
        gameField.setGameField(webGame.getGameField().getGameField());

        return new CurrentGame(
                webGame.getUUID(),
                gameField,
                webGame.getGameState()
        );
    }
}
