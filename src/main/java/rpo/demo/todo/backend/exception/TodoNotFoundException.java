package rpo.demo.todo.backend.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super();
    }

    public TodoNotFoundException(final String message) {
        super(message);
    }
}
