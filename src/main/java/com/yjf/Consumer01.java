package com.yjf;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 余俊锋
 * @date 2020/12/22 17:17
 * @Description
 */
public class Consumer01 {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂,用于获取频道channel
        ConnectionFactory factory = new ConnectionFactory();

        // 设置连接参数
        factory.setHost("192.168.190.132");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        //2.创建连接
        Connection connection = factory.newConnection();

        //创建频道
        Channel channel=connection.createChannel();
        // 4.创建队列
    /*
        定义队列,如果没有此队列则创建,如果有则不创建
        queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)

        queue:      队列的名称
        durable:    是否持久化队列(mq重启之后还在)
        exclusive:  是否独占(只能有一个消费者监听此队列)
        autoDelete: 是否自动删除(当没有Consumer时,自动删除掉)
        arguments:  其他参数
     */
        //可以不再创建，避免没有报错，所以创建一下
        channel.queueDeclare("hello_world", true, false, false, null);

        // 5. 接收消息
    /*
        basicConsume(String queue, boolean autoAck, Consumer callback)
        queue:      队列名称
        autoAck:    是否开启自动确认
        callback:   回调对象
     */
    channel.basicConsume("hello_world",true,new DefaultConsumer(channel){
  // 回调方法,当收到消息之后,会自动执行该方法
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            /*
                1. consumerTag：标识
                2. envelope：获取一些信息，交换机，路由key...
                3. properties:配置信息
                4. body：数据
             */
            System.out.println("consumerTag：" + consumerTag);
            System.out.println("Exchange：" + envelope.getExchange());
            System.out.println("RoutingKey：" + envelope.getRoutingKey());
            System.out.println("properties：" + properties);
            System.out.println("body：" + new String(body));
        }
    });

        // 不释放资源,让rabbitmq一直监听
    }
}
