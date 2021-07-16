package com.example.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter(){super(Config.class);}

    @Override
    public GatewayFilter apply(Config config) {
//        return (exchange,chain)->
//        {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//            log.info("Logging Filter baseMessage: {}",config.baseMessage);
//
//            if(config.isPreLogger()){
//                log.info("Logging Filter Start:request id -> {}",request.getId());
//            }
//
//            //Custom Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(()->{
//                if(config.isPreLogger()){
//                    log.info("Logging Filter End:response id -> {}",response.getStatusCode());
//                }
//            }));
//        };

        GatewayFilter filter =new OrderedGatewayFilter(((exchange, chain) -> {
                        ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Logging Filter baseMessage: {}",config.baseMessage);

            if(config.isPreLogger()){
                log.info("Logging PRE Start:request id -> {}",request.getId());
            }

            //Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if(config.isPreLogger()){
                    log.info("Logging POST End:response code -> {}",response.getStatusCode());
                }
            }));
            //Ordered를 이용하여 filter 우선순위 지정가능
        }), Ordered.LOWEST_PRECEDENCE);

        return filter;
    }
    @Data
    public static class Config{
        // Put the Configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }


}
