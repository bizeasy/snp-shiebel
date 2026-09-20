package com.snp.service;

import com.snp.domain.FileFolder;
import com.snp.repository.FileFolderRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FileFolder}.
 */
@Service
@Transactional
public class FileFolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FileFolderService.class);

    private final FileFolderRepository fileFolderRepository;

    public FileFolderService(FileFolderRepository fileFolderRepository) {
        this.fileFolderRepository = fileFolderRepository;
    }

    /**
     * Save a fileFolder.
     *
     * @param fileFolder the entity to save.
     * @return the persisted entity.
     */
    public FileFolder save(FileFolder fileFolder) {
        LOG.debug("Request to save FileFolder : {}", fileFolder);
        return fileFolderRepository.save(fileFolder);
    }

    /**
     * Update a fileFolder.
     *
     * @param fileFolder the entity to save.
     * @return the persisted entity.
     */
    public FileFolder update(FileFolder fileFolder) {
        LOG.debug("Request to update FileFolder : {}", fileFolder);
        return fileFolderRepository.save(fileFolder);
    }

    /**
     * Partially update a fileFolder.
     *
     * @param fileFolder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FileFolder> partialUpdate(FileFolder fileFolder) {
        LOG.debug("Request to partially update FileFolder : {}", fileFolder);

        return fileFolderRepository
            .findById(fileFolder.getId())
            .map(existingFileFolder -> {
                updateIfPresent(existingFileFolder::setName, fileFolder.getName());
                updateIfPresent(existingFileFolder::setPath, fileFolder.getPath());
                updateIfPresent(existingFileFolder::setIsSystemFolder, fileFolder.getIsSystemFolder());
                updateIfPresent(existingFileFolder::setIsTrashed, fileFolder.getIsTrashed());
                updateIfPresent(existingFileFolder::setCreatedDate, fileFolder.getCreatedDate());

                return existingFileFolder;
            })
            .map(fileFolderRepository::save);
    }

    /**
     * Get all the fileFolders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FileFolder> findAllWithEagerRelationships(Pageable pageable) {
        return fileFolderRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one fileFolder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileFolder> findOne(Long id) {
        LOG.debug("Request to get FileFolder : {}", id);
        return fileFolderRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the fileFolder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FileFolder : {}", id);
        fileFolderRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
