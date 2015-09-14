[ ![Download](https://api.bintray.com/packages/rakawestu/rakawestu/amqp-connect/images/download.svg) ](https://bintray.com/rakawestu/rakawestu/amqp-connect/_latestVersion)

# AMQP CONNECT
Android wrapper library for publishing message to AMQP. It uses [RabbtiMQ Java Library](https://www.rabbitmq.com/java-client.html) as the base client.

# Requirement

This library requires SDK level 1 and Internet permission to send the message.

# Usage

This is a sample usage to send a string message to RabbitMQ server.

```
String URL = "AMQP Server URL";
AmqpPublisher publisher = new AmqpPublisher(URL);
publisher.setupConnectionFactory();
publisher.startConnection();
publisher,createChannel();
publisher.publishMessage("Exchange", "Routing Key", "Message");
publisher,closeConnection();
```
