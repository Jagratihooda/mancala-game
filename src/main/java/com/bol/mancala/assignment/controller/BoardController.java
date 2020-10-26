package com.bol.mancala.assignment.controller;

import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.service.BoardService;
import com.bol.mancala.assignment.service.GameService;
import com.bol.mancala.assignment.service.PlayService;
import com.bol.mancala.assignment.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Controller to handle game related calls
 */
@RestController
@RequestMapping("/board")
public class BoardController {

    private GameService gameService;
    private PlayerService playerService;
    private BoardService boardService;
    private PlayService playService;
    private HttpSession httpSession;
    private SimpMessagingTemplate template;

    private final Logger LOG = LoggerFactory.getLogger(BoardController.class);


    @Autowired
    public BoardController(GameService gameService, PlayerService playerService,
                          BoardService boardService, PlayService playService,
                          HttpSession httpSession, SimpMessagingTemplate template) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.boardService = boardService;
        this.playService = playService;
        this.httpSession = httpSession;
        this.template = template;
    }

    /**
     * This endpoint will be called to fetch a board
     *
     * @return board
     */
    @GetMapping(value = "/current-board")
    public Board fetchBoard() {
        LOG.info("Fetching current board");

        Long gameId = (Long) httpSession.getAttribute(MancalaConstants.GAME_SESSION_ID);

        Game game = gameService.findGameById(gameId);
        Board board = boardService.fetchBoardByGame(game);

        LOG.info(String.format("Current Board is fetched with board id %s ", board.getId()));
        return board;
    }

    /**
     * This endpoint will be called while making a move by the player
     * game
     *
     * @return board
     */

    @PostMapping(value = "/make-move")
    public Board makeMove(@RequestParam(name = "position") int position) {
        LOG.info("Making a move for Player");

        Long gameId = (Long) httpSession.getAttribute(MancalaConstants.GAME_SESSION_ID);

        Game game = gameService.findGameById((Long) httpSession.getAttribute(MancalaConstants.GAME_SESSION_ID));

        Board board = playService.makeMove(playerService.fetchLoggedInUser(), game, position);

        template.convertAndSend("/send-update/position/" + gameId.toString(), "madeMove");

        if (game.getGameState() == GameState.GAME_FINISHED) {
            template.convertAndSend("/send-update/lobby", "gameFinished");
        }
        return board;
    }

    /**
     * This endpoint will be called to fetch player in Action
     * game
     *
     * @return PlayerInAction
     */

    @RequestMapping(value = "/player-in-action")
    public Player fetchPlayerInAction() {
        LOG.info("Fetch player in action");

        Long gameId = (Long) httpSession.getAttribute(MancalaConstants.GAME_SESSION_ID);
        Game game = gameService.findGameById(gameId);

        Player playerInAction = game.getPlayerInAction();

        LOG.info(String.format("Fetched player in action with id %s ", playerInAction.getId()));

        return playerInAction;
    }

    /**
     * This endpoint will be called to fetch current state of a game
     * game
     *
     * @return gameState
     */

    @RequestMapping(value = "/current-state")
    public GameState fetchState() {
        LOG.info("Fetching game's current state");

        Long gameId = (Long) httpSession.getAttribute(MancalaConstants.GAME_SESSION_ID);
        Game game = gameService.findGameById(gameId);

        GameState gameState = game.getGameState();

        LOG.info(String.format("Fetched game's current state %s ", gameState));

        return gameState;
    }


    @GetMapping(value = "/{id}")
    public Game loadGame(@PathVariable Long id) {
        LOG.info("Loading Game");

        return gameService.findGameById(id);
    }

}
