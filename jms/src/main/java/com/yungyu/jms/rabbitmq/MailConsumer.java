package com.yungyu.jms.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class MailConsumer {

    public static final String QUEUE_NAME = "MAILQueue";

    public static void main(String[] args){
        ConnectionFactory factory = null;
        Connection connection = null;
        Channel channel = null;
        Consumer consumer = null;
        try{
            factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(ConnectionFactory.DEFAULT_AMQP_PORT);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //super.handleDelivery(consumerTag, envelope, properties, body);
                    System.out.println(new String(body, "UTF-8"));
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
