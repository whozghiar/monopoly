package fr.monopoly.backend.Exceptions.GameExceptions;

public class GameTurnAdvanceException extends RuntimeException{
    public GameTurnAdvanceException(String message) {
        super(message);
    }
}
