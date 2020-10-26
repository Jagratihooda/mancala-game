package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Pit;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.enums.PitType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayServiceTest {

    @Mock
    private BoardService boardServiceMock;

    @Mock
    private PitService pitServiceMock;

    @Mock
    private GameService gameServiceMock;

    private PlayService playService;

    @Before
    public void init() {
        playService = new PlayService(gameServiceMock, boardServiceMock, pitServiceMock);
    }

    @Test
    public void makeMoveTest() {
        Player p1 = Player.builder().firstName("J").username("J").password("J").build();
        Player p2 = Player.builder().firstName("A").username("A").password("A").build();
        Game game = Game.builder().firstPlayer(p1).secondPlayer(p2).gameState(GameState.GAME_IN_PROGRESS).playerInAction(p1).build();

        Pit pit1 = Pit.builder().id(1).pitType(PitType.REGULAR).position(1).stoneCount(6).build();

        Board board = Board.builder().game(game).id(1).pits(fetchPitList()).build();
        when(boardServiceMock.fetchBoardByGame(game)).thenReturn(board);
        when(pitServiceMock.fetchPitByBoardAndPosition(board, 1)).thenReturn(pit1);
        when(pitServiceMock.clearStones(board, 1)).thenReturn(pit1);

        Board resultBoard = playService.makeMove(p1, game, 1);
        verify(pitServiceMock, times(6)).updateStonesByAmount(any(Board.class), anyInt(), anyInt());

        assertEquals("J", resultBoard.getGame().getPlayerInAction().getUsername());
        assertEquals("J", resultBoard.getGame().getFirstPlayer().getUsername());
        assertEquals("A", resultBoard.getGame().getSecondPlayer().getUsername());
        assertEquals(GameState.GAME_IN_PROGRESS, resultBoard.getGame().getGameState());
    }

    @Test
    public void makeMoveWhenGameIsFinishedTest() {
        Player p1 = Player.builder().firstName("J").username("J").password("J").build();
        Player p2 = Player.builder().firstName("A").username("A").password("A").build();
        Game game = Game.builder().firstPlayer(p1).secondPlayer(p2).gameState(GameState.GAME_FINISHED).playerInAction(p1).build();

        Board resultBoard = playService.makeMove(p1, game, 1);
        verify(pitServiceMock, times(0)).updateStonesByAmount(any(Board.class), anyInt(), anyInt());

        assertEquals(null, resultBoard);
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
