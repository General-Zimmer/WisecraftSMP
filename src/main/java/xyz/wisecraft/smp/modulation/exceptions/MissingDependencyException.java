package xyz.wisecraft.smp.modulation.exceptions;

public class MissingDependencyException extends RuntimeException {

    public MissingDependencyException() {
        super();
    }

    public MissingDependencyException(String message) {
        super(message);
    }
}
