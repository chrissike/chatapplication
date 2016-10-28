package edu.hm.dako.chat.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ImplementationType;
import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.LoggingConnectionDecorator;
import edu.hm.dako.chat.server.tcp.ServerSocketInterface;
import edu.hm.dako.chat.server.tcp.TcpServerSocket;

/**
 * Uebernimmt die Konfiguration und Erzeugung bestimmter Server-Typen. Siehe
 * {@link edu.hm.dako.chat.benchmarking.echo.benchmarking.UserInterfaceInputParameters.ImplementationType}
 * 
 * @author Peter Mandl
 */
public final class ServerFactory {
	private static Log log = LogFactory.getLog(ServerFactory.class);

	private ServerFactory() {
	}

	/**
	 * Erzeugt einen Chat-Server
	 * 
	 * @param implType
	 *            Implementierungytyp des Servers
	 * @param serverPort
	 *            Listenport
	 * @param sendBufferSize
	 *            Groesse des Sendepuffers in Byte
	 * @param receiveBufferSize
	 *            Groesse des Empfangspuffers in Byte
	 * @param serverGuiInterface
	 *            Referenz auf GUI fuer Callback
	 * @return
	 * @throws Exception
	 */
	public static ChatServerInterface getServer(ImplementationType implType, int serverPort, int sendBufferSize,
			int receiveBufferSize) throws Exception {
		log.debug("ChatServer (" + implType.toString() + ") wird gestartet, Serverport: " + serverPort
				+ ", Sendepuffer: " + sendBufferSize + ", Empfangspuffer: " + receiveBufferSize);
		System.out.println("ChatServer (" + implType.toString() + ") wird gestartet, Listen-Port: " + serverPort
				+ ", Sendepuffer: " + sendBufferSize + ", Empfangspuffer: " + receiveBufferSize);

		switch (implType) {

		case TCPSimpleImplementation:

			try {
				TcpServerSocket tcpServerSocket = new TcpServerSocket(serverPort, sendBufferSize, receiveBufferSize);
				return null; // new SimpleChatServerImpl(Executors.newCachedThreadPool(),
						//getDecoratedServerSocket(tcpServerSocket), serverGuiInterface);
			} catch (Exception e) {
				throw new Exception(e);
			}

		case TCPAdvancedImplementation:

			// TODO fuer Advanced Chat-Protokoll

		default:
			System.out.println("Dezeit nur TCP implementiert!");
			throw new RuntimeException("Unknown type: " + implType);
		}
	}

	private static ServerSocketInterface getDecoratedServerSocket(ServerSocketInterface serverSocket) {
		return new DecoratingServerSocket(serverSocket);
	}

	/**
	 * Dekoriert Server-Socket mit Logging-Funktionalitaet
	 * 
	 * @author mandl
	 *
	 */
	private static class DecoratingServerSocket implements ServerSocketInterface {

		private final ServerSocketInterface wrappedServerSocket;

		DecoratingServerSocket(ServerSocketInterface wrappedServerSocket) {
			this.wrappedServerSocket = wrappedServerSocket;
		}

		public Connection accept() throws Exception {
			return new LoggingConnectionDecorator(wrappedServerSocket.accept());
		}

		public void close() throws Exception {
			wrappedServerSocket.close();
		}

		public boolean isClosed() {
			return wrappedServerSocket.isClosed();
		}
	}
}
