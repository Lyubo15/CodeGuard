package com.tu.codeguard.service;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.enums.PromptOption;

import java.util.List;

public interface SourceCodeSubmissionService {

    /**
     * Submission of the source code to be saved analyzed by AI and save the result in the S3 and database
     *
     * @param repositoryUrl  repository to be analyzed.
     * @return StringBuilder analysis result.
     */
    AIAnalysisResultDTO submitSourceCode(String repositoryUrl, List<PromptOption> promptOptions);

}
