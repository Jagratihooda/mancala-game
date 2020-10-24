package com.bol.mancala.assignment.service;
import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.exception.ServiceException;
import com.bol.mancala.assignment.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameService {

    private HttpSession httpSession;
    private GameRepository gameRepository;

    @Autowired
    public GameService(HttpSession httpSession, GameRepository gameRepository ) {
        this.httpSession = httpSession;
        this.gameRepository = gameRepository;
    }

    /**
     * This method is to prepare a new Game for a player
     * game
     * @param  player
     * @return game
     */
    public Game prepareNewGame(Player player) {
        Game game = Game.builder().firstPlayer(player).playerInAction(player).gameState(GameState.WAITING_FOR_SECOND_PLAYER).build();

        gameRepository.save(game);

        httpSession.setAttribute(MancalaConstants.GAME_SESSION_ID, game.getId());

        return game;
    }

    /**
     * This method is called when a player wants to join an existing game
     * game
     * @param  player
     * @param  gameId
     * @return game
     */
    public Game joinAnExistingGame(Player player, Long gameId) {
        Game game = findGameById(gameId);

        game.setSecondPlayer(player);
        game.setGameState(GameState.GAME_IN_PROGRESS);

        gameRepository.save(game);

        httpSession.setAttribute(MancalaConstants.GAME_SESSION_ID, game.getId());

        return game;
    }

    /**
     * This method is called to update current state of a game
     * game
     * @param  game
     * @param  gameState
     * @return game
     */
    public Game updateGameState(Game game, GameState gameState) {

        Game existingGame = findGameById(game.getId());

        existingGame.setGameState(gameState);

        gameRepository.save(existingGame);

        return existingGame;
    }

    /**
     * This method is called to switch player's turn
     * game
     * @param  player
     * @param  game
     * @return game
     */
    public Game changePlayerTurn(Player player, Game game) {

        game.setPlayerInAction(player);

        gameRepository.save(game);

        return game;
    }

    /**
     * This method is called to fetch all the games that a specific player can join
     * @param  player
     * @return list of games
     */
    public List<Game> fetchGamesToJoinForAPlayer(Player player) {
        return gameRepository.findByGameState(GameState.WAITING_FOR_SECOND_PLAYER)
                .stream().filter(
                        game -> game.getFirstPlayer() != player
                ).collect(Collectors.toList());
    }

    /**
     * This method is called to fetch all the games belong to a player
     * @param  player
     * @return list of games
     */
    public List<Game> fetchPlayerGames(Player player) {
        return gameRepository.findByGameState(GameState.GAME_IN_PROGRESS)
                .stream().filter(
                        game -> (game.getFirstPlayer() == player ||
                                game.getSecondPlayer() == player)

                ).collect(Collectors.toList());
    }

    /**
     * This method is called to fetch game per id
     * @param  id
     * @return game
     */
    public Game findGameById(Long id) {
        Optional<Game> game = gameRepository.findById(id);
        if(game.isPresent())
            return gameRepository.findById(id).get();
        else
            throw new ServiceException("Game not found with id: " +id);

    }

}
