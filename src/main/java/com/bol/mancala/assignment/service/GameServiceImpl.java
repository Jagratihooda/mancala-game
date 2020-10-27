package com.bol.mancala.assignment.service;

import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.exception.ServiceException;
import com.bol.mancala.assignment.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final HttpSession httpSession;
    private final GameRepository gameRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    public GameServiceImpl(HttpSession httpSession, GameRepository gameRepository) {
        this.httpSession = httpSession;
        this.gameRepository = gameRepository;
    }


    @Override
    public Game prepareNewGame(Player player) {
        LOGGER.info("Preparing new Game");

        Game game = Game.builder().firstPlayer(player).playerInAction(player).gameState(GameState.WAITING_FOR_SECOND_PLAYER).build();

        gameRepository.save(game);

        httpSession.setAttribute(MancalaConstants.GAME_SESSION_ID, game.getId());

        LOGGER.info("New Game is prepared with id " + game.getId());

        return game;
    }

    @Override
    public Game joinAnExistingGame(Player player, Long gameId) {
        LOGGER.info("Player2 Joining an existing game with id " + gameId);

        Game game = findGameById(gameId);

        game.setSecondPlayer(player);
        game.setGameState(GameState.GAME_IN_PROGRESS);

        gameRepository.save(game);

        httpSession.setAttribute(MancalaConstants.GAME_SESSION_ID, game.getId());

        LOGGER.info("Player2 joined the game");

        return game;
    }


    @Override
    public Game updateGameState(Game game, GameState gameState) {
        LOGGER.info("Updating gameState of a game with id" + game.getId());

        Game existingGame = findGameById(game.getId());

        existingGame.setGameState(gameState);

        gameRepository.save(existingGame);

        LOGGER.info("Updated game state " + gameState.name());

        return existingGame;
    }


    @Override
    public Game changePlayerTurn(Player player, Game game) {
        LOGGER.info("Updating player turn for player");

        game.setPlayerInAction(player);

        gameRepository.save(game);

        return game;
    }


    @Override
    public List<Game> fetchGamesToJoinForAPlayer(Player player) {
        return gameRepository.findByGameState(GameState.WAITING_FOR_SECOND_PLAYER)
                .stream().filter(
                        game -> game.getFirstPlayer() != player
                ).collect(Collectors.toList());
    }

    @Override
    public List<Game> fetchPlayerGames(Player player) {
        return gameRepository.findByGameState(GameState.GAME_IN_PROGRESS)
                .stream().filter(
                        game -> (game.getFirstPlayer() == player ||
                                game.getSecondPlayer() == player)

                ).collect(Collectors.toList());
    }


    @Override
    public Game findGameById(Long id) {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent())
            return game.get();
        else
            throw new ServiceException("Game not found with id: " + id);

    }

}
