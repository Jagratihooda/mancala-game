package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Pit;

/**
 * Service class for Pit related actions
 */
public interface PitService {

    /**
     * This method is called to prepare a new Pit
     *
     * @param board
     */
    void preparePit(Board board);

    /**
     * This method is called to fetch a Pit based on board and position
     *
     * @param board
     * @param position
     * @return pit
     */
    Pit fetchPitByBoardAndPosition(Board board, int position);

    /**
     * This method is called clear stones for a given board and pit position
     *
     * @param board
     * @param position
     * @return board
     */
    Pit clearStones(Board board, int position);

    /**
     * This method is called to update stone count for a given board and pit position
     *
     * @param board
     * @param position
     * @param amount
     * @return pit
     */
    Pit updateStonesByAmount(Board board, int position, int amount);

}
