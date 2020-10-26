package com.bol.mancala.assignment.controller;

import com.bol.mancala.assignment.domain.Pit;
import com.bol.mancala.assignment.enums.PitType;
import com.bol.mancala.assignment.service.BoardService;
import com.bol.mancala.assignment.service.GameService;
import com.bol.mancala.assignment.service.PlayService;
import com.bol.mancala.assignment.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.repository.PlayerRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardController.class)
public class BoardControllerTest {
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
    private PlayerRepository playerRepository;

    @Test
    @WithMockUser(username = "Player1", password = "Player1")
    public void testMovePosition() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player1 = Player.builder().firstName("Player1").lastName("Player1").password("Player1").username("Player1").build();
        Player player2 = Player.builder().firstName("Player2").lastName("Player2").password("Player2").username("Player2").build();

        Long gameId = 1L;
        Game game = new Game(gameId, GameState.GAME_IN_PROGRESS, player2, player2, player1);
        List<Game> games = new ArrayList<>();
        games.add(game);

        Board board = new Board(game);
        board.setId(1);
        board.setPits(fetchPitList());

        when(playerService.fetchLoggedInUser()).thenReturn(player1);
        when(gameService.findGameById(any(Long.class))).thenReturn(game);
        when(playService.makeMove(player1, game, 2)).thenReturn(board);
        this.mockMvc.perform(post("/board/make-move?position=2").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Player1", password = "Player1")
    public void testSelectGame() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();

        Player player1 = Player.builder().firstName("Player1").lastName("Player1").password("Player1").username("Player1").build();
        Player player2 = Player.builder().firstName("Player2").lastName("Player2").password("Player2").username("Player2").build();

        Long gameId = 1L;
        Game gameOne = new Game(gameId, GameState.GAME_IN_PROGRESS, player2, player2, player1);

        // Rules
        when(gameService.findGameById(gameId)).thenReturn(gameOne);

        // Call SUT
        this.mockMvc.perform(get("/board/1")).andDo(print()).andExpect(status().isOk());
    }

    private List<Pit> fetchPitList() {
        List<Pit> pitList = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            pitList.add(Pit.builder().id(i).pitType(PitType.REGULAR).position(i).stoneCount(6).build());
        }
        for (int i = 8; i <= 13; i++) {
            pitList.add(Pit.builder().id(i).pitType(PitType.REGULAR).position(i).stoneCount(6).build());
        }

        Pit pit7 = Pit.builder().id(7).pitType(PitType.MANCALA).position(7).stoneCount(0).build();
        Pit pit14 = Pit.builder().id(9).pitType(PitType.MANCALA).position(14).stoneCount(0).build();
        pitList.add(pit7);
        pitList.add(pit14);
        return pitList;
    }
}
