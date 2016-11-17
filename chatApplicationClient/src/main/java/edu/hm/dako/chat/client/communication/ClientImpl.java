//package edu.hm.dako.chat.client.communication;
//
//import edu.hm.dako.chat.client.ui.ClientUserInterface;
//import edu.hm.dako.chat.common.ExceptionHandler;
//import edu.hm.dako.chat.common.SystemConstants;
//
///**
// * <p/>
// * Verwaltet eine Verbindung zum Server.
// *
// * @author Mandl
// */
//public class ClientImpl extends AbstractChatClient {
//
//	/**
//	 * Konstruktor
//	 * 
//	 * @param userInterface
//	 *            Schnittstelle zum User-Interface
//	 * @param serverPort
//	 *            Portnummer des Servers
//	 * @param remoteServerAddress
//	 *            IP-Adresse/Hostname des Servers
//	 */
//
//	public ClientImpl(ClientUserInterface userInterface, int serverPort, String remoteServerAddress,
//			String serverType) {
//
//		super(userInterface, serverPort, remoteServerAddress);
//		this.serverPort = serverPort;
//		this.remoteServerAddress = remoteServerAddress;
//
//		Thread.currentThread().setName("Client");
//		threadName = Thread.currentThread().getName();
//
//		try {
//			if (serverType.equals(SystemConstants.IMPL_TCP_ADVANCED)) {
//				// TODO Advanced TCP Server erzeugen
//			} else {
//				// Simple TCP Server erzeugen
//				messageListenerThread = new SimpleMessageListenerThreadImpl(userInterface, sharedClientData);
//			}
//			messageListenerThread.start();
//		} catch (Exception e) {
//			ExceptionHandler.logException(e);
//		}
//	}
//}