package edu.hm.dako.chat.client.ui;

import java.net.URISyntaxException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.hm.dako.chat.jms.connect.JmsChatContext;
import edu.hm.dako.chat.jms.connect.JmsConsumer;
import edu.hm.dako.chat.jms.connect.JmsProducer;
import edu.hm.dako.chat.rest.MessagingHandler;
import edu.hm.dako.chat.rest.MessagingHandlerImpl;
import edu.hm.dako.chat.rest.TechnicalRestException;
import edu.hm.dako.chat.client.data.ClientModel;
import edu.hm.dako.chat.client.jms.TopicSubscriber;
import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PduType;
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

	private static final Logger log = LoggerFactory.getLogger(LoggedInGuiController.class);
	
	private JmsConsumer jmsConsumer;
	private JmsChatContext jmsContext;
	
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
		
		jmsContext = new JmsChatContext();
		try {
			jmsConsumer = new JmsConsumer();
			jmsConsumer.initJmsConsumer(new TopicSubscriber(), jmsContext);
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
		} catch (TechnicalRestException e) {
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
		
		ChatPDU chatPdu = new ChatPDU(PduType.MESSAGE);
		chatPdu.setUserName(appController.getModel().getUserName());
		chatPdu.setMessage(txtChatMessage.getText());
		
		JmsProducer<ChatPDU> jms = new JmsProducer<ChatPDU>();
		try {
			jms.sendMessage(chatPdu, jmsContext);
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
