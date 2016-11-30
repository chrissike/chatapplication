package edu.hm.dako.chat.client.benchmarking;

import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import edu.hm.dako.chat.client.benchmarking.ProcessBenchmarking;
import edu.hm.dako.chat.jms.connect.JmsConsumer;
import edu.hm.dako.chat.jms.connect.JmsProducer;
import edu.hm.dako.chat.model.PDU;

public class JmsBenchmarkProcessTest {

	private static Log log = LogFactory.getLog(JmsBenchmarkProcessTest.class);

	ProcessBenchmarking process;
	JmsConsumer consumer;
	TestTopicSubscriber testSubscriber;

	@Before
	public void prepareTest() {
		consumer = new JmsConsumer();
		testSubscriber = new TestTopicSubscriber();
		try {
			consumer.initJmsConsumer(testSubscriber);
		} catch (NamingException e1) {
			log.error(e1.getStackTrace());
		}
	}

	@Test
	public void testJms() {
		Boolean success = false;

		process = new ProcessBenchmarking("testmessage", 1);
		JmsProducer<PDU> jms = new JmsProducer<PDU>();
		try {
			jms.sendMessage(process.createBenchmarkCPU("Hans Dieter"));
		} catch (NamingException e) {
			log.error(e.getMessage() + ", " + e.getCause());
		} catch (JMSException e) {
			log.error(e.getMessage() + ", " + e.getCause());
		}

		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(testSubscriber.getMessageCount() > 0) {
			success = true;
		}
		assertTrue(success);
	}
}
