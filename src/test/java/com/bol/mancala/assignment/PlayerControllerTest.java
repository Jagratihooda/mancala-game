package com.bol.mancala.assignment;

import com.bol.mancala.assignment.controller.PlayerController;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.dto.PlayerDTO;
import com.bol.mancala.assignment.repository.PlayerRepository;
import com.bol.mancala.assignment.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalah.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PitService pitService;

    @MockBean
    private PlayService playService;

    @MockBean
    private PlayerRepository playerRepository; // Needed for securitycontext

    @Test
    @WithMockUser(username = "alexander", password = "alexander") // Needed because MockMVC does not use SecurityConfig
    public void testCreateUser() throws Exception {
        // Test specific inits
        ObjectMapper objMapper = new ObjectMapper();
        Player player = new Player("alexander", "alexander@alexander.com", "alexander");
        PlayerDTO playerDTO = new PlayerDTO("alexander", "alexander", "alexander@alexander.com");

        // Rules
        when(playerService.createPlayer(any(PlayerDTO.class))).thenReturn(player);

        // Call SUT
        this.mockMvc.perform(
                post("/player/create").
                        contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(objMapper.writeValueAsString(playerDTO)).
                        accept(MediaType.APPLICATION_JSON)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string(containsString(objMapper.writeValueAsString(player))));
    }

}
