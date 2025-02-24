package com.tu.codeguard.service.impl;

import static com.tu.codeguard.utils.Constants.allowedExtensions;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.tu.codeguard.exceptions.FileReadException;
import com.tu.codeguard.service.ZipService;
import com.tu.codeguard.utils.CodeMinifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ZipServiceImpl implements ZipService {

    public Map<String, String> readSourceCode(ZipInputStream zip) {
        Map<String, String> sourceCode = new HashMap<>();

        try {
            ZipEntry zipEntry;

            while ((zipEntry = zip.getNextEntry()) != null) {
                if (shouldProcessReading(zipEntry)) {
                    String classCode = extractClass(zip);
                    String minifiedClassCode = CodeMinifier.minifyCode(classCode);
                    sourceCode.put(extractFileName(zipEntry), minifiedClassCode);
                }
            }
        } catch (IOException e) {
            log.error("Error reading source code from zip file: {}", e.getMessage(), e);
            throw new FileReadException("Failed to read source code from zip file");
        }

        log.info("Read source code from the .zip file is done");
        return sourceCode;
    }

    private String extractClass(ZipInputStream zip) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int byteValue;

            while ((byteValue = zip.read()) != -1) {
                byteArrayOutputStream.write(byteValue);
            }

            return byteArrayOutputStream.toString(UTF_8);
        }
    }

    private boolean shouldProcessReading(ZipEntry zipEntry) {
        String fileName = zipEntry.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return !zipEntry.isDirectory() && isAllowedExtension(fileExtension);
    }

    private String extractFileName(ZipEntry zipEntry) {
        Path path = Paths.get(zipEntry.getName());
        String fileName = path.getFileName().toString();
        String parentFolder = path.getParent().getFileName().toString();

        return String.format("%s/%s", parentFolder, fileName);
    }

    private boolean isAllowedExtension(String fileExtension) {
        return allowedExtensions.stream().anyMatch(fileExtension::equals);
    }
}
