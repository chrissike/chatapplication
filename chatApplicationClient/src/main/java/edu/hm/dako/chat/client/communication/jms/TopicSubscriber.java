package edu.hm.dako.chat.client.communication.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.MessageDrivenContext;
import javax.annotation.Resource;


import edu.hm.dako.chat.common.ChatPDU;


/**
 * JMS Nachrichtenempfänger
 *
 */

public class TopicSubscriber implements MessageListener { // MessageDrivenBean

	
	AtomicLong count = new AtomicLong(0);
	
	public void onMessage(Message message) {
		System.out.println("++++++++++++++++11+>>>> Nachricht erhalten: " + message.toString());
		
		long i;
		try {
			Thread.sleep(Integer.MAX_VALUE);
			if (message instanceof ChatPDU){
				
				i=count.incrementAndGet();
				System.out.println("Reading message:++++++++++++++++++++++++++++++++++ " + message.getBody(String.class));
			}else {
				 System.err.println("Message is not a TextMessage");
			}
			ChatPDU chatPDU = message.getBody(ChatPDU.class);
			System.out.println("Topicnachricht erhalten: " + chatPDU.toString());
		} catch (JMSException e) {
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public long getCount(){
		 return count.get();
	}

//	@Override
//	public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
//		log.info("setMessageDrivenContext() wird aufgerufen!!!"); 
//		
//	}
//
//	@Override
//	public void ejbRemove() throws EJBException {
//		log.info("ejbRemove() wird aufgerufen!!!"); 
//		
//	}
}