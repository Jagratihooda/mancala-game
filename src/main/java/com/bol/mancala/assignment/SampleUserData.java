package com.bol.mancala.assignment;

import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SampleUserData {

    @Autowired
    PlayerRepository playerRepository;

    @Bean
    public String demo(PlayerRepository playerRepository) {
        playerRepository.save(Player.builder().firstName("Player1").lastName("Player1").username("Player1").password(new BCryptPasswordEncoder().encode("Player1")).id(1).build());
        playerRepository.save(Player.builder().firstName("Player2").lastName("Player2").username("Player2").password(new BCryptPasswordEncoder().encode("Player2")).id(2).build());
        return "Users Created";
        }
    }

