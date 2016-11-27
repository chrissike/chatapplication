package edu.hm.dako.chat.model;

import java.io.Serializable;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p/>
 * Nachrichtenaufbau fuer Chat-Protokoll (fuer alle Nachrichtentypen: Request,
 * Response, Event, Confirm)
 *
 * @author Mandl
 */
public class ChatPDU2 implements Serializable {

	private static final long serialVersionUID = -6172619032079227585L;
	private static final Log log = LogFactory.getLog(ChatPDU2.class);

	// Kommandos bzw. PDU-Typen
	private PduType pduType;

	// Login-Name des Clients
	private String userName;

	// Name des Clients, von dem ein Event initiiert wurde
	private String eventUserName;

	// Name des Client-Threads, der den Request absendet
	private String clientThreadName;

	// Name des Threads, der den Request im Server
	private String serverThreadName;

	// Zaehlt die uebertragenen Nachrichten eines Clients,
	// optional nutzbar fuer unsichere Transportmechanismen bearbeitet
	private long sequenceNumber;

	// Nutzdaten (eigentliche Chat-Nachricht in Textform)
	private String message;

	// Liste aller angemeldeten User
	private Vector<String> clients;

	// Zeit in Nanosekunden, die der Server fuer die komplette Bearbeitung einer
	// Chat-Nachricht benoetigt (inkl. kompletter Verteilung an alle
	// angemeldeten User).
	// Diese Zeit wird vom Server vor dem Absenden der Response eingetragen
	private long serverTime;

	// Conversation-Status aus Sicht des Servers
	private ChatPDU2Status clientStatus;

	// Fehlercode, derzeit nur 1 Fehlercode definiert
	private int errorCode;
	public final static int NO_ERROR = 0;
	public final static int LOGIN_ERROR = 1;

	// Daten zur statistischen Auswertung, die mit der Logout-Response-PDU
	// mitgesendet werden:

	// Anzahl der verarbeiteten Chat-Nachrichten des Clients
	private long numberOfReceivedChatMessages;

	// Anzahl an gesendeten Events an andere Clients
	private long numberOfSentEvents;

	// Anzahl an empfangenen Bestaetigungen der anderen Clients
	private long numberOfReceivedConfirms;

	// Anzahl verlorener bzw. nicht zugestellten Bestaetigungen anderer Clients
	private long numberOfLostConfirms;

	// Anzahl der Wiederholungen von Nachrichten (nur bei verbindungslosen
	// Transportsystemen)
	private long numberOfRetries;

	public ChatPDU2() {
		pduType = PduType.UNDEFINED;
		userName = null;
		eventUserName = null;
		clientThreadName = null;
		serverThreadName = null;
		sequenceNumber = 0;
		errorCode = NO_ERROR;
		message = null;
		serverTime = 0;
		clients = null;
		clientStatus = ChatPDU2Status.UNREGISTERED;
		numberOfReceivedChatMessages = 0;
		numberOfSentEvents = 0;
		numberOfReceivedConfirms = 0;
		numberOfLostConfirms = 0;
		numberOfRetries = 0;
	}

	public ChatPDU2(PduType cmd, Vector<String> clients) {
		this.pduType = cmd;
		this.clients = clients;
	}

	public ChatPDU2(PduType cmd, String message) {
		this.pduType = cmd;
		this.message = message;
	}

	public String toString() {

		return "\n"
				+ "ChatPdu ****************************************************************************************************"
				+ "\n" + "PduType: " + this.pduType + ", " + "\n" + "userName: " + this.userName
				+ ", " + "\n" + "eventUserName: " + this.eventUserName + ", " + "\n"
				+ "clientThreadName: " + this.clientThreadName + ", " + "\n"
				+ "serverThreadName: " + this.serverThreadName + ", " + "\n" + "errrorCode: "
				+ this.errorCode + ", " + "\n" + "sequenceNumber: " + this.sequenceNumber + "\n"
				+ "serverTime: " + this.serverTime + ", " + "\n" + "clientStatus: "
				+ this.clientStatus + "," + "\n" + "numberOfReceivedChatMessages: "
				+ this.numberOfReceivedChatMessages + ", " + "\n" + "numberOfSentEvents: "
				+ this.numberOfSentEvents + ", " + "\n" + "numberOfLostConfirms: "
				+ this.numberOfLostConfirms + ", " + "\n" + "numberOfRetries: "
				+ this.numberOfRetries + "\n" + "clients (Userliste): " + this.clients + ", "
				+ "\n" + "message: " + this.message + "\n"
				+ "**************************************************************************************************** ChatPdu"
				+ "\n";
	}

