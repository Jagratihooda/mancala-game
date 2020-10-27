package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;


/*
Service for Board related actions
*/
public interface BoardService {

    /**
     * This method is called to prepre a new board.
     * game
     *
     * @param game
     * @return board
     */
    Board prepareNewBoard(Game game);

    /**
     * This method is called to fetch a Board for a given game
     * game
     *
     * @param game
     * @return board
     */
    Board fetchBoardByGame(Game game);

    }
