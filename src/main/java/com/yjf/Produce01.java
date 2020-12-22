package com.yjf;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 余俊锋
 * @date 2020/12/22 17:05
 * @Description
 */
public class Produce01 {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 2. 设置连接参数
        factory.setHost("192.168.190.132");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 3. 获取连接对象
        Connection connection = factory.newConnection();

        // 4. 通过connection获取Channel【管道】
        Channel channel = connection.createChannel();
        // 5. 定义一个交换机
        /*
            参数1(queue): 队列名称
            参数2(durable): 是否要持久化队列(mq重启之后还在)
            参数3(exclusive): 是否是独占队列(只能有一个消费者监听此队列,没有也不行)
            参数4(autoDelete): 是否自动删除(当没有Consumer时,队列自动删除)
            参数5(arguments): 队列的其他参数(过期时间、最大消息容量等)
         */
        channel.queueDeclare("hello_world",true,false,false,null);
        String body="hello world";
        // 6.发送消息
        /*
            参数1(exchange): 交换机的名称,简单模式下交换机会使用默认的交换机""
            参数2(routingKey): routingKey,路由名称,在简单模式下,routingKey就是队列名称
            参数3(props): 消息的一些配置信息
            参数4(body): 发送的消息(字节)
         */
        channel.basicPublish("","hello_world",null,body.getBytes());

        channel.close();
        connection.close();
    }

}
