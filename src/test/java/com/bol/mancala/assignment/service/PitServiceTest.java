package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Pit;
import com.bol.mancala.assignment.enums.PitType;
import com.bol.mancala.assignment.repository.PitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PitServiceTest {

    @Mock
    private PitRepository pitRepositoryMock;
    @InjectMocks
    private PitService pitService;

    @Test
    public void testUpdateNumberOfStonesByAmount() {
        Board boardMock = mock(Board.class);
        int position = 10;
        int amount = 5;

        Pit pit = Pit.builder().id(1).pitType(PitType.MANCALA).position(position).stoneCount(10).board(boardMock).build();

        when(pitRepositoryMock.findByBoardAndPosition(boardMock, position)).thenReturn(pit);

        Pit result = pitService.updateStonesByAmount(boardMock, position, amount);

        // Verify result/calls
        assertEquals(boardMock, result.getBoard());
        assertEquals(position, result.getPosition());
        assertEquals(15, result.getStoneCount());

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }


    @Test
    public void testClearStones() {
        Board boardMock = mock(Board.class);
        int position = 10;

        Pit pit = Pit.builder().id(1).pitType(PitType.MANCALA).position(position).stoneCount(10).board(boardMock).build();

        when(pitRepositoryMock.findByBoardAndPosition(boardMock, position)).thenReturn(pit);

        Pit result = pitService.clearStones(boardMock, position);

        assertEquals(boardMock, result.getBoard());
        assertEquals(position, result.getPosition());
        assertEquals(0, result.getStoneCount());

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }

    @Test
    public void testCreatePitStore() {
        Board boardMock = mock(Board.class);

        pitService.preparePit(boardMock);

        verify(pitRepositoryMock, times(14)).save(any(Pit.class));
    }


}
