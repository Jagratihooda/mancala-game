package com.bol.mancala.assignment.repository;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Pit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for @{@link Pit}
 */
@Repository
public interface PitRepository extends CrudRepository<Pit, Long> {

    List<Pit> findByBoardOrderByPositionAsc(Board Board);


    Pit findByBoardAndPosition(Board board, int position);
}