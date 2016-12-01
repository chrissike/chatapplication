package edu.hm.dako.chat.client.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("restriction")
public class GroupedResultTableModel {

	private StringProperty colUsername;
	private StringProperty colAvgRTT;

	public GroupedResultTableModel(String colTest, String colAnzahlNachrichten) {
		super();
		this.colUsername = new SimpleStringProperty(colTest);
		this.colAvgRTT = new SimpleStringProperty(colAnzahlNachrichten);
	}

	public StringProperty getColUsername() {
		return colUsername;
	}

	public void setColTest(StringProperty colUsername) {
		this.colUsername = colUsername;
	}

	public StringProperty getColAvgRTT() {
		return colAvgRTT;
	}

	public void setColAvgRTT(StringProperty colAvgRTT) {
		this.colAvgRTT = colAvgRTT;
	}

	@Override
	public String toString() {
		return "GroupedResultTableModel [colUsername=" + colUsername + ", colAvgRTT=" + colAvgRTT + "]";
	}
}
