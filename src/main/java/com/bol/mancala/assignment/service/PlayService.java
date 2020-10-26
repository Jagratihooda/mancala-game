package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Board;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Pit;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiPredicate;

/**
 * Service Class to handle game related actions
 */
@Service
@Transactional
public class PlayService {

    private final GameService gameService;
    private final BoardService boardService;
    private final PitService pitService;

    private final Logger LOGGER = LoggerFactory.getLogger(PlayService.class);

    @Autowired
    public PlayService(GameService gameService, BoardService boardService, PitService pitService) {
        this.gameService = gameService;
        this.boardService = boardService;
        this.pitService = pitService;
    }


    public Board makeMove(Player player, Game game, int position) {

        LOGGER.info("Making move for player " + player.getId() + "game " + game.getId() + "and position" + position);

        Board board = boardService.fetchBoardByGame(game);

        if (checkTurn.test(game, player)) {

            if (player == game.getFirstPlayer()) {
                board = handlePlayerMove(game, position, MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION
                        , MancalaConstants.FIRST_PLAYER_END_PIT_POSITION, MancalaConstants.FIRST_PLAYER_MANCALA_POSITION);
            } else {
                board = handlePlayerMove(game, position, MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION
                        , MancalaConstants.SECOND_PLAYER_END_PIT_POSITION
                        , MancalaConstants.SECOND_PLAYER_MANCALA_POSITION);
            }
        }

        return board;
    }

    private BiPredicate<Game, Player> checkTurn = (game, player) ->
            game.getPlayerInAction() == player && game.getGameState() != GameState.GAME_FINISHED;


    private Board handlePlayerMove(Game game, int position, int startPitPos, int endPitPos, int mancalaPos) {

        Board board = boardService.fetchBoardByGame(game);
        Pit pit = pitService.fetchPitByBoardAndPosition(board, position);
        int stoneCount = pit.getStoneCount();

        LOGGER.info("Handling move for board " + board.getId() + "pit " + pit.getId() + "and stoneCount" + stoneCount);


        if (position >= startPitPos && position <= endPitPos && stoneCount > 0) {
            int newPosition = shiftStones(board, position, mancalaPos, stoneCount);

            captureOpponentStones(board, newPosition, startPitPos,
                    endPitPos, mancalaPos, stoneCount);

            changeTurnIfApplicable(game, newPosition, mancalaPos);

            updateGameIfFinished(game, board, startPitPos, endPitPos);
        }
        return board;
    }

    private void changeTurnIfApplicable(Game game, int position, int mancalaPosition) {
        if (position != mancalaPosition) {
            Player nextPlayerInAction = null;

            if (mancalaPosition == MancalaConstants.FIRST_PLAYER_MANCALA_POSITION) {
                nextPlayerInAction = game.getSecondPlayer();
            } else {
                nextPlayerInAction = game.getFirstPlayer();
            }

            LOGGER.info("Player in Action " + nextPlayerInAction);

            gameService.changePlayerTurn(nextPlayerInAction, game);
        }

    }

    private void updateGameIfFinished(Game game, Board board, int startPitPos, int endPitPos) {
        if (checkIfGameFinished(game, board, startPitPos, endPitPos)) {
            gameService.updateGameState(game, GameState.GAME_FINISHED);
        }

    }

    private int shiftStones(Board board, int currentPosition, int mancalaPos, int stoneCount) {
        LOGGER.info("Shifting stones for board " + board.getId() + " currentPosition " + currentPosition + "stoneCount" + stoneCount);

        pitService.clearStones(board, currentPosition);

        // Starting Position
        int position = currentPosition + 1;

        // Start shifting
        while (stoneCount != 0) {

            if (mancalaPos == MancalaConstants.SECOND_PLAYER_MANCALA_POSITION && position > MancalaConstants.SECOND_PLAYER_MANCALA_POSITION) {
                position = MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION;
            } else if (mancalaPos == MancalaConstants.SECOND_PLAYER_MANCALA_POSITION && position == MancalaConstants.FIRST_PLAYER_MANCALA_POSITION) {
                position = MancalaConstants.SECOND_PLAYER_STARTING_PIT_POSITION;
            } else if (mancalaPos == MancalaConstants.FIRST_PLAYER_MANCALA_POSITION && position == MancalaConstants.SECOND_PLAYER_MANCALA_POSITION) {
                position = MancalaConstants.FIRST_PLAYER_STARTING_PIT_POSITION;
            }
            // Add stone for every pit
            pitService.updateStonesByAmount(board, position, 1);

            position++;
            stoneCount--;
        }

        position--;

        LOGGER.info("Stones shifted for board " + board.getId() + " position " + position + "stoneCount" + stoneCount);

        return position;
    }


    private void captureOpponentStones(Board board, int currentPosition, int lower, int upper, int mancala, int stoneCount) {

        if (currentPosition >= lower && currentPosition <= upper && stoneCount == 1) {
            // Capture Opponent's Stones
            int opponentPos = MancalaConstants.MAX_NR_OF_PITS - currentPosition;
            int opponentStones = pitService.fetchPitByBoardAndPosition(board, opponentPos).getStoneCount();

            if (opponentStones > 0) {

                pitService.clearStones(board, opponentPos);
                pitService.clearStones(board, currentPosition);

                // Add to store
                pitService.updateStonesByAmount(board, mancala, (opponentStones + 1));

                LOGGER.info("Opponents stones captured from position " + opponentPos + " amount " + opponentStones);

            }
        }
    }

    private boolean checkIfGameFinished(Game game, Board board, int startPitPos, int endPitPos) {

        boolean isGameFinished = true;

        //Check whether Pits are empty
        for (int i = startPitPos; i <= endPitPos; i++) {
            if (pitService.fetchPitByBoardAndPosition(board, i).getStoneCount() > 0) {
                isGameFinished = false;
                break;
            }
        }
        LOGGER.info("isGameFinished" + isGameFinished);

        return isGameFinished;
    }
}
