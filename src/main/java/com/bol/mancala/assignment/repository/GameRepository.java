package com.bol.mancala.assignment.repository;

import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.enums.GameState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> findByGameState(GameState gameState);
}
