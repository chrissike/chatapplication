package edu.hm.dako.chat.client.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("restriction")
public class ResultTableModel {

	private StringProperty colTest;
	private StringProperty colAnzahlNachrichten;
	private StringProperty colRTT;
	private StringProperty colRTTServer;
	private StringProperty colFreeMemory;
	private StringProperty colAvgCpu;

	public ResultTableModel(String colTest, String colAnzahlNachrichten, String colRTT, String colRTTServer,
			String colFreeMemory, String colAvgCpu) {
		super();
		this.colTest = new SimpleStringProperty(colTest);
		this.colAnzahlNachrichten = new SimpleStringProperty(colAnzahlNachrichten);
		this.colRTT = new SimpleStringProperty(colRTT);
		this.colRTTServer = new SimpleStringProperty(colRTTServer);
		this.colFreeMemory = new SimpleStringProperty(colFreeMemory);
		this.colAvgCpu = new SimpleStringProperty(colAvgCpu);
	}

	public StringProperty getColTest() {
		return colTest;
	}

	public void setColTest(StringProperty colTest) {
		this.colTest = colTest;
	}

	public StringProperty getColAnzahlNachrichten() {
		return colAnzahlNachrichten;
	}

	public void setColAnzahlNachrichten(StringProperty colAnzahlNachrichten) {
		this.colAnzahlNachrichten = colAnzahlNachrichten;
	}

	public StringProperty getColRTT() {
		return colRTT;
	}

	public void setColRTT(StringProperty colRTT) {
		this.colRTT = colRTT;
	}

	public StringProperty getColRTTServer() {
		return colRTTServer;
	}

	public void setColRTTServer(StringProperty colRTTServer) {
		this.colRTTServer = colRTTServer;
	}

	public StringProperty getColFreeMemory() {
		return colFreeMemory;
	}

	public void setColFreeMemory(StringProperty colFreeMemory) {
		this.colFreeMemory = colFreeMemory;
	}

	public StringProperty getColAvgCpu() {
		return colAvgCpu;
	}

	public void setColAvgCpu(StringProperty colAvgCpu) {
		this.colAvgCpu = colAvgCpu;
	}

	@Override
	public String toString() {
		return "ResultTableModel [colTest=" + colTest + ", colAnzahlNachrichten=" + colAnzahlNachrichten + ", colRTT="
				+ colRTT + ", colRTTServer=" + colRTTServer + ", colFreeMemory=" + colFreeMemory + ", colAvgCpu="
				+ colAvgCpu + "]";
	}

}
