package edu.hm.dako.chat.client.communication.rest;

public interface MessagingHandler {

	/**
	 * Login in die Chat-Anwendung
	 * @param anmeldename
	 * @param resource
	 * @param uri
	 * @return
	 */
	public Boolean login(final String anmeldename, String resource, String uri);
	
	/**
	 * Logout von der Chat-Anwendung
	 * @param anmeldename
	 * @param resource
	 * @param uri
	 * @return
	 */
	public Boolean logout(final String anmeldename, String resource, String uri);
	
}