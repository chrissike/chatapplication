package edu.hm.dako.chat.client.data;

import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hm.dako.chat.client.data.util.ModelCalculator;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

/**
 * Modelldaten fuer FX-GUI
 */
@SuppressWarnings("restriction")
public class ClientModel {

	private ModelCalculator calculator;

	private Integer totalNumberOfMessages;

	private ObservableList<ResultTableModel> resultList;
	private ObservableList<GroupedResultTableModel> groupedResultList;

	private final Map<String, List<Double>> sharedRTTClientList;

	private List<Double> rttList;
	private List<Double> rttServerList;
	private List<Double> serverMemoryList;

	private IntegerProperty totalCountOfProcessedMessages;
	private DoubleProperty processedPercentage;

	private DoubleProperty averageRTT;
	private DoubleProperty maxRTT;
	private DoubleProperty minRTT;
	private DoubleProperty averageServerRTT;
	private DoubleProperty stdDev;

	private XYChart.Series<Integer, Double> serverTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<Integer, Double> messageTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<Integer, Double> clientTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<String, Double> anteilsChartServer = new XYChart.Series<String, Double>();
	private XYChart.Series<String, Double> anteilsChartClient = new XYChart.Series<String, Double>();

	private XYChart.Series<Double,Double> regression1 = new XYChart.Series<Double,Double>();
	private XYChart.Series<Double,Double> regression2 = new XYChart.Series<Double,Double>();
	
	public ClientModel() {
		this.calculator = new ModelCalculator();

		sharedRTTClientList = new HashMap<String, List<Double>>();

		this.anteilsChartServer.setName("Serverzeit");
		this.anteilsChartClient.setName("Clientzeit (Anfrage + Netzlatenz)");
		this.regression1.setName("REG1");
		this.regression2.setName("REG2");
		this.resultList = FXCollections.observableArrayList();
		this.groupedResultList = FXCollections.observableArrayList();

		this.rttList = new ArrayList<Double>();
		this.rttServerList = new ArrayList<Double>();
		this.serverMemoryList = new ArrayList<Double>();

		this.totalCountOfProcessedMessages = new SimpleIntegerProperty(0);
		this.processedPercentage = new SimpleDoubleProperty(0.0);
		
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

	public XYChart.Series<Integer, Double> getMessageTimeChart() {
		return messageTimeChart;
	}

	public synchronized void addMessageTime(Integer messageNumber, Double clientTime) {
		this.messageTimeChart.getData().add(new XYChart.Data<Integer, Double>(messageNumber, clientTime));
	}

	public XYChart.Series<Integer, Double> getClientTimeChart() {
		return clientTimeChart;
	}

	public synchronized void addClientTime(Integer clientName, Double clientTime) {
		this.clientTimeChart.getData().add(new XYChart.Data<Integer, Double>(clientName, clientTime));
	}

	public synchronized void updateClientTime(Integer clientName, Double clientTime) {
		for (XYChart.Data<Integer, Double> data : this.clientTimeChart.getData()) {
			if (data.getXValue().equals(clientName)) {
				data.setYValue(clientTime);
			}
		}
	}

	public XYChart.Series<Integer, Double> getServerTimeChart() {
		return serverTimeChart;
	}

	public synchronized void addServerTime(Integer clientName, Double serverTime) {
		this.serverTimeChart.getData().add(new XYChart.Data<Integer, Double>(clientName, serverTime));
	}

	public XYChart.Series<String, Double> getAnteilsChartServer() {
		return anteilsChartServer;
	}

	public synchronized void setAnteilsChartServer(Double serverTime) {
		this.anteilsChartServer.getData().add(new XYChart.Data<String, Double>("Serverzeit", serverTime));
	}

	public XYChart.Series<String, Double> getAnteilsChartClient() {
		return anteilsChartClient;
	}

	public synchronized void setAnteilsChartClient(Double clientZeit) {
		this.anteilsChartClient.getData().add(new XYChart.Data<String, Double>("Clientzeit", clientZeit));
	}

	public ObservableList<ResultTableModel> getResultList() {
		return resultList;
	}

	public synchronized void addToGroupedResultList(GroupedResultTableModel groupedResultList) {
		this.groupedResultList.addAll(groupedResultList);
	}

	public synchronized void updateGroupedResultList(GroupedResultTableModel groupedResultList) {
		GroupedResultTableModel model = getGroupedResultList().stream()
				.filter(p -> p.getColUsername().getValue().equals(groupedResultList.getColUsername().getValue()))
				.findFirst().get();
		GroupedResultTableModel newmodel = model;
		newmodel.setColAvgRTT(groupedResultList.getColAvgRTT());
		getGroupedResultList().set(getGroupedResultList().indexOf(model), newmodel);
	}

	public ObservableList<GroupedResultTableModel> getGroupedResultList() {
		return groupedResultList;
	}

	public synchronized void addToResultList(ResultTableModel resulttable) {
		this.resultList.addAll(resulttable);
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

	public void addClientToSharedRTTClientList(String clientName) {
		sharedRTTClientList.put(clientName, new ArrayList<Double>());
	}

	public synchronized void addRTTToSharedRTTClientList(String clientName, Long rtt) {
		List<Double> list = sharedRTTClientList.get(clientName);
		if(list == null) {	
			list = new ArrayList<Double>();
		}
		list.add(rtt.doubleValue());
		sharedRTTClientList.put(clientName, list);
	}

	public synchronized List<Double> getRTTListOfClient(String clientName) {
		return sharedRTTClientList.get(clientName);
	}

	public Integer getTotalNumberOfMessages() {
		return totalNumberOfMessages;
	}

	public void setTotalNumberOfMessages(Integer totalNumberOfMessages) {
		this.totalNumberOfMessages = totalNumberOfMessages;
	}

	public IntegerProperty getTotalCountOfProcessedMessages() {
		return totalCountOfProcessedMessages;
	}

	public synchronized void setTotalCountOfProcessedMessages(Integer totalCountOfProcessedMessages) {
		this.totalCountOfProcessedMessages.set(totalCountOfProcessedMessages);
	}

	public DoubleProperty getProcessedPercentage() {
		return processedPercentage;
	}

	public void setProcessedPercentage(Double processedPercentage) {
		this.processedPercentage.set(processedPercentage);
	}

	public XYChart.Series<Double,Double> getRegression1() {
		return regression1;
	}

	public synchronized void setRegression1(Double reg1, Double reg2) {
		regression1.getData().add(new XYChart.Data<Double, Double>(reg1, reg2));
	}

	public XYChart.Series<Double,Double> getRegression2() {
		return regression2;
	}

	public synchronized void setRegression2(Double reg1, Double reg2) {
		regression2.getData().add(new XYChart.Data<Double, Double>(reg1, reg2));
	}
}
