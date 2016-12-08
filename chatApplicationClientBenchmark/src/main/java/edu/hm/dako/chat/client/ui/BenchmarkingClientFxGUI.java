package edu.hm.dako.chat.client.ui;

import edu.hm.dako.chat.client.benchmarking.TopicSubscriber;
import edu.hm.dako.chat.client.data.ClientModel;
import edu.hm.dako.chat.client.data.GroupedResultTableModel;
import edu.hm.dako.chat.client.data.ResultTableModel;
import edu.hm.dako.chat.client.data.SystemStatus;
import edu.hm.dako.chat.client.data.util.SystemResourceCalculator;
import edu.hm.dako.chat.jms.connect.JmsChatContext;
import edu.hm.dako.chat.jms.connect.JmsConsumer;
import edu.hm.dako.chat.model.BenchmarkPDU;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.NamingException;
import java.util.stream.Collectors;

/**
 * Chat-GUI
 */
@SuppressWarnings("restriction")
public class BenchmarkingClientFxGUI extends Application {

	private static Log log = LogFactory.getLog(BenchmarkingClientFxGUI.class);

	private ClientModel model = new ClientModel();
	private SystemStatus sysStatus = new SystemStatus();

	public static BenchmarkingClientFxGUI instance;

	private static JmsChatContext jmsContext;
	private static SystemResourceCalculator sysResourceCalc;

	/**
	 * Ziel-Adresse des Servers (Default ist 127.0.0.1)
	 */
	private static String ip = "127.0.0.1";
	
	/**
	 * Ziel Port des Servers (Default ist 8089)
	 */
	private static Integer port = 8089;
	
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
		jmsContext = new JmsChatContext(this.ip, this.port);
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

			log.debug("Client: " + pdu.getUserName() + ", ClientTime: " + rttMs + ", ServerTime: " + rttServerMs);

			// ChartBars
			getModel().addMessageTime(Integer.valueOf(pdu.getMessageNr()), rttMs);
			getModel().addServerTime(Integer.valueOf(pdu.getMessageNr()), rttServerMs);

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
			ResultTableModel resultTableRow = new ResultTableModel(pdu.getMessageNr().toString(), "1",
					String.valueOf(rttMs), String.valueOf(rttServerMs), pdu.getFreeMemory().toString(),
					pdu.getAvgCPUUsage().toString());

			getModel().addToResultList(resultTableRow);
			addEntryToGroupedClientRTTList(pdu.getUserName(), rtt);

			// calculate KPIs
			getModel().calculateKPIs();
			getSysStatus().calculateSysStatus();

			// progress
			getModel().setTotalCountOfProcessedMessages(getModel().getTotalCountOfProcessedMessages().get() + 1);
			getModel().setProcessedPercentage(
					(getModel().getTotalCountOfProcessedMessages().doubleValue() * 100 / getModel().getTotalNumberOfMessages().doubleValue())/100);
		});
	}

	public void addEntryToGroupedClientRTTList(String username, Long rtt) {

		getModel().addRTTToSharedRTTClientList(username, rtt.longValue());

		Double avgRttOfClient = getModel().getRTTListOfClient(username).parallelStream()
				.collect(Collectors.averagingDouble(d -> d)).longValue() / 1000000000.0;
		GroupedResultTableModel groupedResulttableRow = new GroupedResultTableModel(username,
				avgRttOfClient.toString());

		if (getModel().getGroupedResultList().stream().filter(p -> p.getColUsername().getValue().equals(username))
				.findFirst().isPresent()) {
			getModel().updateClientTime(Integer.valueOf(username), avgRttOfClient);
			getModel().updateGroupedResultList(groupedResulttableRow);
		} else {
			getModel().addClientTime(Integer.valueOf(username), avgRttOfClient);
			getModel().addToGroupedResultList(groupedResulttableRow);
		}
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

	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		BenchmarkingClientFxGUI.ip = ip;
	}

	public static Integer getPort() {
		return port;
	}

	public static void setPort(Integer port) {
		BenchmarkingClientFxGUI.port = port;
	}
}
