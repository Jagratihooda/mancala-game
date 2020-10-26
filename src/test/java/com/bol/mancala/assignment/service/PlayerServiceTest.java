package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.repository.PlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
        Player player = mock(Player.class);
        String name = "Jagrati";

        when(playerRepositoryMock.findByUsername(name)).thenReturn(player);

        Player result = playerService.getPlayerByUsername(name);

        assertEquals(player, result);
    }
}
