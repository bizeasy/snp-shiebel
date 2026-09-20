package com.snp.web.rest;

import com.snp.domain.ShipmentMethodType;
import com.snp.repository.ShipmentMethodTypeRepository;
import com.snp.service.ShipmentMethodTypeQueryService;
import com.snp.service.ShipmentMethodTypeService;
import com.snp.service.criteria.ShipmentMethodTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ShipmentMethodType}.
 */
@RestController
@RequestMapping("/api/shipment-method-types")
public class ShipmentMethodTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMethodTypeResource.class);

    private static final String ENTITY_NAME = "shipmentMethodType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShipmentMethodTypeService shipmentMethodTypeService;

    private final ShipmentMethodTypeRepository shipmentMethodTypeRepository;

    private final ShipmentMethodTypeQueryService shipmentMethodTypeQueryService;

    public ShipmentMethodTypeResource(
        ShipmentMethodTypeService shipmentMethodTypeService,
        ShipmentMethodTypeRepository shipmentMethodTypeRepository,
        ShipmentMethodTypeQueryService shipmentMethodTypeQueryService
    ) {
        this.shipmentMethodTypeService = shipmentMethodTypeService;
        this.shipmentMethodTypeRepository = shipmentMethodTypeRepository;
        this.shipmentMethodTypeQueryService = shipmentMethodTypeQueryService;
    }

    /**
     * {@code POST  /shipment-method-types} : Create a new shipmentMethodType.
     *
     * @param shipmentMethodType the shipmentMethodType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentMethodType, or with status {@code 400 (Bad Request)} if the shipmentMethodType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentMethodType> createShipmentMethodType(@Valid @RequestBody ShipmentMethodType shipmentMethodType)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentMethodType : {}", shipmentMethodType);
        if (shipmentMethodType.getId() != null) {
            throw new BadRequestAlertException("A new shipmentMethodType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentMethodType = shipmentMethodTypeService.save(shipmentMethodType);
        return ResponseEntity.created(new URI("/api/shipment-method-types/" + shipmentMethodType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentMethodType.getId().toString()))
            .body(shipmentMethodType);
    }

    /**
     * {@code PUT  /shipment-method-types/:id} : Updates an existing shipmentMethodType.
     *
     * @param id the id of the shipmentMethodType to save.
     * @param shipmentMethodType the shipmentMethodType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMethodType,
     * or with status {@code 400 (Bad Request)} if the shipmentMethodType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMethodType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentMethodType> updateShipmentMethodType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentMethodType shipmentMethodType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentMethodType : {}, {}", id, shipmentMethodType);
        if (shipmentMethodType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMethodType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMethodTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentMethodType = shipmentMethodTypeService.update(shipmentMethodType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMethodType.getId().toString()))
            .body(shipmentMethodType);
    }

    /**
     * {@code PATCH  /shipment-method-types/:id} : Partial updates given fields of an existing shipmentMethodType, field will ignore if it is null
     *
     * @param id the id of the shipmentMethodType to save.
     * @param shipmentMethodType the shipmentMethodType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMethodType,
     * or with status {@code 400 (Bad Request)} if the shipmentMethodType is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentMethodType is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMethodType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentMethodType> partialUpdateShipmentMethodType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentMethodType shipmentMethodType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShipmentMethodType : {}, {}", id, shipmentMethodType);
        if (shipmentMethodType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMethodType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMethodTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentMethodType> result = shipmentMethodTypeService.partialUpdate(shipmentMethodType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMethodType.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-method-types} : get all the Shipment Method Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shipment Method Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentMethodType>> getAllShipmentMethodTypes(ShipmentMethodTypeCriteria criteria) {
        LOG.debug("REST request to get ShipmentMethodTypes by criteria: {}", criteria);

        List<ShipmentMethodType> entityList = shipmentMethodTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /shipment-method-types/count} : count all the shipmentMethodTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentMethodTypes(ShipmentMethodTypeCriteria criteria) {
        LOG.debug("REST request to count ShipmentMethodTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentMethodTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-method-types/:id} : get the "id" shipmentMethodType.
     *
     * @param id the id of the shipmentMethodType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentMethodType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentMethodType> getShipmentMethodType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentMethodType : {}", id);
        Optional<ShipmentMethodType> shipmentMethodType = shipmentMethodTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentMethodType);
    }

    /**
     * {@code DELETE  /shipment-method-types/:id} : delete the "id" shipmentMethodType.
     *
     * @param id the id of the shipmentMethodType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentMethodType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentMethodType : {}", id);
        shipmentMethodTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
