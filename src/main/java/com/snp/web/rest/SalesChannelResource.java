package com.snp.web.rest;

import com.snp.domain.SalesChannel;
import com.snp.repository.SalesChannelRepository;
import com.snp.service.SalesChannelQueryService;
import com.snp.service.SalesChannelService;
import com.snp.service.criteria.SalesChannelCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.SalesChannel}.
 */
@RestController
@RequestMapping("/api/sales-channels")
public class SalesChannelResource {

    private static final Logger LOG = LoggerFactory.getLogger(SalesChannelResource.class);

    private static final String ENTITY_NAME = "salesChannel";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final SalesChannelService salesChannelService;

    private final SalesChannelRepository salesChannelRepository;

    private final SalesChannelQueryService salesChannelQueryService;

    public SalesChannelResource(
        SalesChannelService salesChannelService,
        SalesChannelRepository salesChannelRepository,
        SalesChannelQueryService salesChannelQueryService
    ) {
        this.salesChannelService = salesChannelService;
        this.salesChannelRepository = salesChannelRepository;
        this.salesChannelQueryService = salesChannelQueryService;
    }

    /**
     * {@code POST  /sales-channels} : Create a new salesChannel.
     *
     * @param salesChannel the salesChannel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salesChannel, or with status {@code 400 (Bad Request)} if the salesChannel already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SalesChannel> createSalesChannel(@Valid @RequestBody SalesChannel salesChannel) throws URISyntaxException {
        LOG.debug("REST request to save SalesChannel : {}", salesChannel);
        if (salesChannel.getId() != null) {
            throw new BadRequestAlertException("A new salesChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        salesChannel = salesChannelService.save(salesChannel);
        return ResponseEntity.created(new URI("/api/sales-channels/" + salesChannel.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, salesChannel.getId().toString()))
            .body(salesChannel);
    }

    /**
     * {@code PUT  /sales-channels/:id} : Updates an existing salesChannel.
     *
     * @param id the id of the salesChannel to save.
     * @param salesChannel the salesChannel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesChannel,
     * or with status {@code 400 (Bad Request)} if the salesChannel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salesChannel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalesChannel> updateSalesChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SalesChannel salesChannel
    ) throws URISyntaxException {
        LOG.debug("REST request to update SalesChannel : {}, {}", id, salesChannel);
        if (salesChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salesChannel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salesChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        salesChannel = salesChannelService.update(salesChannel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesChannel.getId().toString()))
            .body(salesChannel);
    }

    /**
     * {@code PATCH  /sales-channels/:id} : Partial updates given fields of an existing salesChannel, field will ignore if it is null
     *
     * @param id the id of the salesChannel to save.
     * @param salesChannel the salesChannel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesChannel,
     * or with status {@code 400 (Bad Request)} if the salesChannel is not valid,
     * or with status {@code 404 (Not Found)} if the salesChannel is not found,
     * or with status {@code 500 (Internal Server Error)} if the salesChannel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalesChannel> partialUpdateSalesChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SalesChannel salesChannel
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update SalesChannel : {}, {}", id, salesChannel);
        if (salesChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salesChannel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salesChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalesChannel> result = salesChannelService.partialUpdate(salesChannel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesChannel.getId().toString())
        );
    }

    /**
     * {@code GET  /sales-channels} : get all the Sales Channels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Sales Channels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SalesChannel>> getAllSalesChannels(SalesChannelCriteria criteria) {
        LOG.debug("REST request to get SalesChannels by criteria: {}", criteria);

        List<SalesChannel> entityList = salesChannelQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sales-channels/count} : count all the salesChannels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSalesChannels(SalesChannelCriteria criteria) {
        LOG.debug("REST request to count SalesChannels by criteria: {}", criteria);
        return ResponseEntity.ok().body(salesChannelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sales-channels/:id} : get the "id" salesChannel.
     *
     * @param id the id of the salesChannel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salesChannel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalesChannel> getSalesChannel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SalesChannel : {}", id);
        Optional<SalesChannel> salesChannel = salesChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salesChannel);
    }

    /**
     * {@code DELETE  /sales-channels/:id} : delete the "id" salesChannel.
     *
     * @param id the id of the salesChannel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesChannel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SalesChannel : {}", id);
        salesChannelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
