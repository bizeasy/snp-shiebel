package com.snp.service;

import com.snp.domain.FileShare;
import com.snp.repository.FileShareRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FileShare}.
 */
@Service
@Transactional
public class FileShareService {

    private static final Logger LOG = LoggerFactory.getLogger(FileShareService.class);

    private final FileShareRepository fileShareRepository;

    public FileShareService(FileShareRepository fileShareRepository) {
        this.fileShareRepository = fileShareRepository;
    }

    /**
     * Save a fileShare.
     *
     * @param fileShare the entity to save.
     * @return the persisted entity.
     */
    public FileShare save(FileShare fileShare) {
        LOG.debug("Request to save FileShare : {}", fileShare);
        return fileShareRepository.save(fileShare);
    }

    /**
     * Update a fileShare.
     *
     * @param fileShare the entity to save.
     * @return the persisted entity.
     */
    public FileShare update(FileShare fileShare) {
        LOG.debug("Request to update FileShare : {}", fileShare);
        return fileShareRepository.save(fileShare);
    }

    /**
     * Partially update a fileShare.
     *
     * @param fileShare the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FileShare> partialUpdate(FileShare fileShare) {
        LOG.debug("Request to partially update FileShare : {}", fileShare);

        return fileShareRepository
            .findById(fileShare.getId())
            .map(existingFileShare -> {
                updateIfPresent(existingFileShare::setPermission, fileShare.getPermission());
                updateIfPresent(existingFileShare::setSharedWithEmail, fileShare.getSharedWithEmail());
                updateIfPresent(existingFileShare::setSharedDate, fileShare.getSharedDate());
                updateIfPresent(existingFileShare::setExpiryDate, fileShare.getExpiryDate());
                updateIfPresent(existingFileShare::setAccessToken, fileShare.getAccessToken());
                updateIfPresent(existingFileShare::setIsRevoked, fileShare.getIsRevoked());

                return existingFileShare;
            })
            .map(fileShareRepository::save);
    }

    /**
     * Get all the fileShares with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FileShare> findAllWithEagerRelationships(Pageable pageable) {
        return fileShareRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one fileShare by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileShare> findOne(Long id) {
        LOG.debug("Request to get FileShare : {}", id);
        return fileShareRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the fileShare by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FileShare : {}", id);
        fileShareRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
