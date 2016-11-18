package edu.hm.dako.chat.client.data;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.StackedBarChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;

/**
 * Modelldaten fuer FX-GUI
 */
@SuppressWarnings("restriction")
public class ClientModel {

	private ObservableList<ResultTableModel> resultList;
	private XYChart.Series<Integer, Double> serverTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<Integer, Double> clientTimeChart = new XYChart.Series<Integer, Double>();
	private XYChart.Series<CategoryAxis, Double> anteilsChartServer = new XYChart.Series<CategoryAxis, Double>();
	private XYChart.Series<CategoryAxis, Double> anteilsChartClient = new XYChart.Series<CategoryAxis, Double>();
	
	public ClientModel() {
		anteilsChartServer.setName("Serverzeit");
		anteilsChartClient.setName("Clientzeit (Anfrage + Netzlatenz)");
		resultList = FXCollections.observableArrayList();
	}

	public XYChart.Series getClientTimeChart() {
		return clientTimeChart;
	}

	public void addClientTime(Integer clientName, Double clientTime) {
		synchronized (clientTimeChart) {
			this.clientTimeChart.getData().add(new XYChart.Data(clientName, clientTime));
		}
	}

	public XYChart.Series getServerTimeChart() {
		return serverTimeChart;
	}

	public void addServerTime(Integer clientName, Double serverTime) {
		synchronized (serverTimeChart) {
			this.serverTimeChart.getData().add(new XYChart.Data(clientName, serverTime));
		}
	}

	
	
	
	
	
	
	public XYChart.Series<CategoryAxis, Double> getAnteilsChartServer() {
		return anteilsChartServer;
	}

	public void setAnteilsChartServer(Double serverTime) {
		synchronized (anteilsChartServer) {
			this.anteilsChartServer.getData().add(new XYChart.Data("Serverzeit", serverTime));
		}
	}

	public XYChart.Series<CategoryAxis, Double> getAnteilsChartClient() {
		return anteilsChartClient;
	}

	public void setAnteilsChartClient(Double clientZeit) {
		synchronized (anteilsChartClient) {
			this.anteilsChartClient.getData().add(new XYChart.Data("Clientzeit", clientZeit));
		}
	}

	public ObservableList<ResultTableModel> getResultList() {
		return resultList;
	}

	public void addToResultList(ResultTableModel resulttable) {
		synchronized (resultList) {
			resultList.addAll(resulttable);
		}
	}

	
}
