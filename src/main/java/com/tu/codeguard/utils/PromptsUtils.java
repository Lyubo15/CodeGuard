package com.tu.codeguard.utils;

import java.util.HashMap;
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
}
