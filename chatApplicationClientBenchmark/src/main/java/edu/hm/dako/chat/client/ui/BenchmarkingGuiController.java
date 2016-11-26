package edu.hm.dako.chat.client.ui;

import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.benchmarking.ProcessBenchmarking;
import edu.hm.dako.chat.client.benchmarking.TopicSubscriber;
import edu.hm.dako.chat.client.communication.jms.JmsConsumer;
import edu.hm.dako.chat.client.data.ResultTableModel;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;

/**
 * Controller fuer Login-GUI
 */
@SuppressWarnings("restriction")
public class BenchmarkingGuiController {

	private static Log log = LogFactory.getLog(BenchmarkingGuiController.class);

	@FXML
	private TextField txtServername, txtServerPort, txtAnzahlClients, txtAnzahlNachrichten, txtNachrichtenlaenge;
	@FXML
	private Label avgRTT, avgRTTServer, rttSD, avgCPU, avgMemory;
	@FXML
	private Button startButton;
	@FXML
	private TableView<ResultTableModel> tableErgebnis;
	@FXML
	private TableColumn<ResultTableModel, String> colTest, colAnzahlNachrichten, colRTT, colRTTServer;
	@FXML
	private Tab rttDiagramm1, rttDiagramm2, ergebniszahlen;
	@FXML
	private NumberAxis analyzedMessage1, analyzedMessage2, timeOfMessage1, timeOfMessage2;
	@FXML
	private AreaChart<Integer, Double> areaChart1, areaChart2;
	@FXML
	private StackedBarChart<CategoryAxis, Double> stackedbarChart1;

	public void setAppController(BenchmarkingClientFxGUI appController) {
		colTest.setCellValueFactory(cellData -> cellData.getValue().getColTest());
		colAnzahlNachrichten.setCellValueFactory(cellData -> cellData.getValue().getColAnzahlNachrichten());
		colRTT.setCellValueFactory(cellData -> cellData.getValue().getColTest());
		colRTTServer.setCellValueFactory(cellData -> cellData.getValue().getColTest());

		tableErgebnis.setItems(appController.getModel().getResultList());
		tableErgebnis.setEditable(false);

		areaChart1.getData().addAll(appController.getModel().getClientTimeChart());
		areaChart2.getData().addAll(appController.getModel().getServerTimeChart());

		stackedbarChart1.getData().add(appController.getModel().getAnteilsChartClient());
		stackedbarChart1.getData().add(appController.getModel().getAnteilsChartServer());
		
		avgRTT.textProperty().bind(Bindings.convert(appController.getModel().getAverageRTT()));
		avgRTTServer.textProperty().bind(Bindings.convert(appController.getModel().getAverageServerRTT()));
		rttSD.textProperty().bind(Bindings.convert(appController.getModel().getStdDev()));
		avgCPU.textProperty().bind(Bindings.convert(appController.getSysStatus().getAvgCPU()));
		avgMemory.textProperty().bind(Bindings.convert(appController.getSysStatus().getAvgMemory()));
	}

	@FXML
	private void startBenchmarking() {
		ProcessBenchmarking process = new ProcessBenchmarking(generateMessageByLength(),
				Integer.parseInt(txtAnzahlClients.getText()));

		JmsConsumer consumer = new JmsConsumer();
		try {
			consumer.initJmsConsumer(new TopicSubscriber());
		} catch (NamingException e1) {
			log.error(e1.getStackTrace());
		}

		for (int i = 0; i <= Integer.parseInt(txtAnzahlClients.getText()); i++) {
			process.createNewBenchmarkingClient(String.valueOf(i));
		}
	
		process.startAllClients();

	}

	private String generateMessageByLength() {
		return StringUtils.leftPad("", Integer.parseInt(txtNachrichtenlaenge.getText()), 'x');
	}

}