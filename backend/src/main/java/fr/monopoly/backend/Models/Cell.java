package fr.monopoly.backend.Models;

import fr.monopoly.backend.Models.Enumerations.CellType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    private String name;
    private CellType type;
}
