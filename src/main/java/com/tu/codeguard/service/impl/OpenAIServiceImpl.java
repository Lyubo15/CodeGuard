package com.tu.codeguard.service.impl;

import com.tu.codeguard.config.properties.OpenAIProperties;
import com.tu.codeguard.service.AIProviderService;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements AIProviderService {

    private final SimpleOpenAI openAiService;
    private final OpenAIProperties openAIProperties;

    @Override
    public Integer getMaxTokens() {
        return openAIProperties.getMaxTokens();
    }

    @Override
    public int getTokens(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int wordCount = text.split("\\s+").length;
        int charCount = text.length();

        // GPT-4o-mini: 1 token ≈ 3.5 characters OR 1 token ≈ 0.75 words
        int estimatedTokens = (charCount / 3) + (wordCount / 2);

        log.debug("Estimated {} tokens for input text ({} chars, {} words)", estimatedTokens, charCount, wordCount);
        return estimatedTokens;

    }

    @Override
    public String sendMessages(String message, String prompts, String filename, UUID correlationId) {
        log.info("Sending the messages to OpenAI for file {} and correlationId {}", filename, correlationId);
        List<ChatMessage> chatMessages = createChatMessages(List.of(message, prompts));

        ChatRequest chatRequest = ChatRequest.builder()
                .model(openAIProperties.getModel())
                .messages(chatMessages)
                .maxCompletionTokens(openAIProperties.getMaxOutputTokens())
                .build();

        var futureChat = openAiService.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();

        String generatedText = chatResponse.firstContent();
        log.info("Sending the messages to OpenAI is done for file {} and correlationId {}", filename, correlationId);

        return generatedText;
    }

    private List<ChatMessage> createChatMessages(List<String> messages) {
        List<ChatMessage> chatMessages = new ArrayList<>();

        messages.forEach(message -> {
            ChatMessage.UserMessage userMessage = ChatMessage.UserMessage.of(message);
            chatMessages.add(userMessage);
        });

        return chatMessages;
    }
}
