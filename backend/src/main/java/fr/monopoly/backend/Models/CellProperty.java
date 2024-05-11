package fr.monopoly.backend.Models;

import fr.monopoly.backend.Models.Enumerations.CellType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CellProperty extends Cell {

    private int price;
    private int rent;
    private int houseCount;
    private int hotelCount;

    private boolean isMortgaged;

    public CellProperty(String name, CellType type, int price, int rent) {
        super(name, type);
        this.price = price;
        this.rent = rent;
        this.houseCount = 0;
        this.hotelCount = 0;
        this.isMortgaged = false;
    }
}