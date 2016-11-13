package edu.hm.dako.chat.benchmarking;

import edu.hm.dako.chat.benchmarking.data.UserInterfaceInputParameters;
import edu.hm.dako.chat.benchmarking.service.impl.SharedClientStatistics;
import edu.hm.dako.chat.client.ui.ClientUserInterface;

/**
 * Uebernimmt die Konfiguration und die Erzeugung bestimmter Client-Typen fuer
 * das Benchmarking. Siehe
 * {@link edu.hm.dako.chat.benchmarking.data.echo.benchmarking.UserInterfaceInputParameters.ImplementationType}
 * Dies beinhaltet die {@link ConnectionFactory}, die Adressen, Ports, Denkzeit
 * etc.
 */
public final class BenchmarkingClientFactory {

	private BenchmarkingClientFactory() {
	}

	public static Runnable getClient(ClientUserInterface userInterface,
			UserInterfaceInputParameters param, int numberOfClient,
			SharedClientStatistics sharedData) {
		try {

			switch (param.getImplementationType()) {

			case TCPSimpleImplementation:
			case TCPAdvancedImplementation:

//				BenchmarkingClientImpl impl = new BenchmarkingClientImpl(userInterface,
//						param.getImplementationType(), param.getRemoteServerPort(),
//						param.getRemoteServerAddress(), numberOfClient, param.getMessageLength(),
//						param.getNumberOfMessages(), param.getClientThinkTime(),
//						param.getNumberOfRetries(), param.getResponseTimeout(), sharedData,
//						getDecoratedFactory(new TcpConnectionFactory()));
//				return impl;

			case UDPAdvancedImplementation:

				System.out.println("UDP noch nicht implementiert");

			default:
				throw new RuntimeException(
						"Unbekannter Implementierungstyp: " + param.getImplementationType());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	public static ConnectionFactory getDecoratedFactory(
//			ConnectionFactory connectionFactory) {
//		return new DecoratingConnectionFactory(connectionFactory);
//	}
}