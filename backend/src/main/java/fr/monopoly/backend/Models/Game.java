package fr.monopoly.backend.Models;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Game {

    @Id
    private Long id;
    private Set<Player> players = new HashSet<Player>(); // List of players in the game
    private boolean isStarted; // Whether the game has started or not
    private boolean isOver; // Whether the game is over or not
    private Long currentPlayer; // Index of the current player in the list of players
}