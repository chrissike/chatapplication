package edu.hm.dako.chat.rest;

public interface MessagingHandler {

	/**
	 * Login in die Chat-Anwendung
	 * @param anmeldename
	 * @param resource
	 * @param uri
	 * @return
	 */
	public Boolean login(final String anmeldename) throws TechnicalRestException;
	
	/**
	 * Logout von der Chat-Anwendung
	 * @param anmeldename
	 * @param resource
	 * @param uri
	 * @return
	 */
	public Boolean logout(final String anmeldename) throws TechnicalRestException;
	
	/**
	 * Logout aller Clients (für Benchmarkzwecke zu verwenden)
	 * @return
	 * @throws TechnicalRestException
	 */
	public Boolean logoutAll() throws TechnicalRestException;
	
}
