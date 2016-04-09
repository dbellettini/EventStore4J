package eu.bellettini.eventstore4j.write;

public class ConsistencyException extends EventStoreException {
    public ConsistencyException(String message) {
        super(message);
    }

    public ConsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsistencyException(Throwable cause) {
        super(cause);
    }
}
