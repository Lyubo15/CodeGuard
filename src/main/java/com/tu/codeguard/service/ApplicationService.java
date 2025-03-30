package com.tu.codeguard.service;

import com.tu.codeguard.dto.Application;
import com.tu.codeguard.dto.ApplicationDTO;

import java.util.List;

public interface ApplicationService {

    /**
     * Get all applications
     */
    List<ApplicationDTO> getAllApplications();

    /**
     * Get application details
     */
    String getApplicationDetailsById(String id);

    /**
     * Save application
     *
     * @param application to be saved.
     */
    void saveApplication(Application application);

    /**
     * Delete application
     */
    void deleteApplicationById(String id);
}