	public static void printPdu(ChatPDU2 pdu) {
		// System.out.println(pdu);
		log.debug(pdu);
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

	public void setClientThreadName(String threadName) {
		this.clientThreadName = threadName;
	}

	public void setServerThreadName(String threadName) {
		this.serverThreadName = threadName;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public void setServerTime(long time) {
		this.serverTime = time;
	}

	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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

	public String getClientThreadName() {
		return (clientThreadName);
	}

	public String getServerThreadName() {
		return (serverThreadName);
	}

	public String getMessage() {
		return (message);
	}

	public long getServerTime() {
		return (serverTime);
	}

	public long getSequenceNumber() {
		return (sequenceNumber);
	}

	public ChatPDU2Status getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(ChatPDU2Status clientStatus) {
		this.clientStatus = clientStatus;
	}

	public long getNumberOfSentEvents() {
		return (numberOfSentEvents);
	}

	public void setNumberOfSentEvents(long nr) {
		this.numberOfSentEvents = nr;
	}

	public long getNumberOfReceivedConfirms() {
		return (numberOfReceivedConfirms);
	}

	public void setNumberOfReceivedEventConfirms(long nr) {
		this.numberOfReceivedConfirms = nr;
	}

	public long getNumberOfLostConfirms() {
		return (numberOfLostConfirms);
	}

	public void setNumberOfLostEventConfirms(long nr) {
		this.numberOfLostConfirms = nr;
	}

	public long getNumberOfRetries() {
		return (numberOfRetries);
	}

	public void setNumberOfRetries(long nr) {
		this.numberOfRetries = nr;
	}

	public long getNumberOfReceivedChatMessages() {
		return (numberOfReceivedChatMessages);
	}

	public void setNumberOfReceivedChatMessages(long nr) {
		this.numberOfReceivedChatMessages = nr;
	}

	public int getErrorCode() {
		return (errorCode);
	}

	public void setErrorCode(int code) {
		this.errorCode = code;
	}

	/**
	 * Erzeugen einer Logout-Event-PDU
	 * 
	 * @param userName
	 *          Client, der Logout-Request-PDU gesendet hat
	 * @param receivedPdu
	 *          Empfangene PDU (Logout-Request-PDU)
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createLogoutEventPdu(String userName, ChatPDU2 receivedPdu) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setUserName(userName);
		pdu.setEventUserName(userName);
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setClientThreadName(receivedPdu.getClientThreadName());
		pdu.setClientStatus(ChatPDU2Status.UNREGISTERING);
		return pdu;
	}

	/**
	 * Erzeugen einer Login-Event-PDU
	 * 
	 * @param userName
	 *          Client, der Login-Request-PDU gesendet hat
	 * @param receivedPdu
	 *          Empfangene PDU (Login-Request-PDU)
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createLoginEventPdu(String userName, ChatPDU2 receivedPdu) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setClientThreadName(receivedPdu.getClientThreadName());
		pdu.setUserName(userName);
		pdu.setEventUserName(receivedPdu.getUserName());
		pdu.setUserName(receivedPdu.getUserName());
		pdu.setClientStatus(ChatPDU2Status.REGISTERING);
		return pdu;
	}

	/**
	 * Erzeugen einer Login-Response-PDU
	 * 
	 * @param eventInitiator
	 *          Urspruenglicher Client, der Login-Request-PDU gesendet hat
	 * @param receivedPdu
	 *          Empfangene PDU
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createLoginResponsePdu(String eventInitiator,
			ChatPDU2 receivedPdu) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setClientThreadName(receivedPdu.getClientThreadName());
		pdu.setUserName(eventInitiator);
		pdu.setClientStatus(ChatPDU2Status.REGISTERED);
		return pdu;
	}

	/**
	 * Erzeugen einer Chat-Message-Event-PDU
	 * 
	 * @param userName
	 *          Client, der Chat-Message-Request-PDU gesendet hat
	 * @param receivedPdu
	 *          (Chat-Message-Request-PDU)
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createChatMessageEventPdu(String userName, ChatPDU2 receivedPdu) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setClientThreadName(receivedPdu.getClientThreadName());
		pdu.setUserName(userName);
		pdu.setEventUserName(receivedPdu.getUserName());
		pdu.setSequenceNumber(receivedPdu.getSequenceNumber());
		pdu.setClientStatus(ChatPDU2Status.REGISTERED);
		pdu.setMessage(receivedPdu.getMessage());
		return pdu;
	}

	/**
	 * Erzeugen einer Logout-Response-PDU
	 * 
	 * @param eventInitiator
	 *          Urspruenglicher Client, der Logout-Request-PDU gesendet hat
	 * @param numberOfSentEvents
	 *          Anzahl an den Client gesendeter Events
	 * @param numberOfLostEventConfirms
	 *          Anzahl verlorener EventConfirms des Clients
	 * @param numberOfReceivedEventConfirms
	 *          Anzahl empfangender EventConfirms des Clients
	 * @param numberOfRetries
	 *          Anzahl wiederholter Nachrichten
	 * @param numberOfReceivedChatMessages
	 *          Anzahl empfangender Chat-Messages des Clients
	 * @param clientThreadName
	 *          Name des Client-Threads
	 * @return Aufgebaute ChatPDU2
	 */
	public static ChatPDU2 createLogoutResponsePdu(String eventInitiator,
			long numberOfSentEvents, long numberOfLostEventConfirms,
			long numberOfReceivedEventConfirms, long numberOfRetries,
			long numberOfReceivedChatMessages, String clientThreadName) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setClientThreadName(clientThreadName);
		pdu.setClientStatus(ChatPDU2Status.UNREGISTERED);

		// Statistikdaten versorgen
		pdu.setNumberOfSentEvents(numberOfSentEvents);
		pdu.setNumberOfLostEventConfirms(numberOfLostEventConfirms);
		pdu.setNumberOfReceivedEventConfirms(numberOfReceivedEventConfirms);
		pdu.setNumberOfRetries(numberOfRetries);
		pdu.setNumberOfReceivedChatMessages(numberOfReceivedChatMessages);
		pdu.setUserName(eventInitiator);
		return pdu;
	}

	/**
	 * Erzeugen einer Chat-Message-Response-PDU
	 * 
	 * @param eventInitiator
	 *          Urspruenglicher Client, der Chat-Message-Request-PDU gesendet hat
	 * @param numberOfSentEvents
	 *          Anzahl an den Client gesendeter Events
	 * @param numberOfLostEventConfirms
	 *          Anzahl verlorener EventConfirms des Clients
	 * @param numberOfReceivedEventConfirms
	 *          Anzahl empfangender EventConfirms des Clients
	 * @param numberOfRetries
	 *          Anzahl wiederholter Nachrichten
	 * @param numberOfReceivedChatMessages
	 *          Anzahl empfangender Chat-Messages des Clients
	 * @param serverTime
	 *          Requestbearbeitungszeit im Server
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createChatMessageResponsePdu(String eventInitiator,
			long numberOfSentEvents, long numberOfLostEventConfirms,
			long numberOfReceivedEventConfirms, long numberOfRetries,
			long numberOfReceivedChatMessages, String clientThreadName, long serverTime) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setServerThreadName(Thread.currentThread().getName());

		pdu.setClientThreadName(clientThreadName);
		pdu.setEventUserName(eventInitiator);
		pdu.setUserName(eventInitiator);

		pdu.setClientStatus(ChatPDU2Status.REGISTERED);

		// Statistikdaten versorgen
		pdu.setSequenceNumber(numberOfReceivedChatMessages);
		pdu.setNumberOfSentEvents(numberOfSentEvents);
		pdu.setNumberOfLostEventConfirms(numberOfLostEventConfirms);
		pdu.setNumberOfReceivedEventConfirms(numberOfReceivedEventConfirms);
		pdu.setNumberOfRetries(numberOfRetries);
		pdu.setNumberOfReceivedChatMessages(numberOfReceivedChatMessages);

		// Serverbearbeitungszeit
		pdu.setServerTime(serverTime);
		return pdu;
	}

	/**
	 * Erzeugen einer Login-Response-PDU mit Fehlermeldung
	 * 
	 * @param pdu
	 *          Empfangene PDU
	 * @param errorCode
	 *          Fehlercode, der in der PDU uebertragen werden soll
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createLoginErrorResponsePdu(ChatPDU2 receivedPdu, int errorCode) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setClientThreadName(receivedPdu.getClientThreadName());
		pdu.setUserName(receivedPdu.getUserName());
		pdu.setClientStatus(ChatPDU2Status.UNREGISTERED);
		pdu.setErrorCode(errorCode);
		return pdu;
	}

	/**
	 * Erzeugen einer Login-Event-Confirm-PDU
	 * 
	 * @param userName
	 *          Name des Clients
	 * @param pdu
	 *          Empfangene PDU
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createLoginEventConfirm(String userName, ChatPDU2 receivedPdu) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setClientStatus(ChatPDU2Status.REGISTERED);
		pdu.setClientThreadName(Thread.currentThread().getName());
		pdu.setServerThreadName(receivedPdu.getServerThreadName());
		pdu.setUserName(userName);
		pdu.setEventUserName(receivedPdu.getEventUserName());
		return pdu;
	}

	/**
	 * Erzeugen einer Logout-Event-Confirm-PDU
	 * 
	 * @param userName
	 *          Name des Clients
	 * @param pdu
	 *          Empfangene PDU
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createLogoutEventConfirm(String userName, ChatPDU2 receivedPdu) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setClientStatus(ChatPDU2Status.UNREGISTERING);
		pdu.setServerThreadName(receivedPdu.getServerThreadName());
		pdu.setUserName(userName);
		pdu.setEventUserName(receivedPdu.getEventUserName());
		return pdu;
	}

	/**
	 * Erzeugen einer Chat-Message-Event-Confirm-PDU
	 * 
	 * @param userName
	 *          Name des Clients
	 * @param pdu
	 *          Empfangene PDU
	 * @return Erzeugte PDU
	 */
	public static ChatPDU2 createChatMessageEventConfirm(String userName,
			ChatPDU2 receivedPdu) {

		ChatPDU2 pdu = new ChatPDU2();
		pdu.setPduType(PduType.UNDEFINED);
		pdu.setClientStatus(ChatPDU2Status.REGISTERED);
		pdu.setClientThreadName(Thread.currentThread().getName());
		pdu.setServerThreadName(receivedPdu.getServerThreadName());
		pdu.setUserName(userName);
		pdu.setEventUserName(receivedPdu.getEventUserName());
		return pdu;
	}
}