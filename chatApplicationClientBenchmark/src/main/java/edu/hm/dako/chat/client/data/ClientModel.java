package edu.hm.dako.chat.client.data;

import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;

import java.util.ArrayList;
import java.util.List;

import edu.hm.dako.chat.client.data.util.ModelCalculator;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

/**
 * Modelldaten fuer FX-GUI
 */
@SuppressWarnings("restriction")
public class ClientModel {

	private ModelCalculator calculator;

	private ObservableList<ResultTableModel> resultList;

	private List<Double> rttList;
	private List<Double> rttServerList;
	private List<Double> serverMemoryList;

	private DoubleProperty averageRTT;
	private DoubleProperty maxRTT;
	private DoubleProperty minRTT;
	private DoubleProperty averageServerRTT;
	private DoubleProperty stdDev;

	private XYChart.Series<Integer, Double> serverTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<Integer, Double> clientTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<CategoryAxis, Double> anteilsChartServer = new XYChart.Series<CategoryAxis, Double>();
	private XYChart.Series<CategoryAxis, Double> anteilsChartClient = new XYChart.Series<CategoryAxis, Double>();

	public ClientModel() {
		this.calculator = new ModelCalculator();

		this.anteilsChartServer.setName("Serverzeit");
		this.anteilsChartClient.setName("Clientzeit (Anfrage + Netzlatenz)");
		this.resultList = FXCollections.observableArrayList();

		this.rttList = new ArrayList<Double>();
		this.rttServerList = new ArrayList<Double>();
		this.serverMemoryList = new ArrayList<Double>();

		this.averageRTT = new SimpleDoubleProperty(0.0);
		this.averageServerRTT = new SimpleDoubleProperty(0.0);
		this.stdDev = new SimpleDoubleProperty(0.0);
		this.maxRTT = new SimpleDoubleProperty(0.0);
		this.minRTT = new SimpleDoubleProperty(0.0);
	}

	public synchronized void calculateKPIs() {
		// AverageRTT
		Double averageRtt = Math.round(calculator.calcAverageOfDouble(getRttList()) * 100.0) / 100.0;
		setAverageRTT(averageRtt);

		// MaxMinRTT
		setMaxRTT(Math.round(calculator.getMaxOfList(getRttList()) * 100.0) / 100.0);
		setMinRTT(Math.round(calculator.getMinOfList(getRttList()) * 100.0) / 100.0);

		// StdDev
		setStdDev(Math.round(calculator.getStdDev(getRttList(), averageRtt) * 100.0) / 100.0);

		// AverageRTTServer
		setAverageServerRTT(Math.round(calculator.calcAverageOfDouble(getRttServerList()) * 100.0) / 100.0);
	}

	public XYChart.Series<Integer, Double> getClientTimeChart() {
		return clientTimeChart;
	}

	public synchronized void addClientTime(Integer clientName, Double clientTime) {
		this.clientTimeChart.getData().add(new XYChart.Data<Integer, Double>(clientName, clientTime));
	}

	public XYChart.Series<Integer, Double> getServerTimeChart() {
		return serverTimeChart;
	}

	public synchronized void addServerTime(Integer clientName, Double serverTime) {
		this.serverTimeChart.getData().add(new XYChart.Data<Integer, Double>(clientName, serverTime));
	}

	public XYChart.Series<CategoryAxis, Double> getAnteilsChartServer() {
		return anteilsChartServer;
	}

	public synchronized void setAnteilsChartServer(Double serverTime) {
		this.anteilsChartServer.getData().add(new XYChart.Data("Serverzeit", serverTime));
	}

	public XYChart.Series<CategoryAxis, Double> getAnteilsChartClient() {
		return anteilsChartClient;
	}

	public synchronized void setAnteilsChartClient(Double clientZeit) {
		this.anteilsChartClient.getData().add(new XYChart.Data("Clientzeit", clientZeit));
	}

	public ObservableList<ResultTableModel> getResultList() {
		return resultList;
	}

	public synchronized void addToResultList(ResultTableModel resulttable) {
		resultList.addAll(resulttable);
	}

	public List<Double> getRttList() {
		return rttList;
	}

	public synchronized void addToRttList(Double rtt) {
		this.rttList.add(rtt);
	}

	public DoubleProperty getAverageRTT() {
		return averageRTT;
	}

	public synchronized void setAverageRTT(Double averageRTT) {
		this.averageRTT.set(averageRTT);
	}

	public DoubleProperty getStdDev() {
		return stdDev;
	}

	public synchronized void setStdDev(Double stdDev) {
		this.stdDev.set(stdDev);
	}

	public DoubleProperty getAverageServerRTT() {
		return averageServerRTT;
	}

	public synchronized void setAverageServerRTT(Double averageServerRTT) {
		this.averageServerRTT.set(averageServerRTT);
	}

	public List<Double> getRttServerList() {
		return rttServerList;
	}

	public synchronized void addRttServerList(Double rttServer) {
		this.rttServerList.add(rttServer);
	}

	public List<Double> getServerMemoryList() {
		return serverMemoryList;
	}

	public synchronized void addServerMemoryList(Double serverMemory) {
		this.serverMemoryList.add(serverMemory);
	}

	public DoubleProperty getMaxRTT() {
		return maxRTT;
	}

	public void setMaxRTT(Double maxRTT) {
		this.maxRTT.set(maxRTT);
	}

	public DoubleProperty getMinRTT() {
		return minRTT;
	}

	public void setMinRTT(Double minRTT) {
		this.minRTT.set(minRTT);
	}

}
