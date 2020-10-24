package com.bol.mancala.assignment.domain;

import com.bol.mancala.assignment.enums.PitType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "stone_count", nullable = false)
    private int stoneCount;

    @Enumerated(EnumType.STRING)
    private PitType pitType;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

}
