package fr.monopoly.backend.Models;


import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {
    @Id
    private Long id;

    private String name; // Name of the player

    private int money; // Amount of money the player has

    private int position; // Position of the player on the board

    private boolean inJail; // Whether the player is in jail or not

    private int jailTurns; // Number of turns the player has been in jail
}
