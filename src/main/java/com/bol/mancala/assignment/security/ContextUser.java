package com.bol.mancala.assignment.security;

import com.google.common.collect.ImmutableSet;
import com.bol.mancala.assignment.domain.Player;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
/**
 * Class for user context
 */
public class ContextUser extends User {

    private final Player player;

    public ContextUser(Player player) {
        super(player.getUsername(),
                player.getPassword(),
                true,
                true,
                true,
                true,
                ImmutableSet.of(new SimpleGrantedAuthority("create")));

        this.player = player;
    }

    public Player getPlayer() {
        return  player;
    }
}