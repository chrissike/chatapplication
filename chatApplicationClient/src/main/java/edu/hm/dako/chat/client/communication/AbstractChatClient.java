//package edu.hm.dako.chat.client.communication;
//
//import java.io.IOException;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import edu.hm.dako.chat.client.data.SharedClientData;
//import edu.hm.dako.chat.client.ui.ClientUserInterface;
//import edu.hm.dako.chat.common.ChatPDU;
//import edu.hm.dako.chat.common.ClientConversationStatus;
//import edu.hm.dako.chat.common.ExceptionHandler;
//import edu.hm.dako.chat.common.PduType;
//
///**
// * Gemeinsame Funktionalitaet fuer alle Client-Implementierungen.
// * 
// * @author Peter Mandl
// *
// */
//public abstract class AbstractChatClient implements ClientCommunication {
//
//	private static Log log = LogFactory.getLog(AbstractChatClient.class);
//
//	// Username (Login-Kennung) des Clients
//	protected String userName;
//
//	protected String threadName;
//
//	protected int localPort;
//
//	protected int serverPort;
//	protected String remoteServerAddress;
//
//	protected ClientUserInterface userInterface;
//
//
//	// Gemeinsame Daten des Clientthreads und dem Message-Listener-Threads
//	protected SharedClientData sharedClientData;
//
//	// Thread, der die ankommenden Nachrichten fuer den Client verarbeitet
//	protected Thread messageListenerThread;
//
//	/**
//	 * @param userInterface
//	 *          GUI-Interface
//	 * @param serverPort
//	 *          Port des Servers
//	 * @param remoteServerAddress
//	 *          Adresse des Servers
//	 */
//
//	public AbstractChatClient(ClientUserInterface userInterface, int serverPort,
//			String remoteServerAddress) {
//
//		this.userInterface = userInterface;
//		this.serverPort = serverPort;
//		this.remoteServerAddress = remoteServerAddress;
//
//		/*
//		 * Verbindung zum Server aufbauen
//		 */
////		try {
////			connectionFactory = getDecoratedFactory(new TcpConnectionFactory());
////			connection = connectionFactory.connectToServer(remoteServerAddress, serverPort,
////					localPort, 20000, 20000);
////		} catch (Exception e) {
////			ExceptionHandler.logException(e);
////		}
//
//		log.debug("Verbindung zum Server steht");
//
//		/*
//		 * Gemeinsame Datenstruktur aufbauen
//		 */
//		sharedClientData = new SharedClientData();
//		sharedClientData.messageCounter = new AtomicInteger(0);
//		sharedClientData.logoutCounter = new AtomicInteger(0);
//		sharedClientData.eventCounter = new AtomicInteger(0);
//		sharedClientData.confirmCounter = new AtomicInteger(0);
//		sharedClientData.messageCounter = new AtomicInteger(0);
//	}
//
//	/**
//	 * Ergaenzt ConnectionFactory um Logging-Funktionalitaet
//	 * 
//	 * @param connectionFactory
//	 *          ConnectionFactory
//	 * @return Dekorierte ConnectionFactory
//	 */
////	public static ConnectionFactory getDecoratedFactory(
////			ConnectionFactory connectionFactory) {
////		return new DecoratingConnectionFactory(connectionFactory);
////	}
//
//	public void login(String name) throws IOException {
//
//		userName = name;
//		sharedClientData.userName = name;
//		sharedClientData.status = ClientConversationStatus.REGISTERING;
//		ChatPDU requestPdu = new ChatPDU();
//		requestPdu.setPduType(PduType.LOGIN_REQUEST);
//		requestPdu.setClientStatus(sharedClientData.status);
//		Thread.currentThread().setName("Client-" + userName);
//		requestPdu.setUserName(userName);
//		try {
////			connection.send(requestPdu);
//			log.debug("Login-Request-PDU fuer Client " + userName + " an Server gesendet");
//		} catch (Exception e) {
//			throw new IOException();
//		}
//	}
//
//	public void logout(String name) throws IOException {
//
//		sharedClientData.status = ClientConversationStatus.UNREGISTERING;
//		ChatPDU requestPdu = new ChatPDU();
//		requestPdu.setPduType(PduType.LOGOUT_REQUEST);
//		requestPdu.setClientStatus(sharedClientData.status);
//		requestPdu.setUserName(userName);
//		try {
////			connection.send(requestPdu);
//			sharedClientData.logoutCounter.getAndIncrement();
//			log.debug("Logout-Request von " + requestPdu.getUserName()
//					+ " gesendet, LogoutCount = " + sharedClientData.logoutCounter.get());
//
//		} catch (Exception e) {
//			log.debug("Senden der Logout-Nachricht nicht moeglich");
//			throw new IOException();
//		}
//	}
//
//	public void tell(String name, String text) throws IOException {
//
//		ChatPDU requestPdu = new ChatPDU();
//		requestPdu.setPduType(PduType.CHAT_MESSAGE_REQUEST);
//		requestPdu.setClientStatus(sharedClientData.status);
//		requestPdu.setUserName(userName);
//		requestPdu.setMessage(text);
//		sharedClientData.messageCounter.getAndIncrement();
//		try {
////			connection.send(requestPdu);
//			log.debug("Chat-Message-Request-PDU fuer Client " + name
//					+ " an Server gesendet, Inhalt: " + text);
//		} catch (Exception e) {
//			log.debug("Senden der Chat-Nachricht nicht moeglich");
//			throw new IOException();
//		}
//	}
//
//	public void cancelConnection() {
//		try {
////			connection.close();
//		} catch (Exception e) {
//			ExceptionHandler.logException(e);
//		}
//	}
//}