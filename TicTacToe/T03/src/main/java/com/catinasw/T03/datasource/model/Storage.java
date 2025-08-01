package com.catinasw.T03.datasource.model;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {
    private List<DSCurrentGame> games;

    public Storage() {
        games = new CopyOnWriteArrayList<>();
    }

    public List<DSCurrentGame> getGames() {
        return games;
    }

    public void addGame(DSCurrentGame game) {
        games.add(game);
    }

    public Optional<DSCurrentGame> getGame(int UUID) {
        return games.stream()
                .filter(game -> game.getUUID() == UUID)
                .findFirst();
    }
}
