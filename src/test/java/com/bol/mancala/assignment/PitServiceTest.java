/*
package com.kalah;

import com.kalah.domain.Board;
import com.kalah.domain.Pit;
import com.kalah.enums.PitType;
import com.kalah.repository.PitRepository;
import com.kalah.service.PitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PitServiceTest {

    @Mock
    private PitRepository pitRepositoryMock;

    private PitService pitService;

    @Before
    public void init() {
        pitService = new PitService(pitRepositoryMock);
    }

    @Test
    public void testCreatePitStore() {
        // Test specific init
        Board boardMock = mock(Board.class);
        PitType type = PitType.STORE;
        int position = 7;
        int nr_of_stones = 10;

        // Test rules

        // Call to SUT
        Pit result = pitService.createPit(boardMock, type, position, nr_of_stones);

        // Verify result/calls
        assertEquals(result.getBoard(), boardMock);
        assertEquals(result.getPitType(), type);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getNumberOfStones(), nr_of_stones);

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }

    @Test
    public void testCreatePitHouse() {
        // Test specific init
        Board boardMock = mock(Board.class);
        PitType type = PitType.HOUSE;
        int position = 10;
        int nr_of_stones = 4;

        // Test rules

        // Call to SUT
        Pit result = pitService.createPit(boardMock, type, position, nr_of_stones);

        // Verify result/calls
        assertEquals(result.getBoard(), boardMock);
        assertEquals(result.getPitType(), type);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getNumberOfStones(), nr_of_stones);

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }

    @Test
    public void testUpdateNumberOfStones() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 10;
        int amount = 4;

        Pit pit = new Pit(boardMock, position, 100, PitType.HOUSE);

        // Test rules
        when(pitRepositoryMock.findByBoardAndPosition(boardMock, position)).thenReturn(pit);

        // Call to SUT
        Pit result = pitService.updatePitNumberOfStones(boardMock, position, amount);

        // Verify result/calls
        assertEquals(result.getBoard(), boardMock);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getNumberOfStones(), amount); // Correct amount set

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }

    @Test
    public void testUpdateNumberOfStonesByOne() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 10;
        int amount = 101;

        Pit pit = new Pit(boardMock, position, 100, PitType.HOUSE);

        // Test rules
        when(pitRepositoryMock.findByBoardAndPosition(boardMock, position)).thenReturn(pit);

        // Call to SUT
        Pit result = pitService.updatePitNumberOfStonesByOne(boardMock, position);

        // Verify result/calls
        assertEquals(result.getBoard(), boardMock);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getNumberOfStones(), amount); // Correct amount set

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }

    @Test
    public void testUpdateNumberOfStonesByAmount() {
        // Test specific init
        Board boardMock = mock(Board.class);
        int position = 10;
        int amount = 4;

        Pit pit = new Pit(boardMock, position, 100, PitType.HOUSE);

        // Test rules
        when(pitRepositoryMock.findByBoardAndPosition(boardMock, position)).thenReturn(pit);

        // Call to SUT
        Pit result = pitService.updatePitNumberOfStonesByAmount(boardMock, position, amount);

        // Verify result/calls
        assertEquals(result.getBoard(), boardMock);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getNumberOfStones(), 104); // Correct amount set

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }
}
*/
