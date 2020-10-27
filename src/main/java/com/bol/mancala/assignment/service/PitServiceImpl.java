package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Pit;
import com.bol.mancala.assignment.enums.PitType;
import com.bol.mancala.assignment.repository.PitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for Pit related actions
 */
@Service
@Transactional
public class PitServiceImpl implements PitService {

    @Autowired
    private PitRepository pitRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PitServiceImpl.class);

    @Override
    public void preparePit(Board board) {
        LOGGER.info("Preparing pits for board with id " + board.getId());

        createPitForIndividualPlayer(board, MancalaConstants.FIRST_PLAYER_MANCALA_POSITION, MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION, MancalaConstants.FIRST_PLAYER_END_PIT_POSITION);
        createPitForIndividualPlayer(board, MancalaConstants.SECOND_PLAYER_MANCALA_POSITION, MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION, MancalaConstants.SECOND_PLAYER_END_PIT_POSITION);

        LOGGER.info("Pits prepared for board " + board.getId());
    }

    private void createPitForIndividualPlayer(Board board, int mancalaPos, int startingPitPos, int endPitPosition) {
        pitRepository.save(Pit.builder().board(board).pitType(PitType.MANCALA).position(mancalaPos).build());

        for (int i = startingPitPos; i <= endPitPosition; i++) {
            pitRepository.save(Pit.builder().board(board).pitType(PitType.REGULAR).position(i).stoneCount(MancalaConstants.INITIAL_STONE_COUNT).build());
        }
    }

    @Override
    public Pit fetchPitByBoardAndPosition(Board board, int position) {
        LOGGER.info("Fetching pit by board " + board.getId() + " and position " + position);

        return pitRepository.findByBoardAndPosition(board, position);
    }

    @Override
    public Pit clearStones(Board board, int position) {
        LOGGER.info("Clearing stones for board " + board.getId() + " and position " + position);

        Pit pit = fetchPitByBoardAndPosition(board, position);

        pit.setStoneCount(0);

        pitRepository.save(pit);

        return pit;
    }

    @Override
    public Pit updateStonesByAmount(Board board, int position, int amount) {
        LOGGER.info("Updating stones for board " + board.getId() + "position " + position + "and amount" + amount);

        Pit pit = fetchPitByBoardAndPosition(board, position);

        pit.setStoneCount(pit.getStoneCount() + amount);

        pitRepository.save(pit);

        return pit;
    }


}
