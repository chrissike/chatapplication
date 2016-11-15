package edu.hm.dako.chat.client.ui;

import java.net.URISyntaxException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.communication.jms.JmsConsumer;
import edu.hm.dako.chat.client.communication.jms.JmsProducer;
import edu.hm.dako.chat.client.communication.rest.MessagingHandler;
import edu.hm.dako.chat.client.communication.rest.MessagingHandlerImpl;
import edu.hm.dako.chat.common.ChatPDU;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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
	private ListView<String> chatList;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private ScrollPane chatPane;
	@FXML
	private ComboBox<String> teilnehmerListe;
	
	private ClientFxGUI appController;

	@FXML
	public void handleEnterPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			btnSubmit_OnAction();
		}
	}

	public void setAppController(ClientFxGUI appController) {

		this.appController = appController;
		updateTeilnehmerListe();

		this.teilnehmerListe.setItems(this.appController.getModel().users);

		chatList.setItems(this.appController.getModel().chats);
		btnSubmit.disableProperty().bind(appController.getModel().block);
		
		try {
			new JmsConsumer().initJmsConsumer();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	
	public void updateTeilnehmerListe() {
		ObservableList<String> userlist = this.appController.getModel().users;
		if(userlist == null) {
			teilnehmerListe.setPromptText("Hallo " + this.appController.getModel().getUserName() + 
					" (aktuelle Teilnehmerzahl: " + 1 + " )");
		} else {
			teilnehmerListe.setPromptText("Hallo " + this.appController.getModel().getUserName() + 
					" (aktuelle Teilnehmerzahl: " + userlist.size() + " )");			
		}
		
		if(userlist.size() <= 1) {
			teilnehmerListe.setDisable(true);
			teilnehmerListe.setStyle("-fx-opacity: 1;");
		}
	}

	
	public void btnLogOut_OnAction() {
		MessagingHandler handler = null;
		Integer port = Integer.valueOf(this.appController.getModel().getPort().getValue());
		try {
			handler = new MessagingHandlerImpl(this.appController.getModel().getAddress().getValue(), port);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		handler.logout(appController.getModel().getUserName());
		try {
			ClientFxGUI.instance.stage.close();
			ClientFxGUI.instance.start(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		Platform.runLater(new Runnable() {
			public void run() {
				txtChatMessage.setText("");
			}
		});
	}
}
