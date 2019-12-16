package ru.alfabank.platform.helpers;

public class NoTestDataException extends Exception {

	/**
	 * Constructs a new exception with {@code null} as its detail message. The cause is not initialized, and may
	 * subsequently be initialized by a call to {@link #initCause}.
	 */
	public NoTestDataException() {
		super("No usable test data on the stand.");
	}
}
