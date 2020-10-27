package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.security.ContextUser;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Class for Player related actions
 */
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    @Override
    public Player getPlayerByUsername(String name) {
        return playerRepository.findByUsername(name);
    }

    @Override
    public Player fetchLoggedInUser() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByUsername(principal.getPlayer().getUsername());
    }
}
