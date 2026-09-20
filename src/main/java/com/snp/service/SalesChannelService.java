package com.snp.service;

import com.snp.domain.SalesChannel;
import com.snp.repository.SalesChannelRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.SalesChannel}.
 */
@Service
@Transactional
public class SalesChannelService {

    private static final Logger LOG = LoggerFactory.getLogger(SalesChannelService.class);

    private final SalesChannelRepository salesChannelRepository;

    public SalesChannelService(SalesChannelRepository salesChannelRepository) {
        this.salesChannelRepository = salesChannelRepository;
    }

    /**
     * Save a salesChannel.
     *
     * @param salesChannel the entity to save.
     * @return the persisted entity.
     */
    public SalesChannel save(SalesChannel salesChannel) {
        LOG.debug("Request to save SalesChannel : {}", salesChannel);
        return salesChannelRepository.save(salesChannel);
    }

    /**
     * Update a salesChannel.
     *
     * @param salesChannel the entity to save.
     * @return the persisted entity.
     */
    public SalesChannel update(SalesChannel salesChannel) {
        LOG.debug("Request to update SalesChannel : {}", salesChannel);
        return salesChannelRepository.save(salesChannel);
    }

    /**
     * Partially update a salesChannel.
     *
     * @param salesChannel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalesChannel> partialUpdate(SalesChannel salesChannel) {
        LOG.debug("Request to partially update SalesChannel : {}", salesChannel);

        return salesChannelRepository
            .findById(salesChannel.getId())
            .map(existingSalesChannel -> {
                updateIfPresent(existingSalesChannel::setName, salesChannel.getName());
                updateIfPresent(existingSalesChannel::setDescription, salesChannel.getDescription());
                updateIfPresent(existingSalesChannel::setSequenceNo, salesChannel.getSequenceNo());

                return existingSalesChannel;
            })
            .map(salesChannelRepository::save);
    }

    /**
     * Get one salesChannel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalesChannel> findOne(Long id) {
        LOG.debug("Request to get SalesChannel : {}", id);
        return salesChannelRepository.findById(id);
    }

    /**
     * Delete the salesChannel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SalesChannel : {}", id);
        salesChannelRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
