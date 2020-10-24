/*
package com.kalah;

import com.kalah.domain.Game;
import com.kalah.domain.Player;
import com.kalah.enums.GameState;
import com.kalah.repository.GameRepository;
import com.kalah.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepositoryMock;

    private GameService gameService;

    @Before
    public void init() {
        gameService = new GameService(gameRepositoryMock);
    }

    @Test
    public void testCreateNewGame() {
        // Test specific init
        Player playerMock = mock(Player.class);

        // Test rules

        // Call to SUT
        Game result = gameService.createNewGame(playerMock);

        // Verify result/calls
        assertEquals(result.getFirstPlayer(), playerMock);
        assertEquals(result.getPlayerTurn(), playerMock);
        assertEquals(result.getGameState(), GameState.WAIT_FOR_PLAYER);

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }

    @Test
    public void testJoinGame() {
        // Test specific init
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = new Game(gameId, playerOneMock, null, playerOneMock, GameState.WAIT_FOR_PLAYER);

        // Test rules
        when(gameRepositoryMock.findOne(gameId)).thenReturn(game);

        // Call to SUT
        Game result = gameService.joinGame(playerMock, gameId);

        // Verify result/calls
        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getPlayerTurn(), playerOneMock);
        assertEquals(result.getSecondPlayer(), playerMock);
        assertEquals(result.getGameState(), GameState.IN_PROGRESS);

        verify(gameRepositoryMock, times(2)).save(any(Game.class)); // Also for updateGameState
    }

    @Test
    public void testSwitchTurn() {
        // Test specific init
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = new Game(gameId, playerOneMock, playerMock, playerMock, GameState.IN_PROGRESS);

        // Test rules
        when(gameRepositoryMock.findOne(gameId)).thenReturn(game);

        // Call to SUT
        Game result = gameService.switchTurn(playerMock, gameId);

        // Verify result/calls
        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getPlayerTurn(), playerMock);
        assertEquals(result.getSecondPlayer(), playerMock);
        assertEquals(result.getGameState(), GameState.IN_PROGRESS);

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }

    @Test
    public void testUpdateGameState() {
        // Test specific init
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = new Game(gameId, playerOneMock, playerMock, playerOneMock, GameState.IN_PROGRESS);

        // Test rules
        when(gameRepositoryMock.findOne(gameId)).thenReturn(game);

        // Call to SUT
        Game result = gameService.updateGameState(game, GameState.FINISHED);

        // Verify result/calls
        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getPlayerTurn(), playerOneMock);
        assertEquals(result.getSecondPlayer(), playerMock);
        assertEquals(result.getGameState(), GameState.FINISHED);

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }
}
*/
