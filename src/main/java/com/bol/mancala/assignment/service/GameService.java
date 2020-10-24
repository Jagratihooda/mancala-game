package com.bol.mancala.assignment.service;
import com.bol.mancala.assignment.constants.MancalaConstants;
import com.bol.mancala.assignment.domain.Game;
import com.bol.mancala.assignment.domain.Player;
import com.bol.mancala.assignment.enums.GameState;
import com.bol.mancala.assignment.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession;
import java.util.List;
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

    public Game prepareNewGame(Player player) {
        Game game = Game.builder().firstPlayer(player).playerInAction(player).gameState(GameState.WAIT_FOR_PLAYER).build();

        gameRepository.save(game);

        httpSession.setAttribute(MancalaConstants.GAME_SESSION_ID, game.getId());

        return game;
    }


    public Game joinAnExistingGame(Player player, Long gameId) {
        Game game = findGameById(gameId);

        game.setSecondPlayer(player);
        game.setGameState(GameState.IN_PROGRESS);

        gameRepository.save(game);

        httpSession.setAttribute(MancalaConstants.GAME_SESSION_ID, game.getId());

        return game;
    }
    public Game updateGameState(Game game, GameState gameState) {

        Game existingGame = findGameById(game.getId());

        existingGame.setGameState(gameState);

        gameRepository.save(existingGame);

        return existingGame;
    }

    public Game changePlayerTurn(Player player, Game game) {

        game.setPlayerInAction(player);

        gameRepository.save(game);

        return game;
    }

    public List<Game> fetchGamesToJoinForAPlayer(Player player) {
        return gameRepository.findByGameState(GameState.WAIT_FOR_PLAYER)
                .stream().filter(
                        game -> game.getFirstPlayer() != player
                ).collect(Collectors.toList());
    }


    public List<Game> fetchPlayerGames(Player player) {
        return gameRepository.findByGameState(GameState.IN_PROGRESS)
                .stream().filter(
                        game -> (game.getFirstPlayer() == player ||
                                game.getSecondPlayer() == player)

                ).collect(Collectors.toList());
    }

    public Game findGameById(Long id) {

        return gameRepository.findOne(id);
    }

}
