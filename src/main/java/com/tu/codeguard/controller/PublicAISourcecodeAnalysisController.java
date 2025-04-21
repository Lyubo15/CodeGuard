package com.tu.codeguard.controller;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.dto.ApplicationDTO;
import com.tu.codeguard.enums.PromptOption;
import com.tu.codeguard.service.ApplicationService;
import com.tu.codeguard.service.SourceCodeSubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-analysis")
@Tag(name = "AI Sourcecode Analysis Controller", description = "Endpoints handling public operations for AI sourcecode analysis")
public class PublicAISourcecodeAnalysisController {

    @NonNull
    private final SourceCodeSubmissionService sourceCodeSubmissionService;

    @NonNull
    private final ApplicationService applicationService;

    @Operation(summary = "Endpoint to retrieve all applications")
    @GetMapping("/applications")
    public List<ApplicationDTO> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Endpoint to retrieve all deleted applications")
    @GetMapping("/deleted/applications")
    public List<ApplicationDTO> getAllDeletedApplications() {
        return applicationService.getAllDeletedApplications();
    }

    @Operation(summary = "Endpoint to retrieve AI result")
    @GetMapping("/application/{id}")
    public String getApplicationResult(@PathVariable String id) {
        return applicationService.getApplicationDetailsById(id);
    }

    @Operation(summary = "Endpoint to analyze source code by AI with dynamic prompts")
    @PostMapping
    public AIAnalysisResultDTO analyseSourcecode(
            @RequestParam String repositoryUrl,
            @RequestParam List<PromptOption> promptOptions)
    {
        return sourceCodeSubmissionService.analyzeSourceCode(repositoryUrl, promptOptions);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Endpoint to delete application")
    @DeleteMapping("/application/{id}")
    public void deleteApplication(@PathVariable String id) {
        applicationService.deleteApplicationById(id);
    }

    @Operation(summary = "Endpoint to soft delete application")
    @DeleteMapping("/application/{id}/soft")
    public void softDeleteApplication(@PathVariable String id) {
        applicationService.softDeleteApplicationById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Endpoint to recover deleted application")
    @PutMapping("/application/{id}/recover")
    public void recoverDeletedApplication(@PathVariable String id) {
        applicationService.recoverDeletedApplicationById(id);
    }
}
