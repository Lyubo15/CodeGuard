package com.tu.codeguard.service.impl;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.enums.PromptOption;
import com.tu.codeguard.exceptions.FileReadException;
import com.tu.codeguard.service.AIProviderService;
import com.tu.codeguard.service.AISourceCodeAnalysisService;
import com.tu.codeguard.service.ZipService;
import com.tu.codeguard.utils.PromptsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AISourceCodeAnalysisServiceImpl implements AISourceCodeAnalysisService {

    private final ZipService zipService;
    private final AIProviderService aiProviderService;

    @Override
    public AIAnalysisResultDTO analyzeSourceCode(
            ZipInputStream zip,
            List<PromptOption> promptOptions,
            UUID correlationId)
    {
        try (zip) {
            log.info("Start analyzing source code. correlationId {}", correlationId);
            Map<String, String> sourceCode = zipService.readSourceCode(zip);
            String combinedCode = mergeSourceCode(sourceCode);

            String prompts = (promptOptions != null && !promptOptions.isEmpty())
                    ? PromptsUtils.combinePromptsFromOptions(promptOptions)
                    : PromptsUtils.getPrompts();

            AIAnalysisResultDTO result = sendToAIWithTokenManagement(combinedCode, prompts, correlationId);
            log.info("Completed analysis. correlationId {}", correlationId);
            return result;
        } catch (IOException e) {
            log.error("Error processing Zip file. correlationId {}, error: {}", correlationId, e.getMessage());
            throw new FileReadException(e.getMessage());
        }
    }

    /**
     * Merges all source files into a single string for AI review.
     */
    private String mergeSourceCode(Map<String, String> sourceCode) {
        StringBuilder mergedCode = new StringBuilder();
        sourceCode.forEach((filename, content) -> {
            mergedCode.append("\n// File: ").append(filename).append("\n");
            mergedCode.append(content).append("\n\n");
        });
        return mergedCode.toString();
    }

    /**
     * Sends source code to OpenAI while ensuring we stay within token limits.
     */
    private AIAnalysisResultDTO sendToAIWithTokenManagement(String fullCode, String prompts, UUID correlationId) {
        AIAnalysisResultDTO aiAnalysisResult = AIAnalysisResultDTO.builder()
                .result(new StringBuilder())
                .build();

        int tokenCount = aiProviderService.getTokens(fullCode);

        if (tokenCount <= aiProviderService.getMaxTokens()) {
            log.info("Sending full code to AI for analysis...");
            aiAnalysisResult.getResult().append(sendToAI(fullCode, prompts, correlationId));
        } else {
            log.warn("Code exceeds token limit ({} tokens), splitting into chunks...", tokenCount);
            List<String> chunks = splitCodeByTokens(fullCode);
            for (String chunk : chunks) {
                aiAnalysisResult.getResult().append(sendToAI(chunk, prompts, correlationId)).append("\n\n");
            }
        }

        log.info("AI analysis completed. correlationId {}", correlationId);
        return aiAnalysisResult;
    }


    /**
     * Splits the code into multiple parts to stay within OpenAI's token limit.
     */
    private List<String> splitCodeByTokens(String fullCode) {
        List<String> chunks = new ArrayList<>();
        String[] lines = fullCode.split("\n");
        StringBuilder currentChunk = new StringBuilder();
        int currentTokens = 0;

        for (String line : lines) {
            int lineTokens = aiProviderService.getTokens(line);
            if (currentTokens + lineTokens > aiProviderService.getMaxTokens()) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
                currentTokens = 0;
            }
            currentChunk.append(line).append("\n");
            currentTokens += lineTokens;
        }

        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString());
        }

        return chunks;
    }

    /**
     * Sends a chunk of source code to OpenAI for review.
     */
    private String sendToAI(String codeChunk, String prompts, UUID correlationId) {
        try {
            return aiProviderService.sendMessages(codeChunk, prompts, correlationId);
        } catch (Exception e) {
            log.error("Error sending code to AI. correlationId {}, error: {}", correlationId, e.getMessage());
            return "Error during AI analysis.";
        }
    }
}
