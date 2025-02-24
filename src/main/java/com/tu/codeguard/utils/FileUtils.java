package com.tu.codeguard.utils;

import com.tu.codeguard.exceptions.FileReadException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public final class FileUtils {

    private FileUtils() { throw new IllegalStateException("Static class"); }

    public static String readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(FileUtils.class.getResourceAsStream(filePath))))) {
            return reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            log.error("Failed to read the prompts from file: {}", e.getMessage(), e);
            throw new FileReadException("Failed to read the prompts from file");
        }
    }
}
