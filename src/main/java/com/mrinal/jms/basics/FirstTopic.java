package com.mrinal.jms.basics;

import javax.jms.*;
import javax.naming.InitialContext;

public class FirstTopic {
    public static void main(String[] args) throws Exception{

        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/myTopic");

        ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = cf.createConnection();

        Session session = connection.createSession();
        MessageProducer producer = session.createProducer(topic);

        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageConsumer consumer2 = session.createConsumer(topic);

        TextMessage message = session.createTextMessage("All the Power is within me.");

        producer.send(message);

        connection.start();

        TextMessage message1 = (TextMessage) consumer1.receive();
        System.out.println("Consumer 1 message received: " + message1.getText());

        TextMessage message2 = (TextMessage) consumer2.receive();
        System.out.println("Consumer 2 message received: " + message2.getText());

        connection.close();
        initialContext.close();



    }
}
