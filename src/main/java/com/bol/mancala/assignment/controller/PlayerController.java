package com.bol.mancala.assignment.controller;

import com.bol.mancala.assignment.security.ContextUser;
import com.bol.mancala.assignment.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle Player's related calls
 */
@RestController
@RequestMapping("/player")
public class PlayerController {

    private PlayerService playerService;

    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);


    @RequestMapping(value = "/loggedin-user")
    public String fetchLoggedInPlayerUserName() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getPlayer().getUsername();
    }
}