package com.tu.codeguard.service.impl;

import com.tu.codeguard.dto.AIAnalysisResultDTO;
import com.tu.codeguard.dto.Application;
import com.tu.codeguard.dto.Customer;
import com.tu.codeguard.enums.PromptOption;
import com.tu.codeguard.service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import static com.tu.codeguard.utils.RepositoryUtils.extractOwnerAndRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceCodeSubmissionServiceImpl implements SourceCodeSubmissionService {

    @NonNull
    private final AISourceCodeAnalysisService aiSourceCodeAnalysisService;

    @NonNull
    private final GitHubClient gitHubClient;

    @NonNull
    private final CustomerService customerService;

    @NonNull
    private final ApplicationService applicationService;

    @NonNull
    private final S3Service s3Service;

    @Override
    public AIAnalysisResultDTO analyzeSourceCode(String repositoryUrl, List<PromptOption> promptOptions) {
        String[] repoParts = extractOwnerAndRepo(repositoryUrl);
        String owner = repoParts[0];
        String repo = repoParts[1];

        // Export source code from repository
        byte[] bytes = gitHubClient.exportRepository(owner, repo);
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes));

        Customer customer = customerService.getCustomer(owner);

        if (customer == null) {
            // Save customer if it doesn't exist
            customer = customerService.saveCustomer(owner);
        }

        // Analyze source code
        AIAnalysisResultDTO aiAnalysisResultDTO =
                aiSourceCodeAnalysisService.analyzeSourceCode(zipInputStream, promptOptions, UUID.fromString(customer.id()));

        // Upload AI analysis result to S3
        String key = String.format("%s-%s-%s.txt", customer.id(), owner, repo);
        uploadAnalysisResultsToS3(key, aiAnalysisResultDTO.getResult().toString());

        // Save application to DB
        Application application = new Application(UUID.randomUUID().toString(), repositoryUrl, key, customer);
        applicationService.saveApplication(application);

        aiAnalysisResultDTO.setApplicationId(application.id());
        return aiAnalysisResultDTO;
    }

    private void uploadAnalysisResultsToS3(String key, String analysisResult) {
        s3Service.upload(key, analysisResult);
    }
}
