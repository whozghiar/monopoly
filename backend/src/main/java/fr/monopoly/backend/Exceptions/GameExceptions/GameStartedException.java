package fr.monopoly.backend.Exceptions.GameExceptions;

public class GameStartedException extends RuntimeException{
    public GameStartedException(String message) {
        super(message);
    }
}
