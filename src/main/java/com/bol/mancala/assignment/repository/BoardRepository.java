package com.bol.mancala.assignment.repository;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for @{@link Board}
 */
@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {

    public Board findByGame(Game game);
}