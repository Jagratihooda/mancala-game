/*
package com.kalah;

import com.kalah.domain.Board;
import com.kalah.domain.Game;
import com.kalah.domain.Player;
import com.kalah.enums.GameState;
import com.kalah.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.hamcrest.Matchers.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.AdditionalMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
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
    public void testIsTurn() {
        Game gameMock = mock(Game.class);
        Player playerMock = mock(Player.class);

        when(gameMock.getPlayerTurn()).thenReturn(playerMock);
        boolean isTurn = playService.isTurn(gameMock, playerMock);

        assertTrue(isTurn);
    }

    @Test
    public void testIsNotTurn() {
        Game gameMock = mock(Game.class);
        Player playerMock = mock(Player.class);
        Player otherPlayerMock = mock(Player.class);

        when(gameMock.getPlayerTurn()).thenReturn(otherPlayerMock);
        boolean isTurn = playService.isTurn(gameMock, playerMock);

        assertFalse(isTurn);
    }

    @Test
    public void testGetPlayerOneScore() {
        Game gameMock = mock(Game.class);
        Player playerMock = mock(Player.class);
        Board boardMock = mock(Board.class);

        int score = 10;

        when(boardServiceMock.getBoardByGame(gameMock)).thenReturn(boardMock);
        when(gameMock.getFirstPlayer()).thenReturn(playerMock);
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), eq(7))).thenReturn(score);

        int result = playService.getScore(gameMock, playerMock);

        assertEquals(result, score);
    }

    @Test
    public void testGetPlayerTwoScore() {
        Game gameMock = mock(Game.class);
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Board boardMock = mock(Board.class);

        int score = 10;

        when(boardServiceMock.getBoardByGame(gameMock)).thenReturn(boardMock);
        when(gameMock.getFirstPlayer()).thenReturn(playerOneMock);
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), eq(14))).thenReturn(score);

        int result = playService.getScore(gameMock, playerMock);

        assertEquals(result, score);
    }

    @Test
    public void testSowSeedsPlayerOneValidStore() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 1; // Start Pit position
        int upper = 13; // Player 1 max nr before back to 1
        boolean skipP1Store = false; // Player 1 can sow into P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, position)).thenReturn(6); // return 6 stones

        // Call to SUT
        playService.sowStones(boardMock, position, upper, skipP1Store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, 1, 0);

        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 2);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 3);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 4);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 5);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 6);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 7);
    }

    @Test
    public void testSowSeedsPlayerOneValidUpper() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 6; // Start Pit position
        int upper = 13; // Player 1 max nr before back to 1
        boolean skipP1Store = false; // Player 1 can sow into P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, position)).thenReturn(9); // return 6 stones

        // Call to SUT
        playService.sowStones(boardMock, position, upper, skipP1Store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, 6, 0);

        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 7);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 8);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 9);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 10);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 11);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 12);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 13);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 1);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 2);
    }

    @Test
    public void testSowSeedsPlayerOneEmptyPit() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 5; // Start Pit position
        int upper = 13; // Player 1 max nr before back to 1
        boolean skipP1Store = false; // Player 1 can sow into P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, position)).thenReturn(0); // return 0 stones

        // Call to SUT
        playService.sowStones(boardMock, position, upper, skipP1Store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, 5, 0);

        verify(pitServiceMock, never()).updatePitNumberOfStonesByOne(eq(boardMock), anyInt()); // No sowing
    }

    @Test
    public void testSowSeedsPlayerTwoValidStore() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 8; // Start Pit position
        int upper = 14; // Player 2 max nr before back to 1
        boolean skipP1Store = true; // Player 2 can not sow into P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, position)).thenReturn(6); // return 6 stones

        // Call to SUT
        playService.sowStones(boardMock, position, upper, skipP1Store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, 8, 0);

        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 9);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 10);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 11);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 12);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 13);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 14);
    }

    @Test
    public void testSowSeedsPlayerTwoValidUpper() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 12; // Start Pit position
        int upper = 14; // Player 2 max nr before back to 1
        boolean skipP1Store = true; // Player 2 can not sow into P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, position)).thenReturn(6); // return 6 stones

        // Call to SUT
        playService.sowStones(boardMock, position, upper, skipP1Store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, 12, 0);

        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 13);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 14);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 1);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 2);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 3);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 4);
    }

    @Test
    public void testSowSeedsPlayerTwoValidSkipStore() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 13; // Start Pit position
        int upper = 14; // Player 2 max nr before back to 1
        boolean skipP1Store = true; // Player 2 can not sow into P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, position)).thenReturn(10); // return 6 stones

        // Call to SUT
        playService.sowStones(boardMock, position, upper, skipP1Store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, 13, 0);

        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 14);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 1);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 2);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 3);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 4);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 5);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 6);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 8); // Skip store
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 9);
        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByOne(boardMock, 10);
    }

    @Test
    public void testSowSeedsPlayerTwoEmptyPit() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 10; // Start Pit position
        int upper = 14; // Player 2 max nr before back to 1
        boolean skipP1Store = true; // Player 2 can not sow into P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, position)).thenReturn(0); // return 0 stones

        // Call to SUT
        playService.sowStones(boardMock, position, upper, skipP1Store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, 10, 0);

        verify(pitServiceMock, never()).updatePitNumberOfStonesByOne(eq(boardMock), anyInt()); // No sowing
    }

    @Test
    public void testCheckCapturePlayerOneValid() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 5; // End Pit position
        int indexAcross = (14 - index);
        int lower = 1; // P1 lower boundary
        int upper = 6; // P1 upper boundary
        int store = 7; // P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(1); // return 1 stones so capture
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, indexAcross)).thenReturn(6); // return 6 stones across

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, index, 0);
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, indexAcross, 0);

        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByAmount(eq(boardMock), eq(store), eq(7)); // 1 + 6
    }

    @Test
    public void testCheckCapturePlayerOneNoCapture() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 5; // End Pit position
        int lower = 1; // P1 lower boundary
        int upper = 6; // P1 upper boundary
        int store = 7; // P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(10); // return 10 stones so NO capture

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, never()).updatePitNumberOfStones(eq(boardMock), anyInt(), anyInt());
        verify(pitServiceMock, never()).updatePitNumberOfStonesByAmount(eq(boardMock), anyInt(), anyInt());
    }

    @Test
    public void testCheckCapturePlayerOneOutOfBounds() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 10; // End Pit position (not owned by P1)
        int lower = 1; // P1 lower boundary
        int upper = 6; // P1 upper boundary
        int store = 7; // P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(1); // return 1 stone

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, never()).updatePitNumberOfStones(eq(boardMock), anyInt(), anyInt());
        verify(pitServiceMock, never()).updatePitNumberOfStonesByAmount(eq(boardMock), anyInt(), anyInt());
    }

    @Test
    public void testCheckCapturePlayerOneStore() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 7; // End Pit position (not owned by P1)
        int lower = 1; // P1 lower boundary
        int upper = 6; // P1 upper boundary
        int store = 7; // P1 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(1); // return 1 stone

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, never()).updatePitNumberOfStones(eq(boardMock), anyInt(), anyInt());
        verify(pitServiceMock, never()).updatePitNumberOfStonesByAmount(eq(boardMock), anyInt(), anyInt());
    }

    @Test
    public void testCheckCapturePlayerTwoValid() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 12; // End Pit position
        int indexAcross = (14 - index);
        int lower = 8; // P2 lower boundary
        int upper = 13; // P2 upper boundary
        int store = 14; // P2 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(1); // return 1 stones so capture
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, indexAcross)).thenReturn(6); // return 6 stones across

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, index, 0);
        verify(pitServiceMock, times(1)).updatePitNumberOfStones(boardMock, indexAcross, 0);

        verify(pitServiceMock, times(1)).updatePitNumberOfStonesByAmount(eq(boardMock), eq(store), eq(7)); // 1 + 6
    }

    @Test
    public void testCheckCapturePlayerTwoNoCapture() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 12; // End Pit position
        int lower = 8; // P2 lower boundary
        int upper = 13; // P2 upper boundary
        int store = 14; // P2 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(10); // return 10 stones so NO capture

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, never()).updatePitNumberOfStones(eq(boardMock), anyInt(), anyInt());
        verify(pitServiceMock, never()).updatePitNumberOfStonesByAmount(eq(boardMock), anyInt(), anyInt());
    }

    @Test
    public void testCheckCapturePlayerTwoOutOfBounds() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 2; // End Pit position (not owned by P1)
        int lower = 8; // P2 lower boundary
        int upper = 13; // P2 upper boundary
        int store = 12; // P2 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(1); // return 1 stone

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, never()).updatePitNumberOfStones(eq(boardMock), anyInt(), anyInt());
        verify(pitServiceMock, never()).updatePitNumberOfStonesByAmount(eq(boardMock), anyInt(), anyInt());
    }

    @Test
    public void testCheckCapturePlayerTwoStore() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int index = 14; // End Pit position (not owned by P1)
        int lower = 8; // P2 lower boundary
        int upper = 13; // P2 upper boundary
        int store = 14; // P2 store

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(boardMock, index)).thenReturn(1); // return 1 stone

        // Call to SUT
        playService.checkCapture(boardMock, index, lower, upper, store);

        // Verify result/calls
        verify(pitServiceMock, never()).updatePitNumberOfStones(eq(boardMock), anyInt(), anyInt());
        verify(pitServiceMock, never()).updatePitNumberOfStonesByAmount(eq(boardMock), anyInt(), anyInt());
    }

    @Test
    public void testCheckFinishedP1True() {
        // Test specific init
        Board boardMock = mock(Board.class);
        Player p1 = new Player("test1", "test@test.nl", "test1");
        Player p2 = new Player("test2", "test@test.nl", "test2");
        Game game = new Game(p1, p1, GameState.IN_PROGRESS);
        game.setSecondPlayer(p2);

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), anyInt())).thenReturn(0);

        // Call to SUT
        boolean result = playService.checkFinished(game, boardMock);

        // Verify result/calls
        assertTrue(result);
    }

    @Test
    public void testCheckFinishedP1False() {
        // Test specific init
        Board boardMock = mock(Board.class);
        Player p1 = new Player("test1", "test@test.nl", "test1");
        Player p2 = new Player("test2", "test@test.nl", "test2");
        Game game = new Game(p1, p1, GameState.IN_PROGRESS);
        game.setSecondPlayer(p2);

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), anyInt())).thenReturn(1);

        // Call to SUT
        boolean result = playService.checkFinished(game, boardMock);

        // Verify result/calls
        assertFalse(result);
    }

    @Test
    public void testCheckFinishedP1OneStoneRemaining() {
        // Test specific init
        Board boardMock = mock(Board.class);
        Player p1 = new Player("test1", "test@test.nl", "test1");
        Player p2 = new Player("test2", "test@test.nl", "test2");
        Game game = new Game(p1, p1, GameState.IN_PROGRESS);
        game.setSecondPlayer(p2);

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), anyInt())).thenReturn(0,0, 0, 0,
                0, 1, 0); // return 1 just a single time

        // Call to SUT
        boolean result = playService.checkFinished(game, boardMock);

        // Verify result/calls
        assertFalse(result);
    }

    @Test
    public void testCheckFinishedP2True() {
        // Test specific init
        Board boardMock = mock(Board.class);
        Player p1 = new Player("test1", "test@test.nl", "test1");
        Player p2 = new Player("test2", "test@test.nl", "test2");
        Game game = new Game(p1, p2, GameState.IN_PROGRESS);
        game.setSecondPlayer(p2);

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), gt(7))).thenReturn(0);

        // Call to SUT
        boolean result = playService.checkFinished(game, boardMock);

        // Verify result/calls
        assertTrue(result);
    }

    @Test
    public void testCheckFinishedP2False() {
        // Test specific init
        Board boardMock = mock(Board.class);
        Player p1 = new Player("test1", "test@test.nl", "test1");
        Player p2 = new Player("test2", "test@test.nl", "test2");
        Game game = new Game(p1, p2, GameState.IN_PROGRESS);
        game.setSecondPlayer(p2);

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), gt(7))).thenReturn(1);

        // Call to SUT
        boolean result = playService.checkFinished(game, boardMock);

        // Verify result/calls
        assertFalse(result);
    }

    @Test
    public void testCheckFinishedP2OneStoneRemaining() {
        // Test specific init
        Board boardMock = mock(Board.class);
        Player p1 = new Player("test1", "test@test.nl", "test1");
        Player p2 = new Player("test2", "test@test.nl", "test2");
        Game game = new Game(p1, p2, GameState.IN_PROGRESS);
        game.setSecondPlayer(p2);

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), gt(7))).thenReturn(0,0, 0, 0,
                0, 1, 0); // return 1 just a single time

        // Call to SUT
        boolean result = playService.checkFinished(game, boardMock);

        // Verify result/calls
        assertFalse(result);
    }

    @Test
    public void testEmptyAllPits() {
        // Test specific init
        Board boardMock = mock(Board.class);

        // Test rules
        when(pitServiceMock.getPitNumberOfStonesByBoardAndPosition(eq(boardMock), anyInt())).thenReturn(3); // 3 in every pit

        // Call to SUT
        playService.emptyAllPits(boardMock);

        // Verify result/calls
        verify(pitServiceMock, times(12)).updatePitNumberOfStones(eq(boardMock), anyInt(), eq(0));
        verify(pitServiceMock, times(12)).updatePitNumberOfStonesByAmount(eq(boardMock), anyInt(), eq(3));
    }

}
*/
