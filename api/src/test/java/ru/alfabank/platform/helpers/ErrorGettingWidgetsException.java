package ru.alfabank.platform.helpers;

public class ErrorGettingWidgetsException extends Exception {
	/**
	 * Constructs a new exception with the specified detail message.  The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
	 *                method.
	 */
	public ErrorGettingWidgetsException(String message) {
		super(message);
	}
}
