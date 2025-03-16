package com.tu.codeguard.controller;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.enums.PromptOption;
import com.tu.codeguard.service.SourceCodeSubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-analysis")
@Tag(name = "AI Sourcecode Analysis Controller", description = "Endpoints handling public operations for AI sourcecode analysis")
public class PublicAISourcecodeAnalysisController {

    @NonNull
    private final SourceCodeSubmissionService sourceCodeSubmissionService;

    @Operation(summary = "Endpoint to analyze source code by AI with dynamic prompts")
    @PostMapping
    public AIAnalysisResultDTO analyseSourcecode(
            @RequestParam String repositoryUrl,
            @RequestParam List<PromptOption> promptOptions)
    {
        return sourceCodeSubmissionService.analyzeSourceCode(repositoryUrl, promptOptions);
    }
}
