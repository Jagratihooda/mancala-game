package com.bol.mancala.assignment;

import com.bol.mancala.assignment.service.BoardService;
import com.bol.mancala.assignment.service.GameService;
import com.bol.mancala.assignment.service.PlayService;
import com.bol.mancala.assignment.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bol.mancala.assignment.controller.PlayController;
import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Pit;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.enums.PitType;
import com.bol.mancala.assignment.repository.PlayerRepository;
import com.kalah.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayController.class)
public class PlayControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PlayService playService;

    @MockBean
    private SimpMessagingTemplate templateMock;

    @MockBean
    private PlayerRepository playerRepository; // Needed for securitycontext

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testMovePosition() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, GameState.WAIT_FOR_PLAYER);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        Board board = new Board(gameOne);
        board.setId(1); // Does not matter here

        List<Pit> pits = new ArrayList<>();

        for(int i = 1; i <= 2; i++) {
            Pit p = new Pit(board, (i*7), 0, PitType.STORE);
            pits.add(p);
        }

        for(int i = 1; i <= 6; i++) {
            Pit p = new Pit(board, i, 6, PitType.HOUSE);
            pits.add(p);
        }

        for(int i = 8; i <= 13; i++) {
            Pit p = new Pit(board, i, 6, PitType.HOUSE);
            pits.add(p);
        }

        board.setPits(pits);

        // Rules
        when(playerService.fetchLoggedInUser()).thenReturn(player);
        when(gameService.findGameById(any(Long.class))).thenReturn(gameOne);
        when(playService.makeMove(player, gameOne, 2)).thenReturn(board);

        // Call SUT
        this.mockMvc.perform(post("/play/move/2").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(board))));
    }

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testGetTurn() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Player playerTwo = new Player("irene", "irene@irene.nl", "irene");
        Long gameId = 1L;
        Game gameOne  = new Game(gameId, player, playerTwo, player, GameState.IN_PROGRESS);

        // Rules
        when(playerService.fetchLoggedInUser()).thenReturn(player);
        when(gameService.findGameById(gameId)).thenReturn(gameOne);

        // Call SUT
        this.mockMvc.perform(get("/play/turn").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(player))));
    }

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testGetPlayerScore() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Player playerTwo = new Player("irene", "irene@irene.nl", "irene");
        Long gameId = 1L;
        Game gameOne  = new Game(gameId, player, playerTwo, player, GameState.IN_PROGRESS);

        int score = 991;

        // Rules
        when(playerService.fetchLoggedInUser()).thenReturn(player);
        when(gameService.findGameById(gameId)).thenReturn(gameOne);
        when(playService.getScore(gameOne, player)).thenReturn(score);

        // Call SUT
        this.mockMvc.perform(get("/play/score").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(score))));
    }

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testGetGameState() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Player playerTwo = new Player("irene", "irene@irene.nl", "irene");
        Long gameId = 1L;
        Game gameOne  = new Game(gameId, player, playerTwo, player, GameState.IN_PROGRESS);

        // Rules
        when(playerService.fetchLoggedInUser()).thenReturn(player);
        when(gameService.findGameById(gameId)).thenReturn(gameOne);

        // Call SUT
        this.mockMvc.perform(get("/play/state").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(GameState.IN_PROGRESS))));
    }

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testGetBoard() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, GameState.WAIT_FOR_PLAYER);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        Board board = new Board(gameOne);
        board.setId(1); // Does not matter here

        List<Pit> pits = new ArrayList<>();

        for(int i = 1; i <= 2; i++) {
            Pit p = new Pit(board, (i*7), 0, PitType.STORE);
            pits.add(p);
        }

        for(int i = 1; i <= 6; i++) {
            Pit p = new Pit(board, i, 6, PitType.HOUSE);
            pits.add(p);
        }

        for(int i = 8; i <= 13; i++) {
            Pit p = new Pit(board, i, 6, PitType.HOUSE);
            pits.add(p);
        }

        board.setPits(pits);

        // Rules
        when(playerService.fetchLoggedInUser()).thenReturn(player);
        when(gameService.findGameById(gameId)).thenReturn(gameOne);
        when(boardService.getBoardByGame(gameOne)).thenReturn(board);

        // Call SUT
        this.mockMvc.perform(get("/play/board").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(board))));
    }

}
