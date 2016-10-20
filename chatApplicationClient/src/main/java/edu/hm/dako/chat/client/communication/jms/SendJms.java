package edu.hm.dako.chat.client.communication.jms;

import javax.naming.NamingException;

public class SendJms {

	
	public static void main( String[] args ) throws NamingException {
		 
		SimpleJmsProducer2 producer = new SimpleJmsProducer2();
		producer.sendJms();
		
	}
}
