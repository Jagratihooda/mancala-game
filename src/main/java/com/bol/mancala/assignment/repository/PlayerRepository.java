package com.bol.mancala.assignment.repository;

import com.bol.mancala.assignment.domain.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for @{@link Player}
 */
@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    Player findByUsername(String username);
}
