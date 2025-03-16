package com.tu.codeguard.service;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.enums.PromptOption;

import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;

public interface AISourceCodeAnalysisService {

    /**
     * Declaration of a method to analyze the source code with AI
     *
     * @param zip      is the source code zip file.
     * @return StringBuilder - analysis result
     */
    AIAnalysisResultDTO analyzeSourceCode(ZipInputStream zip, List<PromptOption> promptOptions, UUID correlationId);
}
