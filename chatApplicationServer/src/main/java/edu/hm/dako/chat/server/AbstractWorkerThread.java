package edu.hm.dako.chat.server;

import java.util.concurrent.atomic.AtomicInteger;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.user.SharedChatClientList;

/**
 * Abstrakte Klasse mit Basisfunktionalitaet fuer serverseitige Worker-Threads
 * 
 * @author Peter Mandl
 *
 */
public abstract class AbstractWorkerThread extends Thread {

	// Kennzeichen zum Beenden des Worker-Threads
	protected boolean finished = false;

	// Username des durch den Worker-Thread bedienten Clients
	protected String userName = null;

	// Client-Threadname
	protected String clientThreadName = null;

	// Startzeit fuer die Serverbearbeitungszeit
	protected long startTime;

	// Gemeinsam fuer alle Workerthreads verwaltete Liste aller eingeloggten
	// Clients
	protected SharedChatClientList clients;

	// Referenzen auf globale Zaehler fuer Testausgaben
	protected AtomicInteger logoutCounter;
	protected AtomicInteger eventCounter;
	protected AtomicInteger confirmCounter;

	public AbstractWorkerThread(SharedChatClientList clients) {
		this.clients = clients;
	}

	/**
	 * Senden eines Login-List-Update-Event an alle angemeldeten Clients
	 * 
	 * @param pdu
	 *          Zu sendende PDU
	 */
	protected abstract void sendLoginListUpdateEvent(ChatPDU pdu);

	/**
	 * Aktion fuer die Behandlung ankommender Login-Requests: Neuen Client anlegen
	 * und alle Clients informieren
	 * 
	 * @param receivedPdu
	 *          Empfangene PDU
	 */
	public abstract void loginRequestAction(ChatPDU receivedPdu);

	/**
	 * Aktion fuer die Behandlung ankommender Logout-Requests: Alle Clients
	 * informieren, Response senden und Client loeschen
	 * 
	 * @param receivedPdu
	 *          Empfangene PDU
	 */
	protected abstract void logoutRequestAction(ChatPDU receivedPdu);

	/**
	 * Aktion fuer die Behandlung ankommender ChatMessage-Requests: Chat-Nachricht
	 * an alle Clients weitermelden
	 * 
	 * @param receivedPdu
	 *          Empfangene PDU
	 */
	protected abstract void chatMessageRequestAction(ChatPDU receivedPdu);

	/**
	 * Aktion fuer die Behandlung ankommender ChatMessageConfirm-PDUs
	 * 
	 * @param receivedPdu
	 */

	/**
	 * Verarbeitung einer ankommenden Nachricht eines Clients (Implementierung des
	 * serverseitigen Chat-Zustandsautomaten)
	 * 
	 * @throws Exception
	 */
	protected abstract void handleIncomingMessage() throws Exception;
}
