package com.bol.mancala.assignment.domain;

import com.bol.mancala.assignment.enums.GameState;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;

/**
 * Game domain class
 */
@Entity
@Getter
@Setter
@Check(constraints = "game_state = 'IN_PROGRESS' or game_state = 'FINISHED' or game_state = 'WAIT_FOR_PLAYER'")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_player_id", nullable = false)
    private Player firstPlayer;

    @ManyToOne
    @JoinColumn(name = "second_player_id", nullable = true)
    private Player secondPlayer;

    @ManyToOne
    @JoinColumn(name = "player_turn_id", nullable = true)
    private Player playerInAction;

    @Enumerated(EnumType.STRING)
    private GameState gameState;

    public Game(Player firstPlayer, Player playerTurn, GameState gameState) {
        this.firstPlayer = firstPlayer;
        this.playerInAction = playerInAction;
        this.gameState = gameState;
    }
}
