package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;

/**
 * Service Class to handle game related actions
 */
public interface PlayService {


    /**
     * This method is  called for making move in a game
     *
     * @param player
     * @param game
     * @param position
     * @return board
     */
    Board makeMove(Player player, Game game, int position);
}
