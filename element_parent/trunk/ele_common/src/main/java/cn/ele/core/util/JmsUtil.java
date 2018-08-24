package cn.ele.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Map;

/**
 * JMS������
 * @author Administrator
 *
 */
@Component
public class JmsUtil {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * ������Ϣ
	 * @param destination
	 * @param map
	 */
	public void send(Destination destination, final Map<String ,String> map){
		System.out.println("---------------�����߷�����Ϣ-----------------");   
		jmsTemplate.send(destination, new MessageCreator() {			
			public Message createMessage(Session session) throws JMSException {
				
				MapMessage mapMessage = session.createMapMessage();
				
				for(String key:map.keySet()){
					mapMessage.setString(key, map.get(key));			
				}				
				
				return mapMessage;
			}			
		} );			
	}
	
	
	/**
	 * ������Ϣ���ı���
	 * @param destination
	 * @param text
	 */
	public void send(Destination destination, final String text){
		System.out.println("---------------�����߷�����Ϣ(Text)-----------------");   
		jmsTemplate.send(destination, new MessageCreator() {			
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(text);
			}			
		} );			
	}
	
}
