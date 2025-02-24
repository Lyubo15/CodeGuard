package com.tu.codeguard.config;

import com.tu.codeguard.config.properties.OpenAIProperties;
import io.github.sashirestela.cleverclient.client.JavaHttpClientAdapter;
import io.github.sashirestela.openai.SimpleOpenAI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class OpenAIConfiguration {

    private final OpenAIProperties openAIProperties;

    @Bean
    public SimpleOpenAI openAiService() {
        var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(openAIProperties.getConnectionDuration()))
                .executor(Executors.newFixedThreadPool(5))
                .build();

        return SimpleOpenAI.builder()
                .apiKey(openAIProperties.getToken())
                .clientAdapter(new JavaHttpClientAdapter(httpClient))
                .build();
    }
}
