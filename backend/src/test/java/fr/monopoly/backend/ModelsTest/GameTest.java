package fr.monopoly.backend.ModelsTest;

import fr.monopoly.backend.Models.Game;
import fr.monopoly.backend.Models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testAddPlayer() {
        Player player = new Player();
        player.setName("Test Player");
        game.getPlayers().add(player);
        assertTrue(game.getPlayers().contains(player));
    }

    @Test
    public void testStartGame() {
        game.setStarted(true);
        assertTrue(game.isStarted());
    }

    @Test
    public void testEndGame() {
        game.setOver(true);
        assertTrue(game.isOver());
    }

}