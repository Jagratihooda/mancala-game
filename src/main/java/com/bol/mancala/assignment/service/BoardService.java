package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);


    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * This method is used to prepare a new Board for a given Game
     * game
     *
     * @param game
     * @return board
     */
    public Board prepareNewBoard(Game game) {
        LOGGER.info("Creating new Board");

        Board board = Board.builder().game(game).build();
        boardRepository.save(board);

        LOGGER.info("New Board created with id" + board.getId());

        return board;
    }

    /**
     * This method is used to fetch a Board for a given Game
     * game
     *
     * @param game
     * @return board
     */
    public Board fetchBoardByGame(Game game) {
        LOGGER.info("Fetching a baord for game id" + game.getId());

        return boardRepository.findByGame(game);
    }

}
