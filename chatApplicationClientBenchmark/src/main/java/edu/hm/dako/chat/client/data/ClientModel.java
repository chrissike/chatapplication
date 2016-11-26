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
	
	private DoubleProperty averageRTT;
	private DoubleProperty averageServerRTT;
	private DoubleProperty stdDev;
	
	private XYChart.Series<Integer, Double> serverTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<Integer, Double> clientTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<CategoryAxis, Double> anteilsChartServer = new XYChart.Series<CategoryAxis, Double>();
	private XYChart.Series<CategoryAxis, Double> anteilsChartClient = new XYChart.Series<CategoryAxis, Double>();

	public ClientModel() {
		calculator = new ModelCalculator();

		anteilsChartServer.setName("Serverzeit");
		anteilsChartClient.setName("Clientzeit (Anfrage + Netzlatenz)");
		resultList = FXCollections.observableArrayList();
		
		rttList = new ArrayList<Double>();
		rttServerList = new ArrayList<Double>();
		
		averageRTT = new SimpleDoubleProperty(0.0);
		averageServerRTT = new SimpleDoubleProperty(0.0);
		stdDev = new SimpleDoubleProperty(0.0);
	}
	
	public void calculateKPIs() {
		//AverageRTT
		Double averageRtt = calculator.calcAverageOfDouble(getRttList());
		averageRtt = Math.round(averageRtt * 100.0) / 100.0;
		setAverageRTT(averageRtt);
		System.out.println("AverageRtt: " + getAverageRTT());

		//StdDev
		Double stdDev = calculator.getStdDev(getRttList(), averageRtt);
		stdDev = Math.round(stdDev * 100.0) / 100.0;
		setStdDev(stdDev);
		System.out.println("StdDev:" + getStdDev());
		
		//AverageRTTServer
		Double averageServerRtt = calculator.calcAverageOfDouble(getRttServerList());
		averageServerRtt = Math.round(averageServerRtt * 100.0) / 100.0;
		setAverageServerRTT(averageServerRtt);
		System.out.println("AverageRtt: " + getAverageRTT());
		
		//TODO AverageCPU
		
		//TODO AverageMemory
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

}
