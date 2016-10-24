package edu.hm.dako.chat.client.ui;

import java.io.IOException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.communication.jms.JmsProducer;
import edu.hm.dako.chat.common.ChatPDU;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controller fuer Chat-GUI
 * 
 * @author Paul Mandl
 *
 */
@SuppressWarnings("restriction")
public class LoggedInGuiController {

	private static Log log = LogFactory.getLog(LoggedInGuiController.class);

	@FXML
	private Button btnSubmit;
	@FXML
	private TextField txtChatMessage;
	@FXML
	private ListView<String> usersList;
	@FXML
	private ListView<String> chatList;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private ScrollPane chatPane;

	private ClientFxGUI appController;

	@FXML
	public void handleEnterPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			btnSubmit_OnAction();
		}
	}

	public void setAppController(ClientFxGUI appController) {

		this.appController = appController;

		usersList.maxWidthProperty().bind(scrollPane.widthProperty().subtract(2));
		usersList.minWidthProperty().bind(scrollPane.widthProperty().subtract(2));
		usersList.maxHeightProperty().bind(scrollPane.heightProperty().subtract(2));
		usersList.minHeightProperty().bind(scrollPane.heightProperty().subtract(2));

		usersList.setItems(appController.getModel().users);

		chatList.maxWidthProperty().bind(chatPane.widthProperty().subtract(2));
		chatList.minWidthProperty().bind(chatPane.widthProperty().subtract(2));
		chatList.maxHeightProperty().bind(chatPane.heightProperty().subtract(2));
		chatList.minHeightProperty().bind(chatPane.heightProperty().subtract(2));

		chatList.setItems(appController.getModel().chats);

		btnSubmit.disableProperty().bind(appController.getModel().block);
	}

	public void btnLogOut_OnAction() {
		try {
			appController.getCommunicator().logout(appController.getModel().getUserName());
		} catch (IOException e) {
			log.error("Logout konnte nicht durchgefuehrt werden, Server aktiv?");
			appController.setErrorMessage("Chat-Client",
					"Abmelden beim Server nicht erfolgreich, vermutlich ist der Server nicht aktiv",
					5);
		}

		// Verbindung zum Server wird sicherheitshalber rnochmals abgebaut
		appController.getCommunicator().cancelConnection();

		System.exit(0);
	}

	public void btnSubmit_OnAction() {
		
		ChatPDU chatPdu = new ChatPDU();
		chatPdu.setUserName(appController.getModel().getUserName());
		chatPdu.setMessage(txtChatMessage.getText());
		
		JmsProducer jms = new JmsProducer();
		try {
			jms.sendMessage(chatPdu);
		} catch (NamingException e) {
			log.error(e.getStackTrace());
		} catch (JMSException e) {
			log.error(e.getStackTrace());
		}
		
//		try {
//			// Eingegebene Chat-Nachricht an Server senden
//			appController.getCommunicator().tell(appController.getModel().getUserName(),
//					txtChatMessage.getText());
//		} catch (IOException e) {
//			// Senden funktioniert nicht, Server vermutlich nicht aktiv
//			log.error("Senden konnte nicht durchgefuehrt werden, Server aktiv?");
//			appController.setErrorMessage("Chat-Client",
//					"Die Nachricht konnte nicht gesendet werden, vermutlich ist der Server nicht aktiv",
//					6);
//		}
		Platform.runLater(new Runnable() {
			public void run() {
				txtChatMessage.setText("");
			}
		});
	}
}
