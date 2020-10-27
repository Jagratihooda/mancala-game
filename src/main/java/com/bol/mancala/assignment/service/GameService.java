package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;

import java.util.List;

public interface GameService {
    /**
     * This method is to prepare a new Game for a player
     * game
     *
     * @param player
     * @return game
     */
    Game prepareNewGame(Player player);

    /**
     * This method is called when a player wants to join an existing game
     * game
     *
     * @param player
     * @param gameId
     * @return game
     */

    Game joinAnExistingGame(Player player, Long gameId);


    /**
     * This method is called to update current state of a game
     * game
     *
     * @param game
     * @param gameState
     * @return game
     */

    Game updateGameState(Game game, GameState gameState);

    /**
     * This method is called to switch player's turn
     * game
     *
     * @param player
     * @param game
     * @return game
     */

    Game changePlayerTurn(Player player, Game game);


    /**
     * This method is called to fetch all the games that a specific player can join
     *
     * @param player
     * @return list of games
     */
    List<Game> fetchGamesToJoinForAPlayer(Player player);

    /**
     * This method is called to fetch all the games belong to a player
     *
     * @param player
     * @return list of games
     */
    List<Game> fetchPlayerGames(Player player);


    /**
     * This method is called to fetch game per id
     *
     * @param id
     * @return game
     */
    Game findGameById(Long id);

}
