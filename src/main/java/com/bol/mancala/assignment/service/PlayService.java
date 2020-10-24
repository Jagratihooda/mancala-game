package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiPredicate;

/**
 * Class to handle gameplay related actions
 */
@Service
@Transactional
public class PlayService {

    private GameService gameService;
    private BoardService boardService;
    private PitService pitService;

    @Autowired
    public PlayService(GameService gameService, BoardService boardService, PitService pitService) {
        this.gameService = gameService;
        this.boardService = boardService;
        this.pitService = pitService;
    }


    public Board makeMove(Player player, Game game, int position) {
        // Get the board
        Board board = boardService.getBoardByGame(game);

        if(checkTurn.test(game, player)) {

            if(player == game.getFirstPlayer()) {
                board = handleFirstPlayerMove(game, position);
            }
            else {
                board = handleSecondPlayerMove(game, position);
            }
        }

        return board;
    }

    private BiPredicate<Game,Player> checkTurn = (game, player) ->
         game.getPlayerInAction() == player && game.getGameState() != GameState.FINISHED;

    public int getScore(Game game, Player player) {
        // Get board
        Board board = boardService.getBoardByGame(game);

        // Score is nr of stones in Store

        // P1
        if(player == game.getFirstPlayer()) {
            return pitService.fetchPitByBoardAndPosition(board, MancalaConstants.FIRST_PLAYER_MANCALA_POSITION).getNumberOfStones();
        }
        // P2
        else {
            return pitService.fetchPitByBoardAndPosition(board, MancalaConstants.SECOND_PLAYER_MANCALA_POSITION).getNumberOfStones();
        }
    }

    private BiPredicate<Integer, Integer> validateFirstPlayerPit = (pitPosition, stoneCount) ->
         pitPosition >= MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION && pitPosition <= MancalaConstants.FIRST_PLAYER_END_PIT_POSITION && stoneCount > 0;

    private BiPredicate<Integer, Integer> validateSecondPlayerPit = (pitPosition, stoneCount) ->
            pitPosition >= MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION && pitPosition <= MancalaConstants.SECOND_PLAYER_END_PIT_POSITION && stoneCount > 0;

    private Board handleFirstPlayerMove(Game game, int position) {
        Board board = boardService.getBoardByGame(game);
        int stoneCount = pitService.fetchPitByBoardAndPosition(board, position).getNumberOfStones();
        if(validateFirstPlayerPit.test(position, stoneCount )) {
            int newPosition = sowStones(board, position, MancalaConstants.SECOND_PLAYER_END_PIT_POSITION, false);
            checkCapture(board, newPosition, MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION, MancalaConstants.FIRST_PLAYER_END_PIT_POSITION, MancalaConstants.FIRST_PLAYER_MANCALA_POSITION);
            changeTurnIfApplicable(game, newPosition, MancalaConstants.FIRST_PLAYER_MANCALA_POSITION, game.getSecondPlayer());
            updateGameIfFinished(game, board);
        }
        return board;
    }

    private Board handleSecondPlayerMove(Game game, int position) {
        Board board = boardService.getBoardByGame(game);
        int stoneCount = pitService.fetchPitByBoardAndPosition(board, position).getNumberOfStones();
        if(validateSecondPlayerPit.test(position, stoneCount )) {
            int newPosition = sowStones(board, position, MancalaConstants.SECOND_PLAYER_MANCALA_POSITION, true);
            checkCapture(board, newPosition, MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION, MancalaConstants.SECOND_PLAYER_END_PIT_POSITION, MancalaConstants.SECOND_PLAYER_MANCALA_POSITION);
            changeTurnIfApplicable(game, newPosition, MancalaConstants.SECOND_PLAYER_MANCALA_POSITION, game.getFirstPlayer());
            updateGameIfFinished(game, board);
        }
        return board;
    }

    private void changeTurnIfApplicable(Game game, int position, int storePosition, Player nextPlayerInAction){
        if (position != storePosition) {
            gameService.changePlayerTurn(nextPlayerInAction, game);
        }

    }
    private void updateGameIfFinished(Game game, Board board){
        if(checkFinished(game, board)) {
            gameService.updateGameState(game, GameState.FINISHED);
            emptyAllPits(board);
        }

    }

    public int sowStones(Board board, int currentPosition, int upper, boolean skipP1Store) {
        // Get nr of stones from startPit and empty
        int stoneCount = pitService.fetchPitByBoardAndPosition(board, currentPosition).getNumberOfStones();
        pitService.clearStones(board, currentPosition);

        // Starting Position
        int position = currentPosition + 1;

        // Start Sowing
        while (stoneCount != 0) {
            if(position > upper) {
                position = MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION;
            }
            else if (skipP1Store && position == MancalaConstants.FIRST_PLAYER_MANCALA_POSITION) {
                // Skip P1 store
                position = MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION;
            }

            // Add stone for every pit
            pitService.updateStonesByAmount(board, position, 1);

            position++;
            stoneCount--;
        }

        position--;
        return position;
    }


    public void checkCapture(Board board, int index, int lower, int upper, int store) {
        // Check if between boundaries, i.e. landed on his own house and if the house was empty
        if(index >= lower && index <= upper
                && pitService.fetchPitByBoardAndPosition(board, index).getNumberOfStones() == 1) {
            // Capture stones across
            int indexAcross = MancalaConstants.MAX_NR_OF_PITS - index;
            int amountAcross = pitService.fetchPitByBoardAndPosition(board, indexAcross).getNumberOfStones();

            if(amountAcross > 0) {
                // Empty own house + across
                pitService.clearStones(board, indexAcross);
                pitService.clearStones(board, index);

                // Add to store
                pitService.updateStonesByAmount(board, store, (amountAcross + 1));
            }
        }
    }


    public boolean checkFinished(Game game, Board board) {

        boolean isFinished = true;

        int lower;
        int upper;

        // If current Player has no stones left game is Finished
        if(game.getPlayerInAction() == game.getFirstPlayer()) {
            lower = MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION;
            upper = MancalaConstants.FIRST_PLAYER_END_PIT_POSITION;
        }
        else {
            lower = MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION;
            upper = MancalaConstants.SECOND_PLAYER_END_PIT_POSITION;
        }

        // Check Player houses for stones
        for (int i = lower; i <= upper; i++) {
            if (pitService.fetchPitByBoardAndPosition(board, i).getNumberOfStones() > 0) {
                isFinished = false;
                break;
            }
        }

        return isFinished;
    }

    public void emptyAllPits(Board board) {
        // Empty P1 houses
        for (int i = MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION; i <= MancalaConstants.FIRST_PLAYER_END_PIT_POSITION; i++) {
            int tmpAmount = pitService.fetchPitByBoardAndPosition(board, i).getNumberOfStones();
            if (tmpAmount > 0) {
                pitService.clearStones(board, i);
                pitService.updateStonesByAmount(board, MancalaConstants.FIRST_PLAYER_MANCALA_POSITION, tmpAmount);
            }
        }

        // Empty P2 houses
        for (int i = MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION; i <= MancalaConstants.FIRST_PLAYER_END_PIT_POSITION; i++) {
            int tmpAmount = pitService.fetchPitByBoardAndPosition(board, i).getNumberOfStones();
            if (tmpAmount > 0) {
                pitService.clearStones(board, i);
                pitService.updateStonesByAmount(board, MancalaConstants.SECOND_PLAYER_MANCALA_POSITION, tmpAmount);
            }
        }
    }
}
