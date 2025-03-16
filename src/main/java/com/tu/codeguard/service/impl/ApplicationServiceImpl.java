package com.tu.codeguard.service.impl;

import com.tu.codeguard.dbo.ApplicationEntity;
import com.tu.codeguard.dto.Application;
import com.tu.codeguard.repository.ApplicationJpaRepository;
import com.tu.codeguard.service.ApplicationService;
import com.tu.codeguard.utils.Mapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    @NonNull
    private final ApplicationJpaRepository applicationJpaRepository;

    @Override
    public void saveApplication(Application application) {
        log.info("Saving application on db: {}", application);
        ApplicationEntity applicationEntity = Mapper.mapApplicationToEntity(application);
        applicationJpaRepository.save(applicationEntity);
    }
}
