package ru.alfabank.platform.helpers;

public class ErrorWhileGettingWidgetsException extends Exception {

  /**
   * Constructs a new exception with the specified detail message.
   * The cause is not initialized, and may subsequently be
   * initialized by a call to {@link #initCause}.
   *
   * @param message the detail message.
   */
  public ErrorWhileGettingWidgetsException(final String message) {
    super(message);
  }
}
