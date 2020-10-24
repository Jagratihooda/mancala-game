package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Pit;
import com.bol.mancala.assignment.enums.PitType;
import com.bol.mancala.assignment.repository.PitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for Pit related actions
 */
@Service
@Transactional
public class PitService {

    @Autowired
    private PitRepository pitRepository;

    public void preparePit(Board board) {

        createPitForIndividualPlayer(board, MancalaConstants.FIRST_PLAYER_MANCALA_POSITION, MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION, MancalaConstants.FIRST_PLAYER_END_PIT_POSITION);
        createPitForIndividualPlayer(board, MancalaConstants.SECOND_PLAYER_MANCALA_POSITION, MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION, MancalaConstants.SECOND_PLAYER_END_PIT_POSITION);

    }

    private void createPitForIndividualPlayer(Board board, int mancalaPos, int startingPitPos, int endPitPosition) {
        pitRepository.save(Pit.builder().board(board).pitType(PitType.MANCALA).position(mancalaPos).build());

        for (int i = startingPitPos; i <= endPitPosition; i++) {
            pitRepository.save(Pit.builder().board(board).pitType(PitType.REGULAR).position(i).stoneCount(MancalaConstants.INITIAL_STONE_COUNT).build());
        }
    }


    public Pit fetchPitByBoardAndPosition(Board board, int position) {
        return pitRepository.findByBoardAndPosition(board, position);
    }


    public Pit clearStones(Board board, int position) {
        Pit pit = fetchPitByBoardAndPosition(board, position);

        pit.setStoneCount(0);

        pitRepository.save(pit);

        return pit;
    }

    public Pit updateStonesByAmount(Board board, int position, int amount) {
        Pit pit = fetchPitByBoardAndPosition(board, position);

        pit.setStoneCount(pit.getStoneCount() + amount);

        pitRepository.save(pit);

        return pit;
    }


}
