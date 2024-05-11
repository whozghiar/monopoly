package fr.monopoly.backend.Models;

import fr.monopoly.backend.Models.Enumerations.CellType;

public class CellTax extends Cell{
    private int taxAmount;

    public CellTax(String name, CellType type, int taxAmount) {
        super(name, type);
        this.taxAmount = taxAmount;
    }
}
