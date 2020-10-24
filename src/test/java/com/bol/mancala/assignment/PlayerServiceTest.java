package com.bol.mancala.assignment;

import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.dto.PlayerDTO;
import com.bol.mancala.assignment.repository.PlayerRepository;
import com.bol.mancala.assignment.service.PlayerService;
import com.kalah.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepositoryMock;

    private PlayerService playerService;

    @Before
    public void init() {
        playerService = new PlayerService(playerRepositoryMock);
    }

    @Test
    public void testGetPlayerByName() {
        // Test specific init
        Player player = mock(Player.class);
        String name = "alexander";

        // Test rules
        when(playerRepositoryMock.findByUsername(name)).thenReturn(player);

        // Call to SUT
        Player result = playerService.getPlayerByUsername(name);

        // Verify result/calls
        assertEquals(player, result);
    }

    @Test
    public void testCreatePlayer() {
        // Test specific init
        PlayerDTO playerDTO = new PlayerDTO("alexander", "alexander", "alexander@alexander.com");

        // Test rules

        // Call to SUT
        Player result = playerService.createPlayer(playerDTO);

        // Verify result/calls
        assertEquals(result.getUsername(), "alexander");
        assertEquals(result.getEmail(), "alexander@alexander.com");
        assertEquals(result.getPassword(), "alexander");
        verify(playerRepositoryMock, times(1)).save(any(Player.class));
    }
}
