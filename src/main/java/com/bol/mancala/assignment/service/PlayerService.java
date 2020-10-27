package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Player;

/**
 * Service for Player related actions
 */
public interface PlayerService {

    /**
     * This method is called to fetch Players details based on username
     *
     * @param player
     */
    Player getPlayerByUsername(String name);

    /**
     * This method is called to fetch logged in Players details
     *
     * @param board
     */
    Player fetchLoggedInUser();

}
