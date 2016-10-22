//package edu.hm.dako.chat.client.communication.jms;
//
//import javax.jms.Topic;
//import javax.jms.TopicConnection;
//import javax.jms.TopicConnectionFactory;
//import javax.jms.TopicSession;
//import javax.naming.Context;
//
//public class TopicSubscriber {
//
//	public static void main(String[] args) {
//		final String topicName = "jms/MyJMSTopic";
//		final String topicConnectionFactoryName = "jms/MyJMSTCF";
//		final String oc4juser = "oc4jadmin";
//		final String oc4juserpassword = "welcome1";
//		final String urlProvider = "opmn:ormi://";
//		final String jmsProviderHost = "sracanov-au.au.oracle.com";
//		final String colon = ":";
//		final String opmnPort = "6007";
//		final String oc4jinstance = "OC4J_JMS";
//		Context jndiContext = null;
//		TopicConnectionFactory topicConnectionFactory = null;
//		TopicConnection topicConnection = null;
//		TopicSession topicSession = null;
//		Topic topic = null;
//		TopicSubscriber topicSubscriber = null;
//		JMSTopicMapListener topicListener = null;
//
//		/*
//		 * Set the environment for a connection to the OC4J instance
//		 */
//		Hashtable env = new Hashtable();
//		env.put(Context.INITIAL_CONTEXT_FACTORY, "oracle.j2ee.rmi.RMIInitialContextFactory");
//		env.put(Context.SECURITY_PRINCIPAL, oc4juser);
//		env.put(Context.SECURITY_CREDENTIALS, oc4juserpassword);
//		env.put(Context.PROVIDER_URL,
//				urlProvider + jmsProviderHost + colon + opmnPort + colon + oc4jinstance + "/default");
//
//		/*
//		 * Set the Context Object. Lookup the Topic Connection Factory. Lookup
//		 * the JMS Destination.
//		 */
//		try {
//			jndiContext = new InitialContext(env);
//			topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup(topicConnectionFactoryName);
//			topic = (Topic) jndiContext.lookup(topicName);
//		} catch (NamingException e) {
//			System.out.println("Lookup failed: " + e.toString());
//			System.exit(1);
//		}
//
//		/*
//		 * Create connection. Create session from connection; false means
//		 * session is not transacted. Create subscriber. Register message
//		 * listener (TextListener). Receive text messages from topic. Close
//		 * connection.
//		 */
//		try {
//			topicConnection = topicConnectionFactory.createTopicConnection();
//			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
//			topicSubscriber = topicSession.createSubscriber(topic);
//			topicListener = new JMSTopicMapListener();
//			topicSubscriber.setMessageListener(topicListener);
//			topicConnection.start();
//			System.out.println("Subscripted to topic: \"" + topicName + "\"");
//			while (true) {
//			}
//		} catch (JMSException e) {
//			System.out.println("Exception occurred: " + e.toString());
//		} finally {
//			if (topicConnection != null) {
//				try {
//					topicConnection.close();
//				} catch (JMSException e) {
//					System.out.println("Closing error: " + e.toString());
//				}
//			}
//		}
//	}
//}
