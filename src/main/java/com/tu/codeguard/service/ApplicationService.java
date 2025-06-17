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
     * Get all deleted applications
     */
    List<ApplicationDTO> getAllDeletedApplications();

    /**
     * Get application details
     *
     * @param id of the application
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
     *
     * @param id of the application
     */
    void deleteApplicationById(String id);

    /**
     * Delete application
     *
     * @param id of the application
     */
    void softDeleteApplicationById(String id);

    /**
     * Recover deleted application
     *
     * @param id of the application
     */
    void recoverDeletedApplicationById(String id);
}
