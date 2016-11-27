package edu.hm.dako.chat.server.user;

/**
 * Eintrag in der serverseitigen Clientliste zur Verwaltung der angemeldeten
 * User.
 */
public class ClientListEntry {

	/**
	 * Login-Name des Clients
	 */
	private String userName;


	public ClientListEntry(String userName) {
		this.userName = userName;
	}

	public synchronized void setUserName(String userName) {
		this.userName = userName;
	}

	public synchronized String getUserName() {
		return userName;
	}
}
