package fr.monopoly.backend.Exceptions.GameExceptions;

public class GameFullException extends RuntimeException{
    public GameFullException(String message) {
        super(message);
    }
}
