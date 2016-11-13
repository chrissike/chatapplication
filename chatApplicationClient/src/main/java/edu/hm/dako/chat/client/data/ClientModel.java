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
	
	public ObservableList<String> users = FXCollections.observableArrayList();
	public ObservableList<String> chats = FXCollections.observableArrayList();

	public BooleanProperty block = new SimpleBooleanProperty();

}
