package com.bol.mancala.assignment;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.repository.BoardRepository;
import com.bol.mancala.assignment.service.BoardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepositoryMock;

    private BoardService boardService;

    @Before
    public void init() {
        boardService = new BoardService(boardRepositoryMock);
    }

    @Test
    public void testCreateNewBoard() {
        // Test specific init
        Game gameMock = mock(Game.class);

        // Test rules

        // Call to SUT
        Board result = boardService.createNewBoard(gameMock);

        // Verify result/calls
        assertEquals(result.getGame(), gameMock);

        verify(boardRepositoryMock, times(1)).save(any(Board.class));
    }
}
