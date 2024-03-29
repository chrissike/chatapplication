package edu.hm.dako.chat.common;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p/>
 * Nachrichtenaufbau fuer Chat-Protokoll (fuer alle Nachrichtentypen: Request,
 * Response, Event, Confirm)
 *
 * @author Mandl
 */
public class ChatPDU implements Serializable {

	private static final long serialVersionUID = -6149488654340429875L;

	// Kommandos bzw. PDU-Typen
	private PduType pduType;

	// Login-Name des Clients
	private String userName;

	// Name des Clients, von dem ein Event initiiert wurde
	private String eventUserName;

	// Name des Threads, der den Request im Server
	private String serverThreadName;

	// Nutzdaten (eigentliche Chat-Nachricht in Textform)
	private String message;

	// Liste aller angemeldeten User
	private Vector<String> clients;

	/* Zeit in Nanosekunden, die der Server fuer die komplette Bearbeitung einer
	 * Chat-Nachricht benoetigt (inkl. kompletter Verteilung an alle
	 * angemeldeten User).
	 */
	private Long serverTime;
	
	/*
	 * StarttimeOfClient 
	 */
	private Long clientStartTime;

	// Conversation-Status aus Sicht des Servers
	private ClientConversationStatus clientStatus;

	// Anzahl der Wiederholungen von Nachrichten (nur bei verbindungslosen
	// Transportsystemen)
	private Long numberOfRetries;

	public ChatPDU() {
		pduType = PduType.UNDEFINED;
		clientStatus = ClientConversationStatus.UNREGISTERED;
	}

	public ChatPDU(PduType cmd, Vector<String> clients) {
		this.pduType = cmd;
		this.clients = clients;
	}

	public ChatPDU(PduType cmd, String message) {
		this.pduType = cmd;
		this.message = message;
	}

	@Override
	public String toString() {
		return "ChatPDU [pduType=" + pduType + ", userName=" + userName + ", eventUserName=" + eventUserName
				+ ", serverThreadName=" + serverThreadName + ", message="
				+ message + ", clients=" + clients + ", serverTime=" + serverTime + ", clientStatus=" + clientStatus
				+ ", numberOfRetries=" + numberOfRetries + "]";
	}

	public void setClients(Vector<String> clients) {
		this.clients = clients;
	}

	public void setPduType(PduType pduType) {
		this.pduType = pduType;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEventUserName(String name) {
		this.eventUserName = name;
	}

	public void setServerThreadName(String threadName) {
		this.serverThreadName = threadName;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public void setServerTime(Long time) {
		this.serverTime = time;
	}

	public PduType getPduType() {
		return pduType;
	}

	public Vector<String> getClients() {
		return clients;
	}

	public String getUserName() {
		return userName;
	}

	public String getEventUserName() {
		return eventUserName;
	}

	public String getServerThreadName() {
		return (serverThreadName);
	}

	public String getMessage() {
		return (message);
	}

	public Long getServerTime() {
		return (serverTime);
	}

	public ClientConversationStatus getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(ClientConversationStatus clientStatus) {
		this.clientStatus = clientStatus;
	}

	public Long getNumberOfRetries() {
		return (numberOfRetries);
	}

	public void setNumberOfRetries(Long nr) {
		this.numberOfRetries = nr;
	}

	public Long getClientStartTime() {
		return clientStartTime;
	}

	public void setClientStartTime(Long clientStartTime) {
		this.clientStartTime = clientStartTime;
	}
	
}