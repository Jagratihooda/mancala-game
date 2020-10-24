package com.bol.mancala.assignment.repository;

import com.bol.mancala.assignment.domain.Player;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    Player findByUsername(String username);
}
