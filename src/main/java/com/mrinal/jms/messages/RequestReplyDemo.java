package com.mrinal.jms.messages;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class RequestReplyDemo {

    public static void main(String[] args) throws Exception{

        // 2 Queues used in order for an application to be able to both request and respond
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/requestQueue");
        //Queue replyQueue = (Queue) context.lookup("queue/replyQueue");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext =  cf.createContext()) {

            // create Producer and send message in 1 step
            JMSProducer producer = jmsContext.createProducer();
            TemporaryQueue replyQueue =  jmsContext.createTemporaryQueue();
            TextMessage message = jmsContext.createTextMessage("This is a request");
            message.setJMSReplyTo(replyQueue); // Allocates reply channel to replyQueue
            producer.send(queue, message);


            // create Consumer and receive message in 1 step
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage messageReceived = (TextMessage) consumer.receive();
            System.out.println(messageReceived.getText());

            JMSProducer replyProducer =  jmsContext.createProducer();
            // Second queue created for consumer to respond to producer
            replyProducer.send(messageReceived.getJMSReplyTo(), "This is the reply"); // Dynamically finds replyQueue

            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
            System.out.println(replyConsumer.receiveBody(String.class));


        }

    }
}
