package com.bol.mancala.assignment.controller;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.service.BoardService;
import com.bol.mancala.assignment.service.GameService;
import com.bol.mancala.assignment.service.PitService;
import com.bol.mancala.assignment.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to handle Lobby related calls
 */
@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final PlayerService playerService;
    private final BoardService boardService;
    private final PitService pitService;
    private final GameService gameService;

    private final Logger LOGGER = LoggerFactory.getLogger(LobbyController.class);

    @Autowired
    public LobbyController(GameService gameService, BoardService boardService, PitService pitService, PlayerService playerService
    ) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.boardService = boardService;
        this.pitService = pitService;
    }

    /**
     * This End Point is to create a new game
     * game
     *
     * @return game
     */
    @PostMapping(value = "/game/prepare")
    public Game prepareNewGame() {
        LOGGER.info("Preparing new game");

        Game game = gameService.prepareNewGame(playerService.fetchLoggedInUser());
        Board board = boardService.prepareNewBoard(game);
        pitService.preparePit(board);

        LOGGER.error(String.format("New game is created %s", game.getId()));
        return game;
    }

    /**
     * This endpoint will be called to list games having just one player
     * game
     *
     * @return List of games
     */
    @GetMapping(value = "/to-be-joined/game/list")
    public List<Game> getGamesWaitingForSecondPlayer() {
        LOGGER.info("Loading games to Join");

        return gameService.fetchGamesToJoinForAPlayer(playerService.fetchLoggedInUser());
    }

    /**
     * This endpoint will be called to load a list of games belong to a player
     * game
     *
     * @return List of games
     */
    @GetMapping(value = "/player/own-games/list")
    public List<Game> loadGamesForTheLoggedInPlayer() {
        LOGGER.info("Loading player's games");

        return gameService.fetchPlayerGames(playerService.fetchLoggedInUser());
    }

    /**
     * This endpoint will be called when a player wants to join a game
     * game
     *
     * @return game
     */
    @PostMapping(value = "/join/game")
    public Game joinAnExistingGame(@RequestParam(name = "id") Long id) {
        LOGGER.info("Joining an existing game");

        Player player = playerService.fetchLoggedInUser();
        Game game = gameService.joinAnExistingGame(player, id);

        LOGGER.info(String.format("Player joined an existing game with id %s", id));
        return game;
    }


}