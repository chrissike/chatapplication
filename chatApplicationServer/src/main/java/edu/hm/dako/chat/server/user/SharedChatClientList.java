package edu.hm.dako.chat.server.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Liste aller angemeldeten Clients. Diese Liste wird im Server als Singleton
 * verwaltet (darf nur einmal erzeugt werden). Alle Worker-Threads im Server
 * nutzen diese Liste.
 *
 * Genereller Hinweis: Zur Umgehung von ConcurrentModificationExceptions wird
 * bei der Iteration durch Listen generell eine Kopie der Liste angelegt.
 *
 * @author Peter Mandl
 *
 */
public class SharedChatClientList {

	private static Log log = LogFactory.getLog(SharedChatClientList.class);

	/**
	 * Liste aller eingeloggten Clients
	 */
	private static ConcurrentHashMap<String, ClientListEntry> clients;

	private static SharedChatClientList instance;


	/**
	 * Thread-sicheres Erzeugen einer Instanz der Liste
	 *
	 * @return Referenz auf die erzeugte Liste
	 */
	public static synchronized SharedChatClientList getInstance() {
		if (SharedChatClientList.instance == null) {
			SharedChatClientList.instance = new SharedChatClientList();
			// Clientliste nur einmal erzeugen
			clients = new ConcurrentHashMap<String, ClientListEntry>();
		}
		return SharedChatClientList.instance;
	}

	/**
	 * Client auslesen
	 *
	 * @param userName
	 *            Name des Clients
	 * @return Referenz auf den gesuchten Client
	 */
	public synchronized ClientListEntry getClient(String userName) {

		return clients.get(userName);
	}

	/**
	 * Stellt eine Liste aller Namen der eingetragenen Clients bereit
	 *
	 * @return Vektor mit allen Namen der eingetragenen Clients
	 */
	public synchronized List<String> getClientNameList() {

		return new ArrayList<>(clients.keySet());
	}

	/**
	 * Prueft, ob ein Client in der Userliste ist
	 *
	 * @param userName
	 *            Name des Clients
	 * @return ob Client in der Liste existiert
	 */
	public synchronized boolean existsClient(String userName) {

		return userName != null && clients.containsKey(userName);
	}

	/**
	 * Legt einen neuen Client an
	 *
	 * @param userName
	 *            Name des neuen Clients
	 * @param client
	 *            Client-Daten
	 */
	public synchronized void createClient(String userName, ClientListEntry client) {
		clients.put(userName, client);
	}

	/**
	 * Loescht einen Client zwangsweise inkl. aller Einträge in Wartelisten.
	 *
	 * @param userName
	 *            Name des Clients
	 */
	public synchronized void deleteClientWithoutCondition(String userName) {
		clients.remove(userName);
		log.debug("Client  " + userName + " entfernt");
	}

	/**
	 * Laenge der Liste ausgeben
	 *
	 * @return Laenge der Liste
	 */
	public synchronized long size() {

		return clients.size();
	}
}
