package com.snp.web.rest;

import com.snp.domain.ShipmentType;
import com.snp.repository.ShipmentTypeRepository;
import com.snp.service.ShipmentTypeQueryService;
import com.snp.service.ShipmentTypeService;
import com.snp.service.criteria.ShipmentTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ShipmentType}.
 */
@RestController
@RequestMapping("/api/shipment-types")
public class ShipmentTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentTypeResource.class);

    private static final String ENTITY_NAME = "shipmentType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShipmentTypeService shipmentTypeService;

    private final ShipmentTypeRepository shipmentTypeRepository;

    private final ShipmentTypeQueryService shipmentTypeQueryService;

    public ShipmentTypeResource(
        ShipmentTypeService shipmentTypeService,
        ShipmentTypeRepository shipmentTypeRepository,
        ShipmentTypeQueryService shipmentTypeQueryService
    ) {
        this.shipmentTypeService = shipmentTypeService;
        this.shipmentTypeRepository = shipmentTypeRepository;
        this.shipmentTypeQueryService = shipmentTypeQueryService;
    }

    /**
     * {@code POST  /shipment-types} : Create a new shipmentType.
     *
     * @param shipmentType the shipmentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentType, or with status {@code 400 (Bad Request)} if the shipmentType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentType> createShipmentType(@Valid @RequestBody ShipmentType shipmentType) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentType : {}", shipmentType);
        if (shipmentType.getId() != null) {
            throw new BadRequestAlertException("A new shipmentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentType = shipmentTypeService.save(shipmentType);
        return ResponseEntity.created(new URI("/api/shipment-types/" + shipmentType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentType.getId().toString()))
            .body(shipmentType);
    }

    /**
     * {@code PUT  /shipment-types/:id} : Updates an existing shipmentType.
     *
     * @param id the id of the shipmentType to save.
     * @param shipmentType the shipmentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentType,
     * or with status {@code 400 (Bad Request)} if the shipmentType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentType> updateShipmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentType shipmentType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentType : {}, {}", id, shipmentType);
        if (shipmentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentType = shipmentTypeService.update(shipmentType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentType.getId().toString()))
            .body(shipmentType);
    }

    /**
     * {@code PATCH  /shipment-types/:id} : Partial updates given fields of an existing shipmentType, field will ignore if it is null
     *
     * @param id the id of the shipmentType to save.
     * @param shipmentType the shipmentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentType,
     * or with status {@code 400 (Bad Request)} if the shipmentType is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentType is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentType> partialUpdateShipmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentType shipmentType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShipmentType : {}, {}", id, shipmentType);
        if (shipmentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentType> result = shipmentTypeService.partialUpdate(shipmentType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentType.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-types} : get all the Shipment Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shipment Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentType>> getAllShipmentTypes(ShipmentTypeCriteria criteria) {
        LOG.debug("REST request to get ShipmentTypes by criteria: {}", criteria);

        List<ShipmentType> entityList = shipmentTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /shipment-types/count} : count all the shipmentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentTypes(ShipmentTypeCriteria criteria) {
        LOG.debug("REST request to count ShipmentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-types/:id} : get the "id" shipmentType.
     *
     * @param id the id of the shipmentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentType> getShipmentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentType : {}", id);
        Optional<ShipmentType> shipmentType = shipmentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentType);
    }

    /**
     * {@code DELETE  /shipment-types/:id} : delete the "id" shipmentType.
     *
     * @param id the id of the shipmentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentType : {}", id);
        shipmentTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
