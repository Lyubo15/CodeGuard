package com.tu.codeguard.controller;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.service.AISourceCodeAnalysisService;
import com.tu.codeguard.service.ClamAVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipInputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-analysis")
@Tag(name = "AI Sourcecode Analysis Controller", description = "Endpoints handling admin operations for AI sourcecode analysis")
public class AdminAISourcecodeAnalysisController {

    private final AISourceCodeAnalysisService aiSourceCodeAnalysisService;
    private final ClamAVService clamAVService;

    @Operation(summary = "Endpoint to analyse sourcecode by AI")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public AIAnalysisResultDTO analyseSourcecode(@RequestPart MultipartFile file) throws IOException {
        clamAVService.checkForMaliciousFile(file);
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
        return aiSourceCodeAnalysisService.analyzeSourceCode(zipInputStream, file.getOriginalFilename(), UUID.randomUUID());
    }
}
