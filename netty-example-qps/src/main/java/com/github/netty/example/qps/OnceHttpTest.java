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

/**
 * 一次qps统计 (一次运行)  注: servlet服务端口=10002,rpc-consumer服务端口=10000,rpc-provider服务端口=10001
 *
 * 用于测试qps性能, 直接右键运行即可
 * Http协议
 * @author acer01
 *  2018/8/12/012
 */
public class OnceHttpTest {
    private static final int PORT = 10002;
    private static final String HOST = "localhost";
    private static final String URI = "/hello?id=1&name=abc";
    private static final JsonObject BODY = new JsonObject("{\"body1\":\"QpsOnceTest-我是post内容\"}");

    private int queryCount = 10000;//===========总调用次数=================
    private int waitTime = 10;//===========等待时间(秒)=================
    private AtomicInteger successCount = new AtomicInteger();
    private AtomicInteger errorCount = new AtomicInteger();
    private CountDownLatch latch = new CountDownLatch(queryCount);
    private long totalTime;

    //==============Vertx客户端===============
    private Vertx vertx = Vertx.vertx();
    private WebClient client = WebClient.create(vertx,new WebClientOptions()
            .setTcpKeepAlive(false)
            //是否保持连接
            .setKeepAlive(true));

    public static void main(String[] args) throws InterruptedException {
        OnceHttpTest test = new OnceHttpTest();
        test.doQuery(PORT,HOST, URI);

        int successCount = test.successCount.get();
        int errorCount = test.errorCount.get();
        long totalTime = test.totalTime;

        test.client.close();
        test.vertx.close();

        if(successCount == 0){
            logger.info("无成功调用");
        }else {
            logger.info("时间 = " + totalTime + "毫秒, " +
                    "成功 = " + successCount + ", " +
                    "失败 = " + errorCount + ", " +
                    "qps = " + new BigDecimal((double) successCount / (double) totalTime * 1000).setScale(2, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString() +
                    "\r\n==============================="
            );
        }
    }

    private void doQuery(int port, String host, String uri) throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        for(int i=0; i< queryCount; i++) {
            client.post(port, host, uri).sendJsonObject(BODY,asyncResult -> {
                if(asyncResult.succeeded()){
                    successCount.incrementAndGet();
                }else {
                    errorCount.incrementAndGet();
                    System.out.println("error = " + asyncResult.cause());
                }
                latch.countDown();
            });
        }
        latch.await(waitTime, TimeUnit.SECONDS);
        totalTime = System.currentTimeMillis() - beginTime;
    }


    static final Logger logger = LoggerFactory.getLogger(OnceHttpTest.class);

}
