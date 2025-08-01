package com.catinasw.T03.datasource.repository;

import com.catinasw.T03.datasource.mapper.DomainDataSourceMapper;
import com.catinasw.T03.datasource.model.DSCurrentGame;
import com.catinasw.T03.datasource.model.Storage;
import com.catinasw.T03.domain.model.CurrentGame;

import java.util.Optional;

public class Repository {
    private final Storage storage;
    private final DomainDataSourceMapper mapper;

    public Repository(Storage storage, DomainDataSourceMapper mapper) {
        this.storage = storage;
        this.mapper = mapper;
    }

    public void createGame(CurrentGame currentGame) {
        storage.addGame(mapper.toDataSource(currentGame));
    }

    public void updateGame(CurrentGame currentGame) {
        DSCurrentGame mappedGame = mapper.toDataSource(currentGame);
        Optional<DSCurrentGame> storageGame = storage.getGame(currentGame.getUUID());
        if (storageGame.isPresent()) {
            DSCurrentGame dsGame = storageGame.get();
            dsGame.setGameField(mappedGame.getGameField());
            dsGame.setGameState(mappedGame.getGameState());
        } else {
            throw new IllegalArgumentException("Cant find game with id: " + currentGame.getUUID());
        }
    }

    public CurrentGame getGameById(int id) {
        Optional<DSCurrentGame> dsCurrentGame = storage.getGame(id);

        if (dsCurrentGame.isEmpty()) {
            throw new IllegalArgumentException("Cant find game with id: " + id);
        }

        return mapper.toDomain(dsCurrentGame.get());
    }
}
