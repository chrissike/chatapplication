package edu.hm.dako.chat.client.communication.rest;

/**
 * Diese Exception wird geworfen, wenn beim Aufruf auf der Server-Seite ein fachlicher Fehler aufgetreten wird.
 */
public class NonTechnicalException extends RuntimeException {

	private static final long serialVersionUID = 1L;



	/**
	 * Konstruktor fuer die Exception. Die Exception enthaelt die Fehlermeldung.
	 *
	 * @param message
	 *            Textmeldung mit dem Grund fuer den Fehler.
	 */
	public NonTechnicalException(final String message) {
		super(message);
	}



	/**
	 * Konstruktor fuer die Exception. Die Exception enthaelt die Fehlermeldung und eine inner Exception.
	 *
	 * @param message
	 *            Textmeldung mit dem Grund fuer den Fehler.
	 * @param cause
	 *            Innere Exception mit dem Grund fuer diese Exception.
	 */
	public NonTechnicalException(final String message, final Exception cause) {
		super(message, cause);
	}

}