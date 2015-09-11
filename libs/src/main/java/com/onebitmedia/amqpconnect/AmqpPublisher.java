package com.onebitmedia.amqpconnect;

import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @author rakawm
 */
public class AmqpPublisher {

    //Define static values
    public static final String DEFAULT_TYPE = "application/json";
    public static final String DEFAULT_ENCODING = "utf-8";
    public static final int DEFAULT_DELIVERY_TYPE = 1;
    public static final int DEFAULT_PRIORITY = 5;
    private static final String TAG = "AMQP Publisher";
    //Define connection and channel.
    public Channel channel;
    public String url;
    ConnectionFactory connectionFactory;
    Connection connection;

    public AmqpPublisher() {

    }

    public AmqpPublisher(String url) {
        this.url = url;
        connectionFactory = new ConnectionFactory();
    }

    /**
     * Setup connection to AMQP server.
     *
     */
    public void setupConnectionFactory() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        connectionFactory.setAutomaticRecoveryEnabled(false);
        connectionFactory.setUri(url);
    }

    /**
     * Start amqp connection with specified url.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public void startConnection() throws IOException, TimeoutException {
        if(url==null) {
            Log.d(TAG, "Url is not defined.");
            return;
        }
        connection = connectionFactory.newConnection();
        Log.d(TAG, String.format("AMQP connection to %s is started.", url));
    }

    /**
     * Stop amqp connection if it's opened.
     *
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        if (connection == null) {
            Log.d(TAG, "Connection is null.");
            return;
        }

        if(connection.isOpen()) {
            connection.close();
            Log.d(TAG, "AMQP connection is closed.");
        } else {
            Log.d(TAG, "AMQP connection is already closed.");
        }
    }

    /**
     * Create amqp channel if connection is open.
     *
     * @throws IOException
     */
    public void createChannel() throws IOException {
        if(!connection.isOpen()) {
            Log.d(TAG, "Connection is not available.");
            return;
        }
        channel = connection.createChannel();
    }

    /**
     * Bind channel to specific exchange, queue and routing key.
     * @param exchangeName    exchange name.
     * @param queueName       queue name.
     * @param routingKey      routing key.
     * @throws IOException
     */
    public void bindChannel(String exchangeName, String queueName, String routingKey) throws IOException {
        if(!channel.isOpen()) {
            Log.d(TAG, "Channel is not available.");
            return;
        }
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    /**
     * Enable confirmation mode in channel.
     *
     * @throws IOException
     */
    public void activateConfirmation() throws IOException {
        channel.confirmSelect();
    }

    /**
     * Close AMQP channel if it's opened.
     * @throws IOException
     * @throws TimeoutException
     */
    public void closeChannel() throws IOException, TimeoutException {
        if(channel.isOpen()) {
            channel.close();
            Log.d(TAG, "Channel has been closed.");
        }
    }


    public void disableConfirmation() {
        channel.clearConfirmListeners();
    }

    /**
     * Create basic properties for AMQP message.
     *
     * @return AMQP message's properties.
     */
    public AMQP.BasicProperties createBasicProperties() {
        return new AMQP.BasicProperties(
                DEFAULT_TYPE,
                DEFAULT_ENCODING,
                null,
                DEFAULT_DELIVERY_TYPE,
                DEFAULT_PRIORITY,
                null,
                null,
                null,
                null,
                new Date(),
                null,
                null,
                null,
                null
        );
    }

    /**
     * Publish message in specified exchange and queue.
     * @param exchangeName    exchange name.
     * @param message         message body.
     * @param routingKey      routing key.
     * @throws IOException
     */
    public boolean publishMessage(String exchangeName, String routingKey, String message) throws IOException {
        if(channel==null || !channel.isOpen()) {
            Log.d(TAG, "Channel isn't available.");
            return false;
        }
        channel.basicPublish(exchangeName, routingKey, createBasicProperties(), message.getBytes());
        Log.d(TAG, String.format("Message has been published in exchange %s using routing key %s.", exchangeName, routingKey));
        return true;
    }

    /**
     * Publish message in specified exchange and routing key.
     * @param exchangeName    exchange name.
     * @param properties      message properties.
     * @param message         message body.
     * @param routingKey      routing key.
     * @throws IOException
     */
    public boolean publishMessage(String exchangeName, String routingKey, AMQP.BasicProperties properties, String message) throws IOException {
        if(channel==null || !channel.isOpen()) {
            Log.d(TAG, "Channel isn't available.");
            return false;
        }
        channel.basicPublish(exchangeName, routingKey, properties, message.getBytes());
        Log.d(TAG, String.format("Message has been published in exchange %s using routing key %s.", exchangeName, routingKey));
        return true;
    }

}
