package fr.monopoly.backend.Exceptions.PlayerExceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
}