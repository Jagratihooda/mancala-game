package com.bol.mancala.assignment.controller;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.service.BoardService;
import com.bol.mancala.assignment.service.GameService;
import com.bol.mancala.assignment.service.PitService;
import com.bol.mancala.assignment.service.PlayerService;
import com.kalah.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private PlayerService playerService;
    private BoardService boardService;
    private PitService pitService;
    private GameService gameService;

    private final Logger LOGGER = LoggerFactory.getLogger(GameController.class);


    @Autowired
    public GameController(GameService gameService, BoardService boardService, PitService pitService, PlayerService playerService
                          ) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.boardService = boardService;
        this.pitService = pitService;
    }

    /**
     * This Method is used to create a new game
     * game
     * @return game
     */
    @PostMapping(value = "/create")
    public Game prepareNewGame() {
        LOGGER.info("Preparing new game");

        Game game = gameService.prepareNewGame(playerService.getPlayerDetails());
        Board board = boardService.prepareNewBoard(game);
        pitService.preparePit(board);

        LOGGER.error("New game is created " + game.getId());
        return game;
    }

    /**
     * This endpoint will be called to list games having just one player
     * game
     * @return List of games
     */
    @GetMapping(value = "/list")
    public List<Game> getGamesWaitingForSecondPlayer() {
        LOGGER.info("Loading games to Join");

        return gameService.fetchGamesToJoinForAPlayer(playerService.fetchLoggedInUser());
    }

    /**
     * This endpoint will be called to load a list of games belong to a player
     * game
     * @return List of games
     */
    @GetMapping(value = "/player/list")
    public List<Game> loadGamesForTheLoggedInPlayer() {
        LOGGER.info("Loading player's game");

        return gameService.fetchPlayerGames(playerService.fetchLoggedInUser());
    }


    @GetMapping(value = "/{id}")
    public Game loadGame(@PathVariable Long id) {
        LOGGER.info("Loading Game");

        return gameService.findGameById(id);
    }

    /**
     * This endpoint will be called when a player wants to join a game
     * game
     * @return game
     */
    @PostMapping(value = "/join")
    public Game joinAnExistingGame(@RequestParam(name = "id") Long id) {
        LOGGER.info("Joining an existing game");

        Player player = playerService.fetchLoggedInUser();
        Game game = gameService.joinAnExistingGame(player, id);

        LOGGER.info("Player joined an existing game with id" + id);
        return game;
    }


}