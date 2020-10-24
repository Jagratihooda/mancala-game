package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardService {

    private BoardRepository boardRepository;


    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    public Board prepareNewBoard(Game game) {
        Board board = Board.builder().game(game).build();
        boardRepository.save(board);
        return board;
    }


    public Board createNewBoard(Game game) {
        Board board = new Board(game);

        boardRepository.save(board);

        return board;
    }


    public Board getBoardByGame(Game game) {
        return boardRepository.findByGame(game);
    }

}
