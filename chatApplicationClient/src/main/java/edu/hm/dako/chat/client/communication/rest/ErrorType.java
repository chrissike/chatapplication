package edu.hm.dako.chat.client.communication.rest;

/**
 * Enumeration von Exception-Typen der REST API für Template.<br/>
 * <br/>
 * Hier werden Exception-Typen definiert, durch die ein bestimmter HTTP-Statuscode auf dem Client nach fachlichen
 * Gesichtspunkten weiter unterschieden werden kann.<br/>
 * <br/>
 * Beispiel:<br/>
 * NOT_FOUND wird unterteilt in ...
 */
public enum ErrorType {

	/** Technischer Fehler. */
	INTERNAL_SERVER_ERROR,

	/** Validierungsfehler in ServiceTemplate. */
	VALIDATION_ERROR,

	/** Evil Example Error. */
	EVIL_SAMPLE_ERROR;

}
