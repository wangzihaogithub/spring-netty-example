package com.github.netty.example.qps;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * running 测试 (一直运行)  注: servlet服务端口=10002,rpc-consumer服务端口=10000,rpc-provider服务端口=10001
 * <p>
 * 用于测试qps性能, 直接右键运行即可
 * Http协议
 *
 * @author acer01
 * 2018/8/12/012
 */
public class RunningHttpTest {
    private static final int PORT = 10002;
    private static final String HOST = "localhost";
    private static final String URI = "/hello?id=1&name=abc";
    private static final JsonObject BODY = new JsonObject("{\"body1\":\"QpsRunningTest-我是post内容\"}");

    private int queryCount = 10000;//===========一次qps任务的调用次数=================
    private int waitTime = 10;//===========一次qps任务的等待时间(秒)=================

    private static final long reportPrintTime = 5;//===========qps统计间隔时间(秒)=================
    private static final long onceSleep = 300;//===========下次调用qps任务的暂停时间(毫秒)=================

    private AtomicInteger successCount = new AtomicInteger();
    private AtomicInteger errorCount = new AtomicInteger();
    private AtomicLong totalSleepTime = new AtomicLong();

    //==============Vertx客户端===============
    private WebClient client = WebClient.create(Vertx.vertx(), new WebClientOptions()
            .setTcpKeepAlive(false)
            //是否保持连接
            .setKeepAlive(true));

    public static void main(String[] args) {
        RunningHttpTest test = new RunningHttpTest();
        new PrintThread(test).start();

        try {
            while (true) {
                test.doQuery(PORT, HOST, URI);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void doQuery(int port, String host, String uri) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(queryCount);
        for (int i = 0; i < queryCount; i++) {
            client.get(port, host, uri).sendJsonObject(BODY, asyncResult -> {
                if (asyncResult.succeeded()) {
                    successCount.incrementAndGet();
                } else {
                    errorCount.incrementAndGet();
                    System.out.println("error = " + asyncResult.cause());
                }
                latch.countDown();
            });
        }

        latch.await(waitTime, TimeUnit.SECONDS);
        Thread.sleep(onceSleep);
        totalSleepTime.addAndGet(onceSleep);
    }

    static class PrintThread extends Thread {
        private final RunningHttpTest test;
        private AtomicInteger printCount = new AtomicInteger();
        private long beginTime = System.currentTimeMillis();
        static final Logger logger = LoggerFactory.getLogger(PrintThread.class);

        PrintThread(RunningHttpTest test) {
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
                    printQps(test.successCount.get(), test.errorCount.get(), totalTime);
//                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        private void printQps(int successCount, int errorCount, long totalTime) {
            if (successCount == 0) {
                logger.info("无成功调用");
            } else {
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


}
