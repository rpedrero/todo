package rpo.demo.todo.backend.exception;

public class JsonParsingException extends Exception {
    public JsonParsingException() {
        super();
    }

    public JsonParsingException(final String message) {
        super(message);
    }

    public JsonParsingException(final Exception e) {
        super(e);
    }
}
