package kolokwium.jaz25535nbp.exceptions;

public class WrongInputException extends RuntimeException{
    public WrongInputException(String errorMessage) {
        super(errorMessage);
    }
}
