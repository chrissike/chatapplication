package edu.hm.dako.chat.client.communication.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import edu.hm.dako.chat.common.ChatPDU;


/**
 * JMS Nachrichtenempfänger
 *
 */

public class TopicSubscriber implements MessageListener { // MessageDrivenBean

	public void onMessage(Message message) {
		System.out.println("+++++++++++++++++++>>>> Nachricht erhalten: " + message.toString());
		
		try {
			Thread.sleep(Integer.MAX_VALUE);
			if (message instanceof ChatPDU){
				System.out.println("Reading message:++++++++++++++++++++++++++++++++++ " + message.getBody(String.class));
			}else {
				 System.err.println("Message is not a TextMessage");
			}
			ChatPDU chatPDU = message.getBody(ChatPDU.class);
			System.out.println("Topicnachricht erhalten: " + chatPDU.toString());
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
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