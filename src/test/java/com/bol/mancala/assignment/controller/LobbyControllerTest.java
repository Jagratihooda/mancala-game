package com.bol.mancala.assignment.controller;

import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.repository.PlayerRepository;
import com.bol.mancala.assignment.service.BoardService;
import com.bol.mancala.assignment.service.GameService;
import com.bol.mancala.assignment.service.PitService;
import com.bol.mancala.assignment.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {
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
    @WithMockUser(username = "Player1", password = "Player1")
    public void testListGamesToJoin() throws Exception {
        Player player = Player.builder().firstName("Player1").lastName("Player1").password("Player1").username("Player1").build();
        Long gameId = 1L;
        Game game = Game.builder().id(gameId).firstPlayer(player).playerInAction(player).gameState(GameState.WAITING_FOR_SECOND_PLAYER).build();

        List<Game> games = new ArrayList<>();
        games.add(game);
        when(playerService.fetchLoggedInUser()).thenReturn(player);
        when(gameService.fetchGamesToJoinForAPlayer(player)).thenReturn(games);
        this.mockMvc.perform(get("/lobby/to-be-joined/game/list")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Player1", password = "Player1")
    public void testListGamesActive() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();
        Player player1 = Player.builder().firstName("Player1").lastName("Player1").password("Player1").username("Player1").build();
        Player player2 = Player.builder().firstName("Player2").lastName("Player2").password("Player2").username("Player2").build();

        Long gameId = 1L;
        Game game = new Game(gameId, GameState.GAME_IN_PROGRESS, player2, player2, player1);
        List<Game> games = new ArrayList<>();
        games.add(game);

        when(playerService.fetchLoggedInUser()).thenReturn(player1);
        when(gameService.fetchPlayerGames(player1)).thenReturn(games);

        this.mockMvc.perform(get("/lobby/player/own-games/list")).andExpect(status().isOk());
    }

}