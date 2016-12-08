package edu.hm.dako.chat.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;



/**
 * Umfasst einen fachlichen Fehlertyp und einen Liste von Fehlermeldungen. Das {@link ErrorItem} wird in *RestExceptions
 * verwendet, um bestimmte HTTP-Statuscodes auf dem Client nach fachlichen Gesichtspunkten weiter unterschieden zu
 * koennen.
 */
@XmlRootElement
public class ErrorItem {

	/** Enumeration von Exception-Typen der REST API des ServiceTemplate-Service. */
	private ErrorType errorType;

	/** Liste von Fehlermeldungen. */
	private List<String> errorMessages;



	/**
	 * Default-Konstruktor.
	 */
	public ErrorItem() {
	}



	/**
	 * Convinience-Konstruktor fuer ein komplett befuelltes {@link ErrorItem}.
	 *
	 * @param errorType
	 *            Enumeration von Exception-Typen der REST API des ServiceTemplate-Service.
	 * @param errorMessages
	 *            Liste von Fehlermeldungen.
	 */
	public ErrorItem(final ErrorType errorType, final List<String> errorMessages) {
		this();
		this.errorType = errorType;
		this.errorMessages = errorMessages;
	}



	public ErrorType getErrorType() {
		return errorType;
	}



	public void setErrorType(final ErrorType errorType) {
		this.errorType = errorType;
	}



	public List<String> getErrorMessages() {
		return errorMessages;
	}



	public void setErrorMessages(final List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

}
