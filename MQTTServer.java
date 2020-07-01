package com.hyls.sb.mqtt;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;

import io.moquette.broker.ClientDescriptor;
import io.moquette.broker.Server;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 *
 *

 * MQTT moquette 的Server  段用于发布主题，并发布主题信息
 *
 * 采用阻塞式 发布主题
 *
 * @author longgangbai
 */
public class MQTTServer {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTServer.class);
    private final static String CONNECTION_STRING = "tcp://0.0.0.0:1883";
    private final static boolean CLEAN_START = true;
    private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
    public  static Topic[] topics = {
            new Topic("china/beijing", QoS.EXACTLY_ONCE),
            new Topic("china/tianjin", QoS.AT_LEAST_ONCE),
            new Topic("china/henan", QoS.AT_MOST_ONCE)};
    public final  static long RECONNECTION_ATTEMPT_MAX=6;
    public final  static long RECONNECTION_DELAY=2000;

    public final static int SEND_BUFFER_SIZE=2*1024*1024;//发送最大缓冲为2M

    public static void main(String arg[])   {
        MQTT mqtt = new MQTT();
        try {
            //设置服务端的ip
            mqtt.setHost(CONNECTION_STRING);
            //连接前清空会话信息
            mqtt.setCleanSession(CLEAN_START);
            //设置重新连接的次数
            mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);
            //设置重连的间隔时间
            mqtt.setReconnectDelay(RECONNECTION_DELAY);
            //设置心跳时间
            mqtt.setKeepAlive(KEEP_ALIVE);
            //设置缓冲的大小
            mqtt.setSendBufferSize(SEND_BUFFER_SIZE);

           /* //创建连接
            BlockingConnection connection = mqtt.blockingConnection();
            //开始连接
            connection.connect();*/
            // 使用回调式API
            final CallbackConnection callbackConnection = mqtt
                    .callbackConnection();

            // 连接监听
            callbackConnection.listener(new Listener() {

                // 接收订阅话题发布的消息
                @Override
                public void onPublish(UTF8Buffer topic, Buffer payload,
                                      Runnable onComplete) {
                    System.out.println("=============receive msg================" + new String(payload.toByteArray()));
                    onComplete.run();
                }

                // 连接失败
                @Override
                public void onFailure(Throwable value) {
                    System.out.println("===========connect failure===========");
                    callbackConnection.disconnect(null);
                }

                // 连接断开
                @Override
                public void onDisconnected() {
                    System.out.println("====mqtt disconnected=====");

                }

                // 连接成功
                @Override
                public void onConnected() {
                    System.out.println("====mqtt connected=====");

                }
            });

            // 连接
            callbackConnection.connect(new Callback<Void>() {

                // 连接失败
                public void onFailure(Throwable value) {
                    System.out.println("============连接失败：" + value.getLocalizedMessage() + "============");
                }

                // 连接成功
                public void onSuccess(Void v) {
                    // 订阅主题
                    String topic="msg/normal";
                    String topic1="msg/disconnect";
                    Topic[] topics = { new Topic(topic, QoS.AT_LEAST_ONCE), new Topic(topic1, QoS.AT_LEAST_ONCE) };
                    callbackConnection.subscribe(topics, new Callback<byte[]>() {
                        // 订阅主题成功
                        public void onSuccess(byte[] qoses) {
                            System.out.println("========订阅成功=======");
                        }

                        // 订阅主题失败
                        public void onFailure(Throwable value) {
                            System.out.println("========订阅失败=======");
                            callbackConnection.disconnect(null);
                        }
                    });

                    // 发布消息
                    callbackConnection.publish(topic, ("Hello,我是服务端").getBytes(), QoS.AT_LEAST_ONCE, true, new Callback<Void>() {

                        public void onSuccess(Void v) {
                            LOG.info("===========消息发布成功============");
                        }

                        public void onFailure(Throwable value) {
                            LOG.info("========消息发布失败=======");
                            callbackConnection.disconnect(null);
                        }
                    });

                }
            });

            try {
                int count=0;
                //getClient();
                while(true){
                /*    count++;
                    //订阅的主题
                    String topic="china/tianjin";
                    //主题的内容
                    String message="hello "+count+"chinese people !";

                    //connection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, false);
                    LOG.info("MQTTServer Message  Topic="+topic+"  Content :"+message);
                    Thread.sleep(2000);*/
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void pubMsgToMj(String jsonStr)   {
        MQTT mqtt = new MQTT();
        try {
            //设置服务端的ip
            mqtt.setHost(CONNECTION_STRING);
            //连接前清空会话信息
            mqtt.setCleanSession(CLEAN_START);
            //设置重新连接的次数
            mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);
            //设置重连的间隔时间
            mqtt.setReconnectDelay(RECONNECTION_DELAY);
            //设置心跳时间
            mqtt.setKeepAlive(KEEP_ALIVE);
            //设置缓冲的大小
            mqtt.setSendBufferSize(SEND_BUFFER_SIZE);

            //创建连接
            BlockingConnection connection = mqtt.blockingConnection();
            //开始连接
            connection.connect();
            try {
                int count=0;
                String topic="msg/normal";
                connection.publish(topic, jsonStr.getBytes(), QoS.AT_LEAST_ONCE, false);
                while(true){
                    /*count++;
                    //订阅的主题

                    //主题的内容
                    String message="hello "+count+"chinese people !";
                    //connection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, false);


                    Thread.sleep(2000);*/
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }





}