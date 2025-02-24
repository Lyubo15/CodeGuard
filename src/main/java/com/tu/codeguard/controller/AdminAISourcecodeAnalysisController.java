package com.tu.codeguard.controller;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.enums.PromptOption;
import com.tu.codeguard.service.AISourceCodeAnalysisService;
import com.tu.codeguard.service.ClamAVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/ai-analysis")
@Tag(name = "AI Sourcecode Analysis Controller", description = "Endpoints handling admin operations for AI sourcecode analysis")
public class AdminAISourcecodeAnalysisController {

    private final AISourceCodeAnalysisService aiSourceCodeAnalysisService;
    private final ClamAVService clamAVService;

    @Operation(summary = "Endpoint to analyze source code by AI with dynamic prompts")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public AIAnalysisResultDTO analyseSourcecode(
            @RequestPart MultipartFile file,
            @RequestParam(required = false) List<PromptOption> promptOptions) throws IOException
    {
        clamAVService.checkForMaliciousFile(file);
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
        return aiSourceCodeAnalysisService.analyzeSourceCode(
                zipInputStream,
                promptOptions,
                file.getOriginalFilename(),
                UUID.randomUUID()
        );
    }
}
