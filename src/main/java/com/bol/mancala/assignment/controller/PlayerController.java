package com.bol.mancala.assignment.controller;

import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class to handle @{@link Player} related REST calls
 */
@RestController
@RequestMapping("/player")
public class PlayerController {

    private PlayerService playerService;

    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);


    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


   /* @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Player createAccount(@RequestBody PlayerDTO playerDTO) {
        Player newPlayer = playerService.createPlayer(playerDTO);
        return newPlayer;
    }*/

    /*@RequestMapping(value = "/logged", produces = MediaType.APPLICATION_JSON_VALUE)
    public Player getLoggedInPlayer() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerService.getPlayerByUsername(principal.getPlayer().getUsername());
    }*/
}