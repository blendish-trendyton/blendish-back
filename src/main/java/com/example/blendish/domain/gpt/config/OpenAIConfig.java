package com.example.blendish.domain.gpt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(15))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                );

        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1") // baseUrl을 명확히 지정
                .defaultHeader("Content-Type", "application/json")
                .filter(addAuthorizationHeader())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }


    private ExchangeFilterFunction addAuthorizationHeader() {
        return (request, next) -> {
            return next.exchange(
                    ClientRequest.from(request)
                            .header("Authorization", "Bearer " + openAiKey)
                            .build()
            );
        };
    }

    @Bean(name = "openAiObjectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
