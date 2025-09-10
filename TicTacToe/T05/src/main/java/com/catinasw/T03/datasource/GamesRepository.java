package com.catinasw.T03.datasource;

import com.catinasw.T03.domain.model.CurrentGame;
import com.catinasw.T03.domain.util.GameState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GamesRepository extends CrudRepository<CurrentGame, UUID> {
    Optional<List<CurrentGame>> findAllByGameState(GameState gameState);

    @Query(value = "SELECT g FROM CurrentGame g WHERE " +
            "(g.gameState = 'MAX_WIN' OR g.gameState = 'MIN_WIN' OR g.gameState = 'DRAW') " +
            "AND (g.player1.uuid = :userId OR g.player2.uuid = :userId)")
    Optional<List<CurrentGame>> findAllCompletedGamesByUserId(@Param("userId") UUID userId);
}
