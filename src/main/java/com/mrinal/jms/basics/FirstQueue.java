package com.mrinal.jms.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class FirstQueue {

    public static void main(String[] args) throws NamingException {

        // JMS 1.x

        InitialContext initialContext = null;
        Connection connection = null;


        try {
            // uses information from jndi.properties
            // initialContext is the root to the JNDI registry
            initialContext = new InitialContext();

            // lookup method creates object so necessary to cast
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

            // creates connection
            connection = cf.createConnection();

            Session session  = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");


            MessageProducer producer = session.createProducer(queue);

            // sent a text as a message
            TextMessage message1 =  session.createTextMessage("Hello World");
            TextMessage message2 =  session.createTextMessage("This is my second message");

            producer.send(message1);
            producer.send(message2);

            QueueBrowser browser = session.createBrowser(queue);

            Enumeration messagesEnum = browser.getEnumeration();

            while(messagesEnum.hasMoreElements()) {
                TextMessage currMessage = (TextMessage) messagesEnum.nextElement();
                System.out.println("Browsing: " + currMessage.getText());
            }

            MessageConsumer consumer = session.createConsumer(queue);

            // need to tell JMS Provider that we are ready to consume the message
            connection.start();

            // messages will only be removed from the queue after consumer receives message
            TextMessage messageReceived = (TextMessage) consumer.receive(5000);
            System.out.println("Message Received: " + messageReceived.getText());//create a message consumer

            messageReceived = (TextMessage) consumer.receive(5000);
            System.out.println("Message Received: " + messageReceived.getText());//create a message consumer



        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
