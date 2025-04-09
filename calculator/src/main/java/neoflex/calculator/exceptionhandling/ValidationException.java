package neoflex.calculator.exceptionhandling;

public class ValidationException extends  RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}