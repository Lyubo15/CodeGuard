package com.tu.codeguard.service.impl;

import com.tu.codeguard.dbo.ApplicationEntity;
import com.tu.codeguard.dto.Application;
import com.tu.codeguard.dto.ApplicationDTO;
import com.tu.codeguard.exceptions.EntityNotFoundException;
import com.tu.codeguard.repository.ApplicationJpaRepository;
import com.tu.codeguard.service.ApplicationService;
import com.tu.codeguard.utils.Mapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    @NonNull
    private final ApplicationJpaRepository applicationJpaRepository;
    @NonNull
    private final S3Service s3Service;

    @Override
    public List<ApplicationDTO> getAllApplications() {
        log.debug("Retrieving all applications");
        List<ApplicationEntity> entities = applicationJpaRepository
                .findAll()
                .stream()
                .filter(entity -> !entity.isDeleted())
                .toList();
        return entities.stream().map(Mapper::mapApplicationToApplicationDTO).toList();
    }

    @Override
    public List<ApplicationDTO> getAllDeletedApplications() {
        log.debug("Retrieving all deleted applications");
        List<ApplicationEntity> entities = applicationJpaRepository
                .findAll()
                .stream()
                .filter(ApplicationEntity::isDeleted)
                .toList();
        return entities.stream().map(Mapper::mapApplicationToApplicationDTO).toList();
    }

    @Override
    public String getApplicationDetailsById(String id) {
        ApplicationEntity entity = applicationJpaRepository.findById(id).orElse(null);
        if (entity == null) { return null; }
        if (entity.isDeleted()) { return null; }

        return s3Service.downloadTxtFile(entity.getAiResultFilePath());
    }

    @Override
    public void saveApplication(Application application) {
        log.debug("Saving application on db: {}", application);
        ApplicationEntity applicationEntity = Mapper.mapApplicationToEntity(application);
        applicationJpaRepository.save(applicationEntity);
    }

    @Override
    public void deleteApplicationById(String id) {
        log.debug("Deleting permanently application by id: {}", id);

        Optional<ApplicationEntity> applicationEntity = applicationJpaRepository.findById(id);

        if (applicationEntity.isEmpty()) {
            throw new EntityNotFoundException("Entity with id " + id + " not found when trying to delete");
        }

        s3Service.delete(applicationEntity.get().getAiResultFilePath());
        applicationJpaRepository.deleteById(id);
    }

    @Override
    public void softDeleteApplicationById(String id) {
        log.debug("Soft deleting application by id: {}", id);
        Optional<ApplicationEntity> applicationEntity = applicationJpaRepository.findById(id);

        if (applicationEntity.isEmpty()) {
            throw new EntityNotFoundException("Entity with id " + id + " not found when trying to softDelete");
        }

        applicationEntity.get().setDeleted(true);
        applicationJpaRepository.save(applicationEntity.get());
    }

    @Override
    public void recoverDeletedApplicationById(String id) {
        log.debug("Recover deleted application by id: {}", id);
        Optional<ApplicationEntity> applicationEntity = applicationJpaRepository.findById(id);

        if (applicationEntity.isEmpty()) {
            throw new EntityNotFoundException("Entity with id " + id + " not found when trying to recover");
        }

        applicationEntity.get().setDeleted(false);
        applicationJpaRepository.save(applicationEntity.get());
    }
}
