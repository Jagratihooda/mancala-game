package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.repository.BoardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepositoryMock;

    @Mock
    private BoardServiceImpl boardService;

    @Before
    public void init() {
        boardService = new BoardServiceImpl(boardRepositoryMock);
    }

    @Test
    public void testPrepareNewBoard() {
        Game gameMock = mock(Game.class);
        Board result = boardService.prepareNewBoard(gameMock);

        assertEquals(result.getGame(), gameMock);
        verify(boardRepositoryMock, times(1)).save(any(Board.class));
    }

    @Test
    public void testFetchBoardByGame() {
        Game gameMock = mock(Game.class);
        boardService.fetchBoardByGame(gameMock);
        verify(boardRepositoryMock, times(1)).findByGame(any(Game.class));

    }
}
