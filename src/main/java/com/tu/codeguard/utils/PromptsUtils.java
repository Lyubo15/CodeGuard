package com.tu.codeguard.utils;

import com.tu.codeguard.enums.PromptOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PromptsUtils {

//    private static final String PROMPTS_FILE_PATH = "/codeprompts/%s-prompts.txt";
//
//    private PromptsUtils() { throw new IllegalStateException("Static class"); }
//
//    private static final Map<String, String> prompts = new HashMap<>();
//
//    public static String getPrompts(String segment) {
//        if (!PromptsUtils.prompts.containsKey(segment)) {
//            String fileName = String.format(PROMPTS_FILE_PATH, segment);
//            String prompts = FileUtils.readFile(fileName);
//
//            PromptsUtils.prompts.putIfAbsent(segment, prompts);
//        }
//
//        return PromptsUtils.prompts.get(segment);
//    }

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
