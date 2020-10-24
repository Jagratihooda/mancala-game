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
import com.kalah.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Class to handle gameplay related REST calls
 */
@RestController
@RequestMapping("/play")
public class PlayController {

    private GameService gameService;
    private PlayerService playerService;
    private BoardService boardService;
    private PlayService playService;
    private HttpSession httpSession;
    private SimpMessagingTemplate template;

    private final Logger LOGGER = LoggerFactory.getLogger(PlayController.class);


    @Autowired
    public PlayController(GameService gameService, PlayerService playerService,
                          BoardService boardService, PlayService playService,
                          HttpSession httpSession, SimpMessagingTemplate template) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.boardService = boardService;
        this.playService = playService;
        this.httpSession = httpSession;
        this.template = template;
    }

    @PostMapping(value = "/move/{position}")
    public Board makeMove(@PathVariable int position) {
        LOGGER.info("Making a move for Player");

        Long gameId = (Long) httpSession.getAttribute(MancalaConstants.GAME_SESSION_ID);

        Game game = gameService.findGameById((Long) httpSession.getAttribute(MancalaConstants.GAME_SESSION_ID));

        Board board = playService.makeMove(playerService.fetchLoggedInUser(), game, position);

        template.convertAndSend("/send-update/position/" + gameId.toString(), "moved");

        // Notify lobby if game is finished (to remove from playable games
        if(game.getGameState() == GameState.FINISHED) {
            template.convertAndSend("/send-update/lobby", "update");
        }

        return board;
    }

    @RequestMapping(value = "/turn")
    public Player getPlayerTurn() {
        LOGGER.info("Getting player turn");
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.findGameById(gameId);
        return game.getPlayerInAction();
    }

    @RequestMapping(value = "/score")
    public Integer getScore() {
        LOGGER.info("Getting player score");

        Player player = playerService.fetchLoggedInUser();
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.findGameById(gameId);

        return playService.getScore(game, player);
    }


    @RequestMapping(value = "/state")
    public GameState getState() {
        LOGGER.info("Getting game state");
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.findGameById(gameId);
        return game.getGameState();
    }


    @GetMapping(value = "/board")
    public Board getBoard() {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.findGameById(gameId);
        Board board = boardService.getBoardByGame(game);
        return board;
    }
}
