package com.tu.codeguard.service;

import com.tu.codeguard.dto.AIAnalysisResultDTO;

import java.util.UUID;
import java.util.zip.ZipInputStream;

public interface AISourceCodeAnalysisService {

    /**
     * Declaration of a method to analyze the source code with AI
     *
     * @param zip      is the source code zip file.
     * @param filename is the source code file name
     * @return StringBuilder - analysis result
     */
    AIAnalysisResultDTO analyzeSourceCode(ZipInputStream zip, String filename, UUID correlationId);
}
