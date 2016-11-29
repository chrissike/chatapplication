package edu.hm.dako.chat.client.ui;

import java.net.URISyntaxException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.communication.jms.JmsConsumer;
import edu.hm.dako.chat.client.communication.jms.JmsProducer;
import edu.hm.dako.chat.client.communication.jms.TopicSubscriber;
import edu.hm.dako.chat.client.communication.rest.MessagingHandler;
import edu.hm.dako.chat.client.communication.rest.MessagingHandlerImpl;
import edu.hm.dako.chat.client.communication.rest.TechnicalException;
import edu.hm.dako.chat.client.data.ClientModel;
import edu.hm.dako.chat.model.ChatPDU;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
 * Controller Chatclient-UI
 */
@SuppressWarnings("restriction")
public class LoggedInGuiController {

	private static Log log = LogFactory.getLog(LoggedInGuiController.class);
	
	private JmsConsumer jmsConsumer;
	
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
		initTeilnehmerListe();

		

		final String username = this.appController.getModel().getUserName();
		
		appController.getModel().users.addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c) {
				teilnehmerListe.setPromptText("Hallo " + username + 
						", weitere Teilnehmer: " + c.getList().size());
				
				
				if (c.getList().size() == 0){
					teilnehmerListe.setDisable(true);
				}
				else {
					teilnehmerListe.setDisable(false);
				}
			}
		});
		
		this.teilnehmerListe.setItems(this.appController.getModel().users);

		chatList.setItems(this.appController.getModel().chats);
		btnSubmit.disableProperty().bind(appController.getModel().block);
		
		try {
			jmsConsumer = new JmsConsumer();
			jmsConsumer.initJmsConsumer(new TopicSubscriber());
		} catch (NamingException e) {
			log.error(e.getMessage() + ", " + e.getCause());
		}
	}

	public void initTeilnehmerListe() {
		ObservableList<String> userlist = this.appController.getModel().chats;
		teilnehmerListe.setPromptText("Hallo " + this.appController.getModel().getUserName() + 
				", weitere Teilnehmer: " + userlist.size());
		if(userlist.size() == 0) {
			teilnehmerListe.setDisable(true);
			teilnehmerListe.setStyle("-fx-opacity: 1;");
		}
	}
	
	public void updateTeilnehmerListe() {
		//is this method needed if the listener can handle everything?
	}

	
	public void btnLogOut_OnAction() {
		MessagingHandler handler = null;
		Integer port = Integer.valueOf(this.appController.getModel().getPort().getValue());
		try {
			handler = new MessagingHandlerImpl(this.appController.getModel().getAddress().getValue(), port);
		} catch (URISyntaxException e) {
			log.error(e.getMessage() + ", " + e.getCause());
		}
		
		try {
		handler.logout(appController.getModel().getUserName());
		} catch (TechnicalException e) {
			System.exit(0);
		}
		
		ClientFxGUI.instance.setModel(new ClientModel());
		
		try {
			jmsConsumer.closeJmsConsumer();
		} catch (NamingException e) {
			log.error(e.getMessage() + ", " + e.getCause());
		}
		
		try {
			ClientFxGUI.instance.stage.close();
			ClientFxGUI.instance.start(new Stage());
		} catch (Exception e) {
			log.error(e.getMessage() + ", " + e.getCause());
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
			log.error(e.getMessage() + ", " + e.getCause());
		} catch (JMSException e) {
			log.error(e.getMessage() + ", " + e.getCause());
		}

		Platform.runLater(new Runnable() {
			public void run() {
				txtChatMessage.setText("");
			}
		});
	}
}
