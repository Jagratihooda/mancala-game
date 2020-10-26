package com.bol.mancala.assignment.repository;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Pit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PitRepository extends CrudRepository<Pit, Long> {


    Pit findByBoardAndPosition(Board board, int position);
}