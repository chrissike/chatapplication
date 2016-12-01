package edu.hm.dako.chat.client.ui;

import org.apache.commons.lang3.StringUtils;

import edu.hm.dako.chat.client.benchmarking.ProcessBenchmarking;
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

/**
 * Controller fuer Login-GUI
 */
@SuppressWarnings("restriction")
public class BenchmarkingGuiController {

	@FXML
	private TextField txtServername, txtServerPort, txtAnzahlClients, txtAnzahlNachrichten, txtNachrichtenlaenge;
	@FXML
	private Label avgRTT, maxRTT, minRTT, avgRTTServer, rttSD, avgCPU, minFreeMemory;
	@FXML
	private Button startButton;
	@FXML
	private TableView<ResultTableModel> tableErgebnis;
	@FXML
	private TableColumn<ResultTableModel, String> colTest, colAnzahlNachrichten, colRTT, colRTTServer, colFreeMemory, colAvgCpu;
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
		colRTT.setCellValueFactory(cellData -> cellData.getValue().getColRTT());
		colRTTServer.setCellValueFactory(cellData -> cellData.getValue().getColRTTServer());
		colFreeMemory.setCellValueFactory(cellData -> cellData.getValue().getColFreeMemory());
		colAvgCpu.setCellValueFactory(cellData -> cellData.getValue().getColAvgCpu());

		tableErgebnis.setItems(appController.getModel().getResultList());
		tableErgebnis.setEditable(false);

		areaChart1.getData().addAll(appController.getModel().getClientTimeChart());
		areaChart2.getData().addAll(appController.getModel().getServerTimeChart());

		stackedbarChart1.getData().add(appController.getModel().getAnteilsChartClient());
		stackedbarChart1.getData().add(appController.getModel().getAnteilsChartServer());

		avgRTT.textProperty().bind(Bindings.convert(appController.getModel().getAverageRTT()));
		maxRTT.textProperty().bind(Bindings.convert(appController.getModel().getMaxRTT()));
		minRTT.textProperty().bind(Bindings.convert(appController.getModel().getMinRTT()));
		avgRTTServer.textProperty().bind(Bindings.convert(appController.getModel().getAverageServerRTT()));
		rttSD.textProperty().bind(Bindings.convert(appController.getModel().getStdDev()));
		avgCPU.textProperty().bind(Bindings.convert(appController.getSysStatus().getAvgCPU()));
		minFreeMemory.textProperty().bind(Bindings.convert(appController.getSysStatus().getMinServerMemory()));
	}

	@FXML
	private void startBenchmarking() {
		ProcessBenchmarking process = new ProcessBenchmarking(generateMessageByLength(),
				Integer.parseInt(txtAnzahlClients.getText()));

		for (int i = 1; i <= Integer.parseInt(txtAnzahlClients.getText()); i++) {
			process.createNewBenchmarkingClient(
					String.valueOf(BenchmarkingClientFxGUI.getAndIncreaseClientNameCounter()), Integer.parseInt(txtAnzahlNachrichten.getText()));
		}
	}

	private String generateMessageByLength() {
		return StringUtils.leftPad("", Integer.parseInt(txtNachrichtenlaenge.getText()), 'x');
	}

}