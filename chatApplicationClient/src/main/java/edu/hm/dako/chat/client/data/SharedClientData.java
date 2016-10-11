package edu.hm.dako.chat.client.data;

import java.util.concurrent.atomic.AtomicInteger;

import edu.hm.dako.chat.common.ClientConversationStatus;

/**
 * Gemeinsame genutzte Daten, die sich der Chat-Client-Thread und die
 * Message-Processing-Threads teilen
 * 
 * @author Peter Mandl
 *
 */
public class SharedClientData {

	// Loginname des Clients
	public String userName;

	// Aktueller Zustand des Clients
	public ClientConversationStatus status;

	// Zaehler fuer gesendete Chat-Nachrichten des Clients
	public AtomicInteger messageCounter;

	// Zaehler fuer Logouts, empfangene Events und Confirms aller Clients fuer
	// Testausgaben
	public AtomicInteger logoutCounter;
	public AtomicInteger eventCounter;
	public AtomicInteger confirmCounter;
}
