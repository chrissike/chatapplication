package edu.hm.dako.chat.server.user;

import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.server.user.ClientListEntry;

/**
 * Liste aller angemeldeten Clients. Diese Liste wird im Server als Singleton
 * verwaltet (darf nur einmal erzeugt werden). Alle Worker-Threads im Server
 * nutzen diese Liste.
 *
 * Die Liste wird als HashMap organisiert. Als Schluessel wird der Username von
 * Clients verwendet.
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
	public synchronized Vector<String> getClientNameList() {

		Vector<String> clientNameList = new Vector<String>();
		for (String s : new HashSet<String>(clients.keySet())) {
			clientNameList.add(s);
		}
		return clientNameList;
	}

	/**
	 * Prueft, ob ein Client in der Userliste ist
	 * 
	 * @param userName
	 *            Name des Clients
	 * @return
	 */
	public synchronized boolean existsClient(String userName) {

		if (userName != null) {
			if (!clients.containsKey(userName)) {
				log.debug("User nicht in Clientliste: " + userName);
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
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

	/**
	 * Ausgeben der aktuellen Clientliste einschliesslich der Wartelisten der
	 * Clients
	 */
	public String printClientList() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Clientliste mit zugehoerigen Wartelisten: ");

		if (clients.isEmpty()) {
			stringBuilder.append(" leer\n");
		} else {
			stringBuilder.append("\n");
			for (String s : new HashSet<String>(clients.keySet())) {
				ClientListEntry client = clients.get(s);
				stringBuilder.append(client.getUserName() + ", ");
				stringBuilder.append(client.getWaitList() + "\n");
			}
		}
		return stringBuilder.toString();
	}
}
