package kr.meet42.apigatewayservice.client;

import feign.codec.Decoder;
import feign.codec.Encoder;

import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.context.annotation.Bean;

public class FeignConfig {
    @Bean
    public Decoder feignDecoder() {
        return new GsonDecoder();
    }

    @Bean
    public Encoder feignEncoder() {
        return new GsonEncoder();
    }
}
