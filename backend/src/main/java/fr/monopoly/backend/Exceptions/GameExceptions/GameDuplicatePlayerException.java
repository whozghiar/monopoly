package fr.monopoly.backend.Exceptions.GameExceptions;

public class GameDuplicatePlayerException extends RuntimeException{
    public GameDuplicatePlayerException(String message) {
        super(message);
    }
}
