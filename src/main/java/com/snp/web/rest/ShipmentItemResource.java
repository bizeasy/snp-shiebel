package com.snp.web.rest;

import com.snp.domain.ShipmentItem;
import com.snp.repository.ShipmentItemRepository;
import com.snp.service.ShipmentItemQueryService;
import com.snp.service.ShipmentItemService;
import com.snp.service.criteria.ShipmentItemCriteria;
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
 * REST controller for managing {@link com.snp.domain.ShipmentItem}.
 */
@RestController
@RequestMapping("/api/shipment-items")
public class ShipmentItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentItemResource.class);

    private static final String ENTITY_NAME = "shipmentItem";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShipmentItemService shipmentItemService;

    private final ShipmentItemRepository shipmentItemRepository;

    private final ShipmentItemQueryService shipmentItemQueryService;

    public ShipmentItemResource(
        ShipmentItemService shipmentItemService,
        ShipmentItemRepository shipmentItemRepository,
        ShipmentItemQueryService shipmentItemQueryService
    ) {
        this.shipmentItemService = shipmentItemService;
        this.shipmentItemRepository = shipmentItemRepository;
        this.shipmentItemQueryService = shipmentItemQueryService;
    }

    /**
     * {@code POST  /shipment-items} : Create a new shipmentItem.
     *
     * @param shipmentItem the shipmentItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentItem, or with status {@code 400 (Bad Request)} if the shipmentItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentItem> createShipmentItem(@Valid @RequestBody ShipmentItem shipmentItem) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentItem : {}", shipmentItem);
        if (shipmentItem.getId() != null) {
            throw new BadRequestAlertException("A new shipmentItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentItem = shipmentItemService.save(shipmentItem);
        return ResponseEntity.created(new URI("/api/shipment-items/" + shipmentItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentItem.getId().toString()))
            .body(shipmentItem);
    }

    /**
     * {@code PUT  /shipment-items/:id} : Updates an existing shipmentItem.
     *
     * @param id the id of the shipmentItem to save.
     * @param shipmentItem the shipmentItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentItem,
     * or with status {@code 400 (Bad Request)} if the shipmentItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentItem> updateShipmentItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentItem shipmentItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentItem : {}, {}", id, shipmentItem);
        if (shipmentItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentItem = shipmentItemService.update(shipmentItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentItem.getId().toString()))
            .body(shipmentItem);
    }

    /**
     * {@code PATCH  /shipment-items/:id} : Partial updates given fields of an existing shipmentItem, field will ignore if it is null
     *
     * @param id the id of the shipmentItem to save.
     * @param shipmentItem the shipmentItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentItem,
     * or with status {@code 400 (Bad Request)} if the shipmentItem is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentItem> partialUpdateShipmentItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentItem shipmentItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShipmentItem : {}, {}", id, shipmentItem);
        if (shipmentItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentItem> result = shipmentItemService.partialUpdate(shipmentItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentItem.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-items} : get all the Shipment Items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shipment Items in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentItem>> getAllShipmentItems(
        ShipmentItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentItems by criteria: {}", criteria);

        Page<ShipmentItem> page = shipmentItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-items/count} : count all the shipmentItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentItems(ShipmentItemCriteria criteria) {
        LOG.debug("REST request to count ShipmentItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-items/:id} : get the "id" shipmentItem.
     *
     * @param id the id of the shipmentItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentItem> getShipmentItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentItem : {}", id);
        Optional<ShipmentItem> shipmentItem = shipmentItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentItem);
    }

    /**
     * {@code DELETE  /shipment-items/:id} : delete the "id" shipmentItem.
     *
     * @param id the id of the shipmentItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentItem : {}", id);
        shipmentItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
