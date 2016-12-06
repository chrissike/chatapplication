package edu.hm.dako.chat.client.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controller fuer Login-GUI
 * 
 * @author Paul Mandl
 *
 */
@SuppressWarnings("restriction")
public class LogInGuiController implements Initializable {

//	private static final Logger log = LoggerFactory.getLogger(LogInGuiController.class);

	private String userName;

	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtServername;
	@FXML
	private TextField txtServerPort;
	@FXML
	private Button loginButton;

	private ClientFxGUI appController;

	private static final Pattern IPV6_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
	private static final Pattern IPV4_PATTERN = Pattern
			.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");

	public void setAppController(ClientFxGUI clientFxGUI) {
		this.appController = clientFxGUI;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void handleKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if(!performLogin()) {
//				log.error("Login war leider nicht erfolgreich.");				
			}
		}
	}

	/**
	 * Login-Eingaben entgegennehmen, pruefen und Anmeldung beim Server
	 * durchfuehren
	 */
	public Boolean performLogin() {
		Boolean inputOk = false;
		if (txtServerPort != null || txtUsername != null || txtUsername != null) {
			inputOk = checkCredentials();
		} else {
			appController.setErrorMessage("Chat-Client", "Benutzername fehlt", 1);
		}

		Boolean success = false;
		if (inputOk) {
			appController.getModel().setUserName(this.userName);
			appController.getModel().setAddress(this.txtServername.getText());
			appController.getModel().setPort(txtServerPort.getText());
		}
		
		success = ClientFxGUI.instance.doLogin();
		return success;
	}

	private Boolean checkCredentials() {
		Boolean credentialsOk = true;
		// Benutzername pruefen
		this.userName = txtUsername.getText	();
		if (this.userName.isEmpty() == true) {
			appController.setErrorMessage("Chat-Client", "Benutzername fehlt", 1);
			credentialsOk = false;
		}

		// IP prüfen
		if (ipCorrect() != true) {
			appController.setErrorMessage("Chat-Client", "IP-Adresse nicht korrekt, z.B. 127.0.0.1!", 3);
		}

		// Serverport pruefen
		if (txtServerPort.getText().matches("[0-9]+")) {
			Integer serverPort = Integer.parseInt(txtServerPort.getText());
			if ((serverPort < 1) || (serverPort > 65535)) {
				appController.setErrorMessage("Chat-Client", "Serverport ist nicht im Wertebereich von 1 bis 65535!",
						2);
			}
		} else {
			appController.setErrorMessage("Chat-Client", "Serverport ist nicht numerisch!", 3);
		}
		return credentialsOk;
	}

	public String getUserName() {
		return userName;
	}

	public void exitButtonReaction() {
		System.exit(0);
	}

	/**
	 * Pruefen, ob IP-Adresse korrekt ist
	 * 
	 * @return true - korrekt, false - nicht korrekt
	 */
	private Boolean ipCorrect() {
		String ip = txtServername.getText();
		if (ip.equals("localhost")) {
			return true;
		} else if (IPV6_PATTERN.matcher(ip).matches()) {
			return true;
		} else if (IPV4_PATTERN.matcher(ip).matches()) {
			return true;
		} else {
			return false;
		}
	}
}