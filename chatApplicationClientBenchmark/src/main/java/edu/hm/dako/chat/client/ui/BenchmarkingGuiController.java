package edu.hm.dako.chat.client.ui;

import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;

import edu.hm.dako.chat.client.benchmarking.ProcessBenchmarking;
import edu.hm.dako.chat.client.data.GroupedResultTableModel;
import edu.hm.dako.chat.client.data.ResultTableModel;
import edu.hm.dako.chat.rest.TechnicalRestException;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
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
	private Label avgRTT, maxRTT, minRTT, avgRTTServer, rttSD, avgCPU, minFreeMemory, txtNumberOfMessages,
			txtNumberOfProcessedMessages;
	@FXML
	private Button startButton;
	@FXML
	private TableView<ResultTableModel> tableResults;
	@FXML
	private TableView<GroupedResultTableModel> tableResultsGrouped;
	@FXML
	private TableColumn<ResultTableModel, String> colTest, colAnzahlNachrichten, colRTT, colRTTServer, colFreeMemory,
			colAvgCpu;
	@FXML
	private TableColumn<GroupedResultTableModel, String> colClientName, colAvgRttOfClient;
	@FXML
	private Tab rttDiagramm1, rttDiagramm2, rttDiagramm3, ergebniszahlen;
	@FXML
	private NumberAxis analyzedMessage1, analyzedMessage2, analyzedMessage4, timeOfMessage1, timeOfMessage2,
			timeOfMessage4;
	@FXML
	private AreaChart<Integer, Double> areaChart1, areaChart2;
	@FXML
	private LineChart<Integer, Double> areaChart3;
	@FXML
	private LineChart<Double, Double> regressionChart;
	@FXML
	private StackedBarChart<String, Double> stackedbarChart1;
	@FXML
	private ProgressBar processBar;

	public void setAppController(BenchmarkingClientFxGUI appController) {
		// initialize columns of Table tableResults
		colTest.setCellValueFactory(cellData -> cellData.getValue().getColTest());
		colAnzahlNachrichten.setCellValueFactory(cellData -> cellData.getValue().getColAnzahlNachrichten());
		colRTT.setCellValueFactory(cellData -> cellData.getValue().getColRTT());
		colRTTServer.setCellValueFactory(cellData -> cellData.getValue().getColRTTServer());
		colFreeMemory.setCellValueFactory(cellData -> cellData.getValue().getColFreeMemory());
		colAvgCpu.setCellValueFactory(cellData -> cellData.getValue().getColAvgCpu());

		// initialize columns of Table tableResultsGrouped
		colClientName.setCellValueFactory(cellData -> cellData.getValue().getColUsername());
		colAvgRttOfClient.setCellValueFactory(cellData -> cellData.getValue().getColAvgRTT());

		// initialize tables
		tableResults.setItems(appController.getModel().getResultList());
		tableResults.setEditable(false);
		tableResultsGrouped.setItems(appController.getModel().getGroupedResultList());
		tableResultsGrouped.setEditable(false);

		// initialize charts
		areaChart1.getData().addAll(appController.getModel().getMessageTimeChart());
		areaChart2.getData().addAll(appController.getModel().getServerTimeChart());
		areaChart3.getData().addAll(new XYChart.Series<Integer, Double>(), appController.getModel().getClientTimeChart());
		regressionChart.setAnimated(false);
		regressionChart.setCreateSymbols(true);
		regressionChart.getData().addAll(appController.getModel().getRegression1(),
				appController.getModel().getRegression2());

		stackedbarChart1.getData().add(appController.getModel().getAnteilsChartClient());
		stackedbarChart1.getData().add(appController.getModel().getAnteilsChartServer());

		txtNumberOfMessages.setText(String.valueOf(0));
		txtNumberOfProcessedMessages.textProperty()
				.bind(Bindings.convert(appController.getModel().getTotalCountOfProcessedMessages()));

		// initialize statistic-fields
		avgRTT.textProperty().bind(Bindings.convert(appController.getModel().getAverageRTT()));
		maxRTT.textProperty().bind(Bindings.convert(appController.getModel().getMaxRTT()));
		minRTT.textProperty().bind(Bindings.convert(appController.getModel().getMinRTT()));
		avgRTTServer.textProperty().bind(Bindings.convert(appController.getModel().getAverageServerRTT()));
		rttSD.textProperty().bind(Bindings.convert(appController.getModel().getStdDev()));
		avgCPU.textProperty().bind(Bindings.convert(appController.getSysStatus().getAvgCPU()));
		minFreeMemory.textProperty().bind(Bindings.convert(appController.getSysStatus().getMinServerMemory()));

		// process
		processBar.progressProperty().bind(appController.getModel().getProcessedPercentage());
	}

	@FXML
	private void startBenchmarking() throws TechnicalRestException, URISyntaxException {
		BenchmarkingClientFxGUI.instance.setIp(txtServername.getText());
		BenchmarkingClientFxGUI.instance.setPort(Integer.valueOf(txtServerPort.getText()));
		
		txtNumberOfMessages.setText(String.valueOf(
				Integer.valueOf(txtAnzahlClients.getText()) * Integer.valueOf(txtAnzahlNachrichten.getText())));

		ProcessBenchmarking process = new ProcessBenchmarking(generateMessageByLength(),
				Integer.parseInt(txtAnzahlClients.getText()));

		process.performLogoutAll();
		
		Integer messageCount = Integer.parseInt(txtAnzahlNachrichten.getText());
		Integer clientCount = Integer.parseInt(txtAnzahlClients.getText());

		BenchmarkingClientFxGUI.instance.getModel().setTotalNumberOfMessages(clientCount * messageCount);

		for (int i = 1; i <= Integer.parseInt(txtAnzahlClients.getText()); i++) {
			process.createNewBenchmarkingClient(
					String.valueOf(BenchmarkingClientFxGUI.getAndIncreaseClientNameCounter()), messageCount);
		}
	}

	private String generateMessageByLength() {
		return StringUtils.leftPad("", Integer.parseInt(txtNachrichtenlaenge.getText()), 'x');
	}

}