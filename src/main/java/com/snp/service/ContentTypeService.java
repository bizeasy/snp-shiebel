package com.snp.service;

import com.snp.domain.ContentType;
import com.snp.repository.ContentTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ContentType}.
 */
@Service
@Transactional
public class ContentTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentTypeService.class);

    private final ContentTypeRepository contentTypeRepository;

    public ContentTypeService(ContentTypeRepository contentTypeRepository) {
        this.contentTypeRepository = contentTypeRepository;
    }

    /**
     * Save a contentType.
     *
     * @param contentType the entity to save.
     * @return the persisted entity.
     */
    public ContentType save(ContentType contentType) {
        LOG.debug("Request to save ContentType : {}", contentType);
        return contentTypeRepository.save(contentType);
    }

    /**
     * Update a contentType.
     *
     * @param contentType the entity to save.
     * @return the persisted entity.
     */
    public ContentType update(ContentType contentType) {
        LOG.debug("Request to update ContentType : {}", contentType);
        return contentTypeRepository.save(contentType);
    }

    /**
     * Partially update a contentType.
     *
     * @param contentType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContentType> partialUpdate(ContentType contentType) {
        LOG.debug("Request to partially update ContentType : {}", contentType);

        return contentTypeRepository
            .findById(contentType.getId())
            .map(existingContentType -> {
                updateIfPresent(existingContentType::setName, contentType.getName());

                return existingContentType;
            })
            .map(contentTypeRepository::save);
    }

    /**
     * Get one contentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContentType> findOne(Long id) {
        LOG.debug("Request to get ContentType : {}", id);
        return contentTypeRepository.findById(id);
    }

    /**
     * Delete the contentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContentType : {}", id);
        contentTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
