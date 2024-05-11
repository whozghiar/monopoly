package fr.monopoly.backend.Services;

import fr.monopoly.backend.Exceptions.GameExceptions.GameDuplicatePlayerException;
import fr.monopoly.backend.Exceptions.GameExceptions.GameFullException;
import fr.monopoly.backend.Exceptions.GameExceptions.GameNotFoundException;
import fr.monopoly.backend.Exceptions.GameExceptions.GameStartedException;
import fr.monopoly.backend.Exceptions.PlayerExceptions.PlayerNotFoundException;
import fr.monopoly.backend.Models.*;
import fr.monopoly.backend.Models.Enumerations.CellName;
import fr.monopoly.backend.Models.Enumerations.CellType;
import fr.monopoly.backend.Repositories.GameRepository;
import fr.monopoly.backend.Repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;


    public Game createGame() {
        Game game = new Game();
        game.setBoard(initializeBoard());
        game.setStarted(false);
        game.setOver(false);
        game.setCurrentPlayer(null);

        addLog(game, "Game created.");
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
        addLog(game, "Player " + player.getName() + " has joined the game.");
        log.info("Player " + player.getName() + " has joined the game.");
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
        addLog(game, "Player " + player.getName() + " has left the game.");
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

        List<Player> playersList = new ArrayList<>(game.getPlayers());
        Long nextPlayer = (game.getCurrentPlayer() + 1) % playersList.size();
        game.setCurrentPlayer(nextPlayer);
        addLog(game, "Turn advanced to player " + playersList.get(nextPlayer.intValue()).getName());
        log.info("Turn advanced to player " + playersList.get(nextPlayer.intValue()).getName());
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

    private void addLog(Game game, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        game.getActionLogs().add(timestamp + " - " + message);
    }

    private Board initializeBoard() {
        Board board = new Board();
        Set<Cell> cells = board.getCells();

        cells.add(new Cell(CellName.DEPART.name(), CellType.GO));

        cells.add(new CellProperty(CellName.BOULEVARD_DE_BELLEVILLE.name(), CellType.PROPERTY, 60, 2));
        cells.add(new CellProperty(CellName.RUE_LECOURBE.name(), CellType.PROPERTY, 60, 4));

        cells.add(new CellTax(CellName.TAXE_DE_LUXE.name(), CellType.TAX, 75));

        cells.add(new CellProperty(CellName.GARE_MONTPARNASSE.name(), CellType.RAILROAD, 200, 8));
        cells.add(new CellProperty(CellName.RUE_DE_VAUGIRARD.name(), CellType.PROPERTY, 100, 10));

        cells.add(new Cell(CellName.CHANCE.name(), CellType.CHANCE));

        cells.add(new CellProperty(CellName.RUE_DE_COURCELLES.name(), CellType.PROPERTY, 100, 12));
        cells.add(new CellProperty(CellName.AVENUE_DE_LA_REPUBLIQUE.name(), CellType.PROPERTY, 120, 14));

        cells.add(new Cell(CellName.VISITE_DE_PRISON.name(), CellType.VISIT_JAIL));

        cells.add(new CellProperty(CellName.BOULEVARD_DE_LA_VILLETTE.name(), CellType.PROPERTY, 140, 16));

        cells.add(new CellTax(CellName.COMPAGNIE_DE_DISTIBUTION_ELECTRIQUE.name(), CellType.TAX, 150));

        cells.add(new CellProperty(CellName.AVENUE_DE_NEUILLY.name(), CellType.PROPERTY, 140, 18));
        cells.add(new CellProperty(CellName.RUE_DE_PARADIS.name(), CellType.PROPERTY, 160, 20));

        cells.add(new CellProperty(CellName.GARE_DE_LYON.name(), CellType.RAILROAD, 200, 8));

        cells.add(new CellProperty(CellName.AVENUE_DE_MOZART.name(), CellType.PROPERTY, 180, 22));

        cells.add(new Cell(CellName.COMMUNAUTE.name(), CellType.COMMUNITY_CHEST));

        cells.add(new CellProperty(CellName.BOULEVARD_SAINT_MICHEL.name(), CellType.PROPERTY, 180, 24));
        cells.add(new CellProperty(CellName.PLACE_PIGALLE.name(), CellType.PROPERTY, 200, 26));

        cells.add(new Cell(CellName.PARC_GRATUIT.name(), CellType.FREE_PARKING));

        cells.add(new CellProperty(CellName.AVENUE_MATIGNON.name(), CellType.PROPERTY, 220, 28));

        cells.add(new Cell(CellName.CHANCE.name(), CellType.CHANCE));

        cells.add(new CellProperty(CellName.BOULEVARD_MALESHERBES.name(), CellType.PROPERTY, 220, 30));
        cells.add(new CellProperty(CellName.AVENUE_HENRI_MARTIN.name(), CellType.PROPERTY, 240, 32));

        cells.add(new CellProperty(CellName.GARE_DU_NORD.name(), CellType.RAILROAD, 200, 8));

        cells.add(new CellProperty(CellName.FAUBOURG_SAINT_HONORE.name(), CellType.PROPERTY, 260, 34));
        cells.add(new CellProperty(CellName.PLACE_DE_LA_BOURSE.name(), CellType.PROPERTY, 260, 36));

        cells.add(new CellTax(CellName.COMPAGNIE_DES_EAUX.name(), CellType.TAX, 150));

        cells.add(new CellProperty(CellName.RUE_LA_FAYETTE.name(), CellType.PROPERTY, 280, 38));

        cells.add(new Cell(CellName.PRISON.name(), CellType.JAIL));

        cells.add(new CellProperty(CellName.AVENUE_DE_BRETEUIL.name(), CellType.PROPERTY, 300, 40));
        cells.add(new CellProperty(CellName.AVENUE_FOCH.name(), CellType.PROPERTY, 300, 42));

        cells.add(new Cell(CellName.COMMUNAUTE.name(), CellType.COMMUNITY_CHEST));

        cells.add(new CellProperty(CellName.BOULEVARD_DES_CAPUCINES.name(), CellType.PROPERTY, 320, 44));

        cells.add(new CellProperty(CellName.GARE_SAINT_LAZARE.name(), CellType.RAILROAD, 200, 8));

        cells.add(new Cell(CellName.CHANCE.name(), CellType.CHANCE));

        cells.add(new CellProperty(CellName.AVENUE_DES_CHAMPS_ELYSEES.name(), CellType.PROPERTY, 350, 50));

        cells.add(new CellTax(CellName.IMPOT_SUR_LE_REVENU.name(), CellType.TAX, 200));

        cells.add(new CellProperty(CellName.RUE_DE_LA_PAIX.name(), CellType.PROPERTY, 400, 60));

        return board;
    }
}
