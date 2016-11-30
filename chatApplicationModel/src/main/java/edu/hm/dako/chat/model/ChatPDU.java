package edu.hm.dako.chat.model;

import java.io.Serializable;
import java.util.List;

/**
 * Nachrichtenaufbau fuer Chat-Protokoll (fuer alle Nachrichtentypen: Request,
 * Response, Event, Confirm)
 */
public class ChatPDU implements Serializable, PDU {

	private static final long serialVersionUID = -6149488654340429875L;

	/**
	 * Kommandos bzw. PDU-Typen
	 */
	private PduType pduType;

	/**
	 * Login-Name des Clients
	 */
	private String userName;

	/**
	 * Name des Threads, der den Request im Server
	 */
	private String serverThreadName;

	/**
	 * Nutzdaten (eigentliche Chat-Nachricht in Textform)
	 */
	private String message;

	/**
	 * Liste aller angemeldeten User
	 */
	private List<String> clients;

	/**
	 * Zeit in Nanosekunden, die der Server fuer die komplette Bearbeitung einer
	 * Chat-Nachricht benoetigt (inkl. kompletter Verteilung an alle
	 * angemeldeten User).
	 */
	private Long serverTime;

	/**
	 * StarttimeOfClient
	 */
	private Long clientStartTime;

	/**
	 * Anzahl der Wiederholungen von Nachrichten (nur bei verbindungslosen
	 * Transportsystemen)
	 */
	private Long numberOfRetries;

	public ChatPDU(PduType type) {
		pduType = type;
	}

	public ChatPDU(String clientName, String message, PduType pdutype) {
		this.userName = clientName;
		this.pduType = pdutype;
		this.message = message;
	}

	@Override
	public String toString() {
		return "ChatPDU [pduType=" + pduType + ", userName=" + userName + ", serverThreadName=" + serverThreadName
				+ ", message=" + message + ", clients=" + clients + ", serverTime=" + serverTime + ", numberOfRetries="
				+ numberOfRetries + "]";
	}

	public synchronized void setClients(List<String> clients) {
		this.clients = clients;
	}

	public void setPduType(PduType pduType) {
		this.pduType = pduType;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public List<String> getClients() {
		return clients;
	}

	public String getUserName() {
		return userName;
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