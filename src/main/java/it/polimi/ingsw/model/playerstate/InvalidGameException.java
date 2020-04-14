package it.polimi.ingsw.model.playerstate;

public class InvalidGameException extends RuntimeException {
    public InvalidGameException(String message) {
        super(message);
    }
}
