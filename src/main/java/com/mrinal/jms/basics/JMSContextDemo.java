package com.mrinal.jms.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;

public class JMSContextDemo {

    public static void main(String[] args) throws Exception{

        // Uses JMS 2.0 API
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext =  cf.createContext()) {

            // create Producer and send message in 1 step
            jmsContext.createProducer().send(queue, "New message for JMS Context Demo");


            // create Consumer and receive message in 1 step
            String messageReceived = jmsContext.createConsumer(queue).receiveBody(String.class);

            System.out.println(messageReceived);

            // resources closes automatically when program ends

        }

    }
}
