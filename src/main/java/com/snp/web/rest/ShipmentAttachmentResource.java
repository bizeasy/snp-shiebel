package com.snp.web.rest;

import com.snp.domain.ShipmentAttachment;
import com.snp.repository.ShipmentAttachmentRepository;
import com.snp.service.ShipmentAttachmentQueryService;
import com.snp.service.ShipmentAttachmentService;
import com.snp.service.criteria.ShipmentAttachmentCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.ShipmentAttachment}.
 */
@RestController
@RequestMapping("/api/shipment-attachments")
public class ShipmentAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentAttachmentResource.class);

    private static final String ENTITY_NAME = "shipmentAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShipmentAttachmentService shipmentAttachmentService;

    private final ShipmentAttachmentRepository shipmentAttachmentRepository;

    private final ShipmentAttachmentQueryService shipmentAttachmentQueryService;

    public ShipmentAttachmentResource(
        ShipmentAttachmentService shipmentAttachmentService,
        ShipmentAttachmentRepository shipmentAttachmentRepository,
        ShipmentAttachmentQueryService shipmentAttachmentQueryService
    ) {
        this.shipmentAttachmentService = shipmentAttachmentService;
        this.shipmentAttachmentRepository = shipmentAttachmentRepository;
        this.shipmentAttachmentQueryService = shipmentAttachmentQueryService;
    }

    /**
     * {@code POST  /shipment-attachments} : Create a new shipmentAttachment.
     *
     * @param shipmentAttachment the shipmentAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentAttachment, or with status {@code 400 (Bad Request)} if the shipmentAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentAttachment> createShipmentAttachment(@Valid @RequestBody ShipmentAttachment shipmentAttachment)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentAttachment : {}", shipmentAttachment);
        if (shipmentAttachment.getId() != null) {
            throw new BadRequestAlertException("A new shipmentAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentAttachment = shipmentAttachmentService.save(shipmentAttachment);
        return ResponseEntity.created(new URI("/api/shipment-attachments/" + shipmentAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentAttachment.getId().toString()))
            .body(shipmentAttachment);
    }

    /**
     * {@code PUT  /shipment-attachments/:id} : Updates an existing shipmentAttachment.
     *
     * @param id the id of the shipmentAttachment to save.
     * @param shipmentAttachment the shipmentAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentAttachment,
     * or with status {@code 400 (Bad Request)} if the shipmentAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentAttachment> updateShipmentAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentAttachment shipmentAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentAttachment : {}, {}", id, shipmentAttachment);
        if (shipmentAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentAttachment = shipmentAttachmentService.update(shipmentAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentAttachment.getId().toString()))
            .body(shipmentAttachment);
    }

    /**
     * {@code PATCH  /shipment-attachments/:id} : Partial updates given fields of an existing shipmentAttachment, field will ignore if it is null
     *
     * @param id the id of the shipmentAttachment to save.
     * @param shipmentAttachment the shipmentAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentAttachment,
     * or with status {@code 400 (Bad Request)} if the shipmentAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentAttachment> partialUpdateShipmentAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentAttachment shipmentAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShipmentAttachment : {}, {}", id, shipmentAttachment);
        if (shipmentAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentAttachment> result = shipmentAttachmentService.partialUpdate(shipmentAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-attachments} : get all the Shipment Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shipment Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentAttachment>> getAllShipmentAttachments(
        ShipmentAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentAttachments by criteria: {}", criteria);

        Page<ShipmentAttachment> page = shipmentAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-attachments/count} : count all the shipmentAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentAttachments(ShipmentAttachmentCriteria criteria) {
        LOG.debug("REST request to count ShipmentAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-attachments/:id} : get the "id" shipmentAttachment.
     *
     * @param id the id of the shipmentAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentAttachment> getShipmentAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentAttachment : {}", id);
        Optional<ShipmentAttachment> shipmentAttachment = shipmentAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentAttachment);
    }

    /**
     * {@code DELETE  /shipment-attachments/:id} : delete the "id" shipmentAttachment.
     *
     * @param id the id of the shipmentAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentAttachment : {}", id);
        shipmentAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
