/*
package com.kalah;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalah.controller.GameController;
import com.kalah.domain.Board;
import com.kalah.domain.Game;
import com.kalah.domain.Player;
import com.kalah.enums.GameState;
import com.kalah.enums.PitType;
import com.kalah.repository.PlayerRepository;
import com.kalah.service.BoardService;
import com.kalah.service.GameService;
import com.kalah.service.PitService;
import com.kalah.service.PlayerService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PitService pitService;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private SimpMessagingTemplate templateMock;

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testListGamesToJoin() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, GameState.WAIT_FOR_PLAYER);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        // Rules
        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getGamesToJoin(player)).thenReturn(games);

        // Call SUT
        this.mockMvc.perform(get("/game/list")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(games))));
    }

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testListGamesActive() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Player playerTwo = new Player("irene", "irene@irene.nl", "irene");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, playerTwo, player, GameState.IN_PROGRESS);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        // Rules
        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getPlayerGames(player)).thenReturn(games);

        // Call SUT
        this.mockMvc.perform(get("/game/player/list")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(games))));
    }

    @Test
    @WithMockUser(username = "alexander", password = "alexander")
    public void testSelectGame() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Player playerTwo = new Player("irene", "irene@irene.nl", "irene");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, playerTwo, player, GameState.IN_PROGRESS);

        // Rules
        when(gameService.getGameById(gameId)).thenReturn(gameOne);

        // Call SUT
        this.mockMvc.perform(get("/game/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(gameOne))));
    }

    @Test
    @WithMockUser(username = "irene", password = "irene")
    public void testJoinGame() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Player playerTwo = new Player("irene", "irene@irene.nl", "irene");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, playerTwo, player, GameState.IN_PROGRESS);

        // Rules
        when(playerService.getLoggedInUser()).thenReturn(playerTwo);
        when(gameService.joinGame(playerTwo, gameId)).thenReturn(gameOne);

        // Call SUT
        this.mockMvc.perform(post("/game/join/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(gameOne))));
    }

    @Test
    @WithMockUser(username = "irene", password = "irene")
    public void testCreateGame() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("alexander", "alexander@alexander.nl", "alexander");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, GameState.WAIT_FOR_PLAYER);

        Board boardMock = mock(Board.class);

        // Rules
        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.createNewGame(player)).thenReturn(gameOne);
        when(boardService.createNewBoard(gameOne)).thenReturn(boardMock);

        // Call SUT
        this.mockMvc.perform(post("/game/create")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(gameOne))));

        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.STORE), eq(7), eq(0));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.STORE), eq(14), eq(0));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(1), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(2), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(3), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(4), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(5), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(6), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(8), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(9), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(10), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(11), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(12), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(PitType.HOUSE), eq(13), eq(6));

    }
}*/
