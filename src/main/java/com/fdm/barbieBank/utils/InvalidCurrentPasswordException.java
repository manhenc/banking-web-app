package com.fdm.barbieBank.utils;

public class InvalidCurrentPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidCurrentPasswordException() {
        super();
    }

    public InvalidCurrentPasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidCurrentPasswordException(final String message) {
        super(message);
    }

    public InvalidCurrentPasswordException(final Throwable cause) {
        super(cause);
    }
	
}
