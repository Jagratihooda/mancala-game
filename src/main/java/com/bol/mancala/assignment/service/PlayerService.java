package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.security.ContextUser;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.dto.PlayerDTO;
import com.bol.mancala.assignment.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class for @{@link Player} related actions
 */
@Service
@Transactional
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }



    public Player getPlayerDetails() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Player player = playerRepository.findByUsername(principal.getPlayer().getUsername());
        return player;
    }


    public Player createPlayer(PlayerDTO playerDTO) {
        // Create Player
        Player player = new Player(playerDTO.getUsername(), playerDTO.getEmail(), playerDTO.getPassword());

        // Save Player
        playerRepository.save(player);

        return player;
    }


    public Player getPlayerByUsername(String name) {
        return playerRepository.findByUsername(name);
    }


    public Player fetchLoggedInUser() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByUsername(principal.getPlayer().getUsername());
    }
}
