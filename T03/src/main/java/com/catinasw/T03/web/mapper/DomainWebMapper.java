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
                domain.getUuid(),
                dsGameField,
                domain.getGameState(),
                domain.getPlayer1(),
                domain.getPlayer2(),
                domain.getCreatedAt(),
                domain.getWinnerId()
        );
    }

    public CurrentGame toDomain(WebCurrentGame webGame) {
        GameField gameField = new GameField();
        gameField.setGameField(webGame.getGameField().getGameField());

        return new CurrentGame(
                gameField,
                webGame.getGameState(),
                webGame.getPlayer1(),
                webGame.getPlayer2(),
                webGame.getCreatedAt(),
                webGame.getWinnerId()
        );
    }
}
