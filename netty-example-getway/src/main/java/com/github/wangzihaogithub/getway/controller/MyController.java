package com.github.wangzihaogithub.getway.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

/**
 * @author wangzihao
 */
@Controller
@RefreshScope
public class MyController {
    @Value("${user.name:}")
    private String configCenterTest;
    @NacosInjected
    private NamingService namingService;
    @Autowired
    private ObjectFactory<WebClient.Builder> webClientBuilderFactory;

    @RequestMapping("/rpc/provider/**")
    public Mono<ResponseEntity<Flux<DataBuffer>>> rpcProvider(ServerWebExchange exchange) throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance("rpc-provider-service");
        Mono<ResponseEntity<Flux<DataBuffer>>> asyncResponse = asyncRequest(exchange.getRequest(), instance);
        return asyncResponse;
    }

    @RequestMapping("/rpc/consumer/**")
    public Mono<ResponseEntity<Flux<DataBuffer>>> rpcConsumer(ServerWebExchange exchange) throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance("rpc-consumer-service");
        Mono<ResponseEntity<Flux<DataBuffer>>> asyncResponse = asyncRequest(exchange.getRequest(), instance);
        return asyncResponse;
    }

    private Mono<ResponseEntity<Flux<DataBuffer>>> asyncRequest(ServerHttpRequest httpRequest, Instance instance) throws NacosException {
        String rawProtocol = httpRequest.getSslInfo() == null ? "http://" : "https://";
        HttpMethod rawHttpMethod = Objects.requireNonNull(httpRequest.getMethod());
        URI rawUri = httpRequest.getURI();
        HttpHeaders rawHeaders = httpRequest.getHeaders();
        Flux<DataBuffer> rawBody = httpRequest.getBody();

        String url = rawProtocol + instance.getIp() + ":" + instance.getPort() +
                rawUri.getRawPath() + "?" + rawUri.getRawQuery();
        WebClient.Builder webClientBuilder = webClientBuilderFactory.getObject()
                .baseUrl(url);

        Mono<ClientResponse> exchangeMono = webClientBuilder.build()
                .method(rawHttpMethod)
                .headers((HttpHeaders httpHeaders) -> {
                    httpHeaders.clear();
                    httpHeaders.addAll(rawHeaders);
                })
                .body(rawBody, DataBuffer.class)
                .exchange();

        Mono<ResponseEntity<Flux<DataBuffer>>> clientResponseMono = exchangeMono.map((ClientResponse clientResponse) -> {
            ClientHttpResponse clientHttpResponse = clientResponse.body((httpResponse, context) -> httpResponse);
            return new ResponseEntity<>(
                    clientHttpResponse.getBody(),
                    clientResponse.headers().asHttpHeaders(),
                    clientResponse.statusCode()
            );
        });
        return clientResponseMono;
    }
}
