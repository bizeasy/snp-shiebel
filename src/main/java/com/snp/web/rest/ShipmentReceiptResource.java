package com.snp.web.rest;

import com.snp.domain.ShipmentReceipt;
import com.snp.repository.ShipmentReceiptRepository;
import com.snp.service.ShipmentReceiptQueryService;
import com.snp.service.ShipmentReceiptService;
import com.snp.service.criteria.ShipmentReceiptCriteria;
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
 * REST controller for managing {@link com.snp.domain.ShipmentReceipt}.
 */
@RestController
@RequestMapping("/api/shipment-receipts")
public class ShipmentReceiptResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentReceiptResource.class);

    private static final String ENTITY_NAME = "shipmentReceipt";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShipmentReceiptService shipmentReceiptService;

    private final ShipmentReceiptRepository shipmentReceiptRepository;

    private final ShipmentReceiptQueryService shipmentReceiptQueryService;

    public ShipmentReceiptResource(
        ShipmentReceiptService shipmentReceiptService,
        ShipmentReceiptRepository shipmentReceiptRepository,
        ShipmentReceiptQueryService shipmentReceiptQueryService
    ) {
        this.shipmentReceiptService = shipmentReceiptService;
        this.shipmentReceiptRepository = shipmentReceiptRepository;
        this.shipmentReceiptQueryService = shipmentReceiptQueryService;
    }

    /**
     * {@code POST  /shipment-receipts} : Create a new shipmentReceipt.
     *
     * @param shipmentReceipt the shipmentReceipt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentReceipt, or with status {@code 400 (Bad Request)} if the shipmentReceipt already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentReceipt> createShipmentReceipt(@Valid @RequestBody ShipmentReceipt shipmentReceipt)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentReceipt : {}", shipmentReceipt);
        if (shipmentReceipt.getId() != null) {
            throw new BadRequestAlertException("A new shipmentReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentReceipt = shipmentReceiptService.save(shipmentReceipt);
        return ResponseEntity.created(new URI("/api/shipment-receipts/" + shipmentReceipt.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentReceipt.getId().toString()))
            .body(shipmentReceipt);
    }

    /**
     * {@code PUT  /shipment-receipts/:id} : Updates an existing shipmentReceipt.
     *
     * @param id the id of the shipmentReceipt to save.
     * @param shipmentReceipt the shipmentReceipt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentReceipt,
     * or with status {@code 400 (Bad Request)} if the shipmentReceipt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentReceipt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentReceipt> updateShipmentReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentReceipt shipmentReceipt
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentReceipt : {}, {}", id, shipmentReceipt);
        if (shipmentReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentReceipt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentReceipt = shipmentReceiptService.update(shipmentReceipt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentReceipt.getId().toString()))
            .body(shipmentReceipt);
    }

    /**
     * {@code PATCH  /shipment-receipts/:id} : Partial updates given fields of an existing shipmentReceipt, field will ignore if it is null
     *
     * @param id the id of the shipmentReceipt to save.
     * @param shipmentReceipt the shipmentReceipt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentReceipt,
     * or with status {@code 400 (Bad Request)} if the shipmentReceipt is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentReceipt is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentReceipt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentReceipt> partialUpdateShipmentReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentReceipt shipmentReceipt
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShipmentReceipt : {}, {}", id, shipmentReceipt);
        if (shipmentReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentReceipt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentReceipt> result = shipmentReceiptService.partialUpdate(shipmentReceipt);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentReceipt.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-receipts} : get all the Shipment Receipts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shipment Receipts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentReceipt>> getAllShipmentReceipts(
        ShipmentReceiptCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentReceipts by criteria: {}", criteria);

        Page<ShipmentReceipt> page = shipmentReceiptQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-receipts/count} : count all the shipmentReceipts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentReceipts(ShipmentReceiptCriteria criteria) {
        LOG.debug("REST request to count ShipmentReceipts by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentReceiptQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-receipts/:id} : get the "id" shipmentReceipt.
     *
     * @param id the id of the shipmentReceipt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentReceipt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentReceipt> getShipmentReceipt(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentReceipt : {}", id);
        Optional<ShipmentReceipt> shipmentReceipt = shipmentReceiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentReceipt);
    }

    /**
     * {@code DELETE  /shipment-receipts/:id} : delete the "id" shipmentReceipt.
     *
     * @param id the id of the shipmentReceipt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentReceipt(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentReceipt : {}", id);
        shipmentReceiptService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
