package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.repository.GameRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
    @Mock
    private GameRepository gameRepositoryMock;

    private GameService gameService;
    @Mock
    private HttpSession httpSessionMock;

    @Before
    public void init() {
        gameService = new GameService(httpSessionMock, gameRepositoryMock);
    }

    @Test
    public void testCreateNewGame() {
        Player playerMock = mock(Player.class);

        Game result = gameService.prepareNewGame(playerMock);

        assertEquals(result.getFirstPlayer(), playerMock);
        assertEquals(GameState.WAITING_FOR_SECOND_PLAYER, result.getGameState());

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }

    @Test
    public void testJoinAnExistingGame() {
        // Test specific init
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = Game.builder().id(gameId).gameState(GameState.WAITING_FOR_SECOND_PLAYER).firstPlayer(playerOneMock).build();
        when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(game));

        Game result = gameService.joinAnExistingGame(playerMock, gameId);

        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getSecondPlayer(), playerMock);
        assertEquals(GameState.GAME_IN_PROGRESS, result.getGameState());

        verify(gameRepositoryMock, times(1)).save(any(Game.class)); // Also for updateGameState
    }

    @Test
    public void testChangePlayerTurn() {

        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = Game.builder().id(gameId).gameState(GameState.WAITING_FOR_SECOND_PLAYER).firstPlayer(playerOneMock).build();

        when(gameRepositoryMock.save(game)).thenReturn(game);

        Game result = gameService.changePlayerTurn(playerMock, game);

        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getPlayerInAction(), playerMock);

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }

    @Test
    public void testUpdateGameState() {
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = Game.builder().id(gameId).gameState(GameState.WAITING_FOR_SECOND_PLAYER).firstPlayer(playerOneMock).build();

        when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(game));

        Game result = gameService.updateGameState(game, GameState.GAME_FINISHED);

        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(GameState.GAME_FINISHED, result.getGameState());

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }
}
