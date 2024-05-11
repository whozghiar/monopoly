package fr.monopoly.backend.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Board {
    private Set<Cell> cells = new LinkedHashSet<>(); // Liste de toutes les cases du plateau
}

