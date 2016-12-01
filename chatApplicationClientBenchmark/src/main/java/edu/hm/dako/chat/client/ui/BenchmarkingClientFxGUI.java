package edu.hm.dako.chat.client.ui;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.benchmarking.TopicSubscriber;
import edu.hm.dako.chat.jms.connect.JmsChatContext;
import edu.hm.dako.chat.jms.connect.JmsConsumer;
import edu.hm.dako.chat.client.data.ClientModel;
import edu.hm.dako.chat.client.data.ResultTableModel;
import edu.hm.dako.chat.client.data.SystemStatus;
import edu.hm.dako.chat.client.data.util.SystemResourceCalculator;
import edu.hm.dako.chat.model.BenchmarkPDU;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Chat-GUI
 * 
 * @author Paul Mandl
 *
 */
@SuppressWarnings("restriction")
public class BenchmarkingClientFxGUI extends Application {

	private static Log log = LogFactory.getLog(BenchmarkingClientFxGUI.class);

	private ClientModel model = new ClientModel();
	private SystemStatus sysStatus = new SystemStatus();

	public static BenchmarkingClientFxGUI instance;

	private static JmsChatContext jmsContext;
	private static SystemResourceCalculator sysResourceCalc;
	
	private static Integer clientNameCounter = 1;
	private static Integer clientNameReceivedCounter = 1;
	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Diese Methode wird von Java FX bei Aufruf der launch-Methode implizit
	 * aufgerufen
	 */
	public void start(Stage primaryStage) throws Exception {

		BenchmarkingClientFxGUI.instance = this;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/BenchmarkingClient.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		BenchmarkingGuiController lc = (BenchmarkingGuiController) loader.getController();
		lc.setAppController(this);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Benchmarking");
		primaryStage.setResizable(true);
		primaryStage.show();
		
		JmsConsumer consumer = new JmsConsumer();
		jmsContext = new JmsChatContext();
		try {
			consumer.initJmsConsumer(new TopicSubscriber(), jmsContext);
		} catch (NamingException e1) {
			log.error(e1.getStackTrace());
		}

		sysResourceCalc = new SystemResourceCalculator();
	}

	public ClientModel getModel() {
		return model;
	}

	public void setModel(ClientModel clientModel) {
		this.model = clientModel;
	}

	public SystemStatus getSysStatus() {
		return sysStatus;
	}

	public void setSysStatus(SystemStatus sysStatus) {
		this.sysStatus = sysStatus;
	}

	public static JmsChatContext getJmsContext() {
		return jmsContext;
	}

	public static void setJmsContext(JmsChatContext jmsContext) {
		BenchmarkingClientFxGUI.jmsContext = jmsContext;
	}

	public static SystemResourceCalculator getSysResourceCalc() {
		return sysResourceCalc;
	}

	public void showResults(BenchmarkPDU pdu, Long rtt, Double rttMs, Double rttServerMs) {
		Platform.runLater(() -> {
			Integer userNameNumber = Integer.valueOf(pdu.getUserName());

			log.info("Client: " + userNameNumber + ", ClientTime: " + rttMs + ", ServerTime: " + rttServerMs);

			// ChartBars
			getModel().addClientTime(userNameNumber, rttMs);
			getModel().addServerTime(userNameNumber, rttServerMs);

			// StockedBarChart
			getModel().setAnteilsChartClient((rtt.longValue() - pdu.getServerTime().longValue()) / 1000000000.0);
			getModel().setAnteilsChartServer(rttServerMs);

			// Label
			getModel().addToRttList(rttMs);
			getModel().addRttServerList(rttServerMs);

			// CPU/Memory
			getSysStatus().addCpuList(pdu.getAvgCPUUsage());
			getSysStatus().addFreeServerMemoryList(pdu.getFreeMemory().intValue());

			// Table
			ResultTableModel resulttable = new ResultTableModel(pdu.getUserName(), "1", String.valueOf(rttMs),
					String.valueOf(rttServerMs), pdu.getFreeMemory().toString(), pdu.getAvgCPUUsage().toString());
			getModel().addToResultList(resulttable);

			// calculate KPIs
			 getModel().calculateKPIs();
			 getSysStatus().calculateSysStatus();
		});
	}

	public synchronized static Integer getAndIncreaseClientNameReceivedCounter() {
		Integer count = clientNameReceivedCounter;
		clientNameReceivedCounter++;
		return count;
	}
	
	public synchronized static Integer getAndIncreaseClientNameCounter() {
		Integer count = clientNameCounter;
		clientNameCounter++;
		return count;
	}
}
