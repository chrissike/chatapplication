package edu.hm.dako.chat.client.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Modelldaten fuer FX-GUI
 * 
 * @author Paul Mandl
 * 
 */
@SuppressWarnings("restriction")
public class ClientModel {

	private StringProperty userName = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();
	private StringProperty address = new SimpleStringProperty();
	private StringProperty port = new SimpleStringProperty();
	
	public StringProperty userNameProperty() {
		return userName;
	}

	public void setUserName(String name) {
		this.userName.set(name);
	}

	public String getUserName() {
		return userName.get();
	}

	public StringProperty getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password.set(password);
	}
	
	public StringProperty getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public StringProperty getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port.set(port);
	}


	public ObservableList<String> users = FXCollections.observableArrayList();
	public ObservableList<String> chats = FXCollections.observableArrayList();

	public BooleanProperty block = new SimpleBooleanProperty();
}
