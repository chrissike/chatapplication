package edu.hm.dako.chat.server;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.server.tcp.ServerSocketInterface;
import edu.hm.dako.chat.server.user.ClientListEntry;

/**
 * <p/>
 * Simple-Chat-Server-Implementierung
 *
 * @author Peter Mandl
 */
public class SimpleChatServerImpl extends AbstractChatServer {

	private static Log log = LogFactory.getLog(SimpleChatServerImpl.class);

	// Threadpool fuer Worker-Threads
	private final ExecutorService executorService;

	// Socket fuer den Listener, der alle Verbindungsaufbauwuensche der Clients
	// entgegennimmt
	private ServerSocketInterface socket;

	/**
	 * Konstruktor
	 * 
	 * @param executorService
	 * @param socket
	 * @param serverGuiInterface
	 */
	public SimpleChatServerImpl(ExecutorService executorService,
			ServerSocketInterface socket) {
		log.debug("SimpleChatServerImpl konstruiert");
		this.executorService = executorService;
		this.socket = socket;
		counter = new SharedServerCounter();
		counter.logoutCounter = new AtomicInteger(0);
		counter.eventCounter = new AtomicInteger(0);
		counter.confirmCounter = new AtomicInteger(0);
	}

	
	//TODO Max: das ist obsolet... kann eigentlich weg!
	@Override
	public void start() {
//		Task<Void> task = new Task<Void>() {
//			
//			protected Void call() throws Exception {
//				// Clientliste erzeugen
//				clients = SharedChatClientList.getInstance();
//
//				while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
//					try {
//						// Auf ankommende Verbindungsaufbauwuensche warten
//						System.out.println(
//								"SimpleChatServer wartet auf Verbindungsanfragen von Clients...");
//
//						Connection connection = socket.accept();
//						log.debug("Neuer Verbindungsaufbauwunsch empfangen");
//
//						// Neuen Workerthread starten
//						executorService.submit(new SimpleChatWorkerThreadImpl(connection, clients,
//								counter));
//					} catch (Exception e) {
//						if (socket.isClosed()) {
//							log.debug("Socket wurde geschlossen");
//						} else {
//							log.error(
//									"Exception beim Entgegennehmen von Verbindungsaufbauwuenschen: " + e);
//							ExceptionHandler.logException(e);
//						}
//					}
//				}
//				return null;
//			}
//		};
//
//		Thread th = new Thread(task);
//		th.setDaemon(true);
//		th.start();
	}

	@Override
	public void stop() throws Exception {

		// Alle Verbindungen zu aktiven Clients abbauen
		Vector<String> sendList = clients.getClientNameList();
		for (String s : new Vector<String>(sendList)) {
			ClientListEntry client = clients.getClient(s);
			try {
				if (client != null) {
					client.getConnection().close();
					log.error("Verbindung zu Client " + client.getUserName() + " geschlossen");
				}
			} catch (Exception e) {
				log.debug(
						"Fehler beim Schliessen der Verbindung zu Client " + client.getUserName());
				ExceptionHandler.logException(e);
			}
		}

		// Loeschen der Userliste
		clients.deleteAll();
		Thread.currentThread().interrupt();
		socket.close();
		log.debug("Listen-Socket geschlossen");
		executorService.shutdown();
		log.debug("Threadpool freigegeben");

		System.out.println("SimpleChatServer beendet sich");
	}
}