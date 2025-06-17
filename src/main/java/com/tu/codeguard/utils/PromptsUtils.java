package com.tu.codeguard.utils;

import com.tu.codeguard.enums.PromptOption;

import java.util.List;

public final class PromptsUtils {

    private static final String PROMPTS_FILE_PATH = "/codeprompts/prompts.txt";

    private PromptsUtils() { throw new IllegalStateException("Static class"); }

    public static String getPrompts() {
        return FileUtils.readFile(PROMPTS_FILE_PATH);
    }

    public static String combinePromptsFromOptions(List<PromptOption> promptOptions) {
        StringBuilder sb = new StringBuilder();
        sb.append("Please review the code according to the following criteria:\n");

        for (int i = 1; i <= promptOptions.size(); i++) {
            sb.append(String.format("%s.", i)).append(promptOptions.get(i - 1).getPromptText()).append("\n");
        }

        sb.append("Answer in detail for all the prompts!");
        return sb.toString();
    }
}
