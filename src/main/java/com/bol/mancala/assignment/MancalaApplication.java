package com.bol.mancala.assignment;

import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Main class
 */
@SpringBootApplication
public class MancalaApplication {

    public static void main(String[] args) {

        SpringApplication.run(MancalaApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(PlayerRepository playerRepository) {
        return (args) -> {

            playerRepository.save(new Player("a", "alexander@bolhuis.com", new BCryptPasswordEncoder().encode("a")));
            playerRepository.save(new Player("b", "irene@vanderheijden.com",  new BCryptPasswordEncoder().encode("b")));
         };
    }

}