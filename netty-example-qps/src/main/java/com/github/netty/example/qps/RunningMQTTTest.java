package com.github.netty.example.qps;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * running 测试 (一直运行)  注: mqtt服务端口=10004
 * <p>
 * 用于测试qps性能, 直接右键运行即可
 * MQTT协议
 *
 * @author acer01
 * 2018/8/12/012
 */
public class RunningMQTTTest {
    private static Logger logger = LoggerFactory.getLogger(RunningMQTTTest.class);
    private static final int PORT = 10004;
    private static final String HOST = "localhost";
    private static final String TOPIC =
            "#";
//            "/hello?id=1&name=abc";

    private static final long reportPrintTime = 5;//===========统计间隔时间(秒)=================
    private static AtomicInteger successCount = new AtomicInteger();
    private static AtomicInteger errorCount = new AtomicInteger();
    private static AtomicLong totalSleepTime = new AtomicLong();


    //==============Vertx客户端===============

    public static void main(String[] args) {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
        Verticle verticle = new AbstractVerticle() {
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
                    logger.info("接收到消息: {} from topic {}", message, response.topicName());
                });

                client.connect(s -> {
                    if (!s.succeeded()) {
                        vertx.close();
                        scheduled.shutdown();
                        return;
                    }

                    Map<String, Integer> topics = new HashMap<>(2);
                    topics.put(TOPIC, MqttQoS.AT_LEAST_ONCE.value());
                    // subscribe to all subtopics
                    client.subscribe(topics, resp -> {
                        int result = resp.result();
                        logger.info("subscribe {}", resp);
                    });


                    AtomicInteger count = new AtomicInteger();
                    scheduled.scheduleAtFixedRate(() ->
                            client.publish("/hello",
                                    Buffer.buffer("发布数据" + count.incrementAndGet()), MqttQoS.EXACTLY_ONCE, true, true,
                                    asyncResult -> {
                                        if (asyncResult.succeeded()) {
//                            logger.info("publish {}",asyncResult);
                                        }
                                    }
                            ), 0, 15, TimeUnit.MILLISECONDS);
                });
            }
        };

        Vertx.vertx().deployVerticle(verticle);
        scheduled.scheduleAtFixedRate(new PrintInfoRunnable(), 0, reportPrintTime, TimeUnit.SECONDS);
    }


    static class PrintInfoRunnable implements Runnable {
        private AtomicInteger printCount = new AtomicInteger();
        private long beginTime = System.currentTimeMillis();

        @Override
        public void run() {
            long totalTime = System.currentTimeMillis() - beginTime - totalSleepTime.get();
            int successCount = RunningMQTTTest.successCount.get();
            int errorCount = RunningMQTTTest.errorCount.get();

            StringJoiner joiner = new StringJoiner(",");
            joiner.add("第(" + printCount.incrementAndGet() + ")次统计 ");
            joiner.add("时间 = " + totalTime + "毫秒[" + (totalTime / 60000) + "分" + ((totalTime % 60000) / 1000) + "秒] ");
            joiner.add("成功 = " + successCount);
            joiner.add("失败 = " + errorCount);
            joiner.add("平均响应 = " + new BigDecimal((double) totalTime / (double) successCount)
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString() + "ms, ");
            joiner.add("qps = " + new BigDecimal((double) successCount / (double) totalTime * 1000)
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString());

            logger.info(joiner.toString());
        }
    }

}
