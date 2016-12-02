package edu.hm.dako.chat.rest;

/**
 * Diese Exception wird geworfen, wenn beim Aufruf auf der Server-Seite ein technischer Fehler aufgetreten wird.
 */
public class TechnicalRestException extends RuntimeException {

	private static final long serialVersionUID = 1L;



	/**
	 * Konstruktor fuer die Exception. Die Exception enthaelt die Fehlermeldung.
	 *
	 * @param message
	 *            Textmeldung mit dem Grund fuer den Fehler.
	 */
	public TechnicalRestException(final String message) {
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
	public TechnicalRestException(final String message, final Exception cause) {
		super(message, cause);
	}

}