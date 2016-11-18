package edu.hm.dako.chat.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.data.ClientModel;
import edu.hm.dako.chat.common.ExceptionHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Chat-GUI
 * 
 * @author Paul Mandl
 *
 */
@SuppressWarnings("restriction")
public class ClientFxGUI extends Application {

	private static Log log = LogFactory.getLog(ClientFxGUI.class);

	protected Stage stage;
	private ClientModel model = new ClientModel();
	public static ClientFxGUI instance;
	private LoggedInGuiController lc;

	
	public static void main(String[] args) {
		launch(args);
	}

	
	/**
	 * Diese Methode wird von Java FX bei Aufruf der launch-Methode implizit
	 * aufgerufen
	 */
	public void start(Stage primaryStage) throws Exception {
		ClientFxGUI.instance = this;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/LogInGui.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 280, 320);
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
		LogInGuiController lc = (LogInGuiController) loader.getController();
		lc.setAppController(this);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Anmelden");
		primaryStage.setResizable(false);
		stage = primaryStage;
		primaryStage.show();
	}

	
	/**
	 * Benutzeroberflaeche fuer Chat erzeugen
	 */
	public void createNextGui() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoggedInGui.fxml"));
			final Parent root = loader.load();
			lc = (LoggedInGuiController) loader.getController();
			lc.setAppController(this);
			Platform.runLater(new Runnable() {

				public void run() {
					Scene scene = new Scene(root, 490, 542);
					scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
					stage.setScene(scene);
					stage.setTitle("Chatapp");
					stage.setResizable(false);
				}
			});
		} catch (Exception e) {
			ExceptionHandler.logException(e);
		}
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			public void handle(WindowEvent event) {
				lc.btnLogOut_OnAction();
			}
		});
	}

	public void setUserList(Vector<String> userList) {
		log.info("Update der Teilnehmerliste gestartet.");
		final List<String> users = new ArrayList<String>();
		for (String user : userList) {
			if (!user.equals(getModel().getUserName())) {
				users.add(user);
			}
			Platform.runLater(new Runnable() {
				public void run() {
					getModel().users.setAll(users);
					log.info(users);
				}
			});
		}
	}

	public void setMessageLine(String sender, String message) {
		log.info("Update der Nachrichtenliste gestartet.");
		final String messageText;
		if (sender.equals(getModel().getUserName())) {
			messageText = " ich: " + message; //StringUtils.leftPad("", 30, ' ')
		} else {
			messageText = sender + ": " + message;
		}
		Platform.runLater(new Runnable() {
			public void run() {
				getModel().chats.add(messageText);
			}
		});
	}
	

	public ClientModel getModel() {
		return model;
	}


	public void setLock(boolean lock) {
		getModel().block.set(lock);
	}

	public boolean getLock() {
		return false;
	}

	public boolean isTestAborted() {
		return false;
	}

	public void setSessionStatisticsCounter(long numberOfSentEvents, long numberOfReceivedConfirms,
			long numberOfLostConfirms, long numberOfRetries, long numberOfReceivedChatMessages) {
	}

	public void setErrorMessage(final String sender, final String errorMessage, final long errorCode) {
		log.debug("errorMessage: " + errorMessage);
		Platform.runLater(new Runnable() {

			public void run() {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Es ist ein Fehler im " + sender + " aufgetreten");
				alert.setHeaderText("Fehlerbehandlung (Fehlercode = " + errorCode + ")");
				alert.setContentText(errorMessage);
				alert.showAndWait();
			}
		});
	}

	public void loginComplete() {
		log.debug("Login erfolreich");
		createNextGui();
	}

	public void logoutComplete() {
		log.debug("Abnmeldung durchgefuehrt");
	}
}
