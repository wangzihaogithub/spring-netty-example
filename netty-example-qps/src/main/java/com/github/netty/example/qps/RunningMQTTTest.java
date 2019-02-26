package com.github.netty.example.qps;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * running 测试 (一直运行)  注: mqtt服务端口=10004
 *
 * 用于测试qps性能, 直接右键运行即可
 * MQTT协议
 * @author acer01
 * 2018/8/12/012
 */
public class RunningMQTTTest {

    private static final int PORT = 10004;
    private static final String HOST = "localhost";
    private static final String TOPIC =
            "#";
//            "/hello?id=1&name=abc";

    private int queryCount = 10000;//===========一次qps任务的调用次数=================
    private int waitTime = 10;//===========一次qps任务的等待时间(秒)=================

    private static final long reportPrintTime = 5;//===========qps统计间隔时间(秒)=================
    private static final long onceSleep = 300;//===========下次调用qps任务的暂停时间(毫秒)=================

    private AtomicInteger successCount = new AtomicInteger();
    private AtomicInteger errorCount = new AtomicInteger();
    private AtomicLong totalSleepTime = new AtomicLong();

    //==============Vertx客户端===============
    private Vertx vertx = Vertx.vertx();
    private Verticle verticle = new AbstractVerticle(){
        @Override
        public void start() {
            MqttClient client = MqttClient.create(vertx, new MqttClientOptions()
                    .setHost(HOST)
                    .setPort(PORT)
                    .setWillTopic("willTopic")
                    .setWillMessage("hello")
                    .setWillFlag(true)
                    .setUsername("admin")
                    .setPassword("123456")
                    .setMaxMessageSize(8192));

            client.publishHandler(response -> {
                String message = new String(response.payload().getBytes(), Charset.forName("UTF-8"));
                System.out.println(
                        String.format("接收到消息: \"%s\" from topic \"%s\"",
                                message, response.topicName()));
            });

            client.connect(s -> {
                Map<String,Integer> topics = new HashMap<>(2);
                topics.put(TOPIC, MqttQoS.AT_LEAST_ONCE.value());
                // subscribe to all subtopics
                client.subscribe(topics, resp -> {
                    int result = resp.result();
                    System.out.println("subscribe"+resp);
                });

                ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
                AtomicInteger count = new AtomicInteger();
                scheduled.scheduleAtFixedRate(() ->
                    client.publish("/hello",Buffer.buffer("发布数据" + count.incrementAndGet()) ,MqttQoS.EXACTLY_ONCE,true,true, asyncResult -> {
                        if(asyncResult.succeeded()){
    //                        System.out.println("publish"+asyncResult);
                        }
                    }
                ),0,15, TimeUnit.MILLISECONDS);
            });
        }
    };


    public static void main(String[] args) {
        RunningMQTTTest test = new RunningMQTTTest();

        test.vertx.deployVerticle(test.verticle);
        new PrintThread(test).start();
    }

    static class PrintThread extends Thread{
        private final RunningMQTTTest test;
        private AtomicInteger printCount = new AtomicInteger();
        private long beginTime = System.currentTimeMillis();
        static final Logger logger = LoggerFactory.getLogger(PrintThread.class);

        PrintThread(RunningMQTTTest test) {
            super("QpsPrintThread");
            this.test = test;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(reportPrintTime * 1000);
//                    synchronized (test) {
                    long totalTime = System.currentTimeMillis() - beginTime - test.totalSleepTime.get();
//                    printQps(test.successCount.get(), test.errorCount.get(), totalTime);
//                    }
                }catch (Throwable t){
                    t.printStackTrace();
                }
            }
        }

        private void printQps(int successCount, int errorCount, long totalTime){
            if(successCount == 0){
                logger.info("无成功调用");
            }else {
                logger.info(
                        "第(" + printCount.incrementAndGet() + ")次统计, " +
                                "时间 = " + totalTime + "毫秒[" + (totalTime / 60000) + "分" + ((totalTime % 60000) / 1000) + "秒], " +
                                "成功 = " + successCount + ", " +
                                "失败 = " + errorCount + ", " +
                                "平均响应 = " + new BigDecimal((double) totalTime / (double) successCount).setScale(2, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString() + "ms, " +
                                "qps = " + new BigDecimal((double) successCount / (double) totalTime * 1000).setScale(2, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString()
//                            +
//                            "\r\n==============================="
                );
            }
        }
    }


    static final Logger logger = LoggerFactory.getLogger(RunningMQTTTest.class);

}
