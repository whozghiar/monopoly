package fr.monopoly.backend.Services;

import fr.monopoly.backend.Exceptions.GameExceptions.GameDuplicatePlayerException;
import fr.monopoly.backend.Exceptions.GameExceptions.GameFullException;
import fr.monopoly.backend.Exceptions.GameExceptions.GameNotFoundException;
import fr.monopoly.backend.Exceptions.GameExceptions.GameStartedException;
import fr.monopoly.backend.Exceptions.PlayerExceptions.PlayerNotFoundException;
import fr.monopoly.backend.Models.Game;
import fr.monopoly.backend.Models.Player;
import fr.monopoly.backend.Repositories.GameRepository;
import fr.monopoly.backend.Repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;


    public Game createGame() {
        Game game = new Game();
        gameRepository.save(game);
        return game;
    }

    public Game findGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game with ID " + id + " not found"));
    }

    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new GameNotFoundException("Game with ID " + id + " not found");
        }
        gameRepository.deleteById(id);
    }

    public Game addPlayerToGame(Long gameId, Player player) {
        Game game = findGameById(gameId);
        if(game.isStarted()) {
            throw new GameStartedException("Cannot add player to a game that has already started.");
        }
        if (game.getPlayers().contains(player)) {
            throw new GameDuplicatePlayerException("Player already in game.");
        }
        if (game.getPlayers().size() >= 8) {
            throw new GameFullException("Cannot add more players, the game is full.");
        }
        game.getPlayers().add(player);
        playerRepository.save(player);
        return gameRepository.save(game);
    }

    public Game removePlayerFromGame(Long gameId, Player player) {
        Game game = findGameById(gameId);

        if (!game.getPlayers().contains(player)) {
            throw new PlayerNotFoundException("Player not found in game.");
        }

        // Handle player exit
        if(game.isStarted()) {
            handlePlayerExit(game, player);
        }

        game.getPlayers().remove(player);
        playerRepository.delete(player);

        if (!canGameContinue(gameId)) {
            endGame(gameId);
        }

        return gameRepository.save(game);
    }

    public Game advanceTurn(Long gameId) {
        Game game = findGameById(gameId);
        if (game.getPlayers().isEmpty() || game.getPlayers().size() < 2){
            throw new IllegalStateException("Cannot advance turn in uncrowded game.");
        }
        int nextPlayer = (game.getCurrentPlayer() + 1) % game.getPlayers().size();
        game.setCurrentPlayer(nextPlayer);
        return gameRepository.save(game);
    }

    private void handlePlayerExit(Game game, Player player) {
        //TODO: Implement this method
        log.info("Player " + player.getName() + " has left the game.");
    }

    private boolean canGameContinue(Long gameId) {
        Game game = findGameById(gameId);
        return game.getPlayers().size() >= 2;
    }

    private void endGame(Long gameId) {
        Game game = findGameById(gameId);
        game.setOver(true);
        gameRepository.save(game);
    }

}
