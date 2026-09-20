package com.snp.web.rest;

import com.snp.domain.InventoryItemType;
import com.snp.repository.InventoryItemTypeRepository;
import com.snp.service.InventoryItemTypeQueryService;
import com.snp.service.InventoryItemTypeService;
import com.snp.service.criteria.InventoryItemTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.InventoryItemType}.
 */
@RestController
@RequestMapping("/api/inventory-item-types")
public class InventoryItemTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemTypeResource.class);

    private static final String ENTITY_NAME = "inventoryItemType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InventoryItemTypeService inventoryItemTypeService;

    private final InventoryItemTypeRepository inventoryItemTypeRepository;

    private final InventoryItemTypeQueryService inventoryItemTypeQueryService;

    public InventoryItemTypeResource(
        InventoryItemTypeService inventoryItemTypeService,
        InventoryItemTypeRepository inventoryItemTypeRepository,
        InventoryItemTypeQueryService inventoryItemTypeQueryService
    ) {
        this.inventoryItemTypeService = inventoryItemTypeService;
        this.inventoryItemTypeRepository = inventoryItemTypeRepository;
        this.inventoryItemTypeQueryService = inventoryItemTypeQueryService;
    }

    /**
     * {@code POST  /inventory-item-types} : Create a new inventoryItemType.
     *
     * @param inventoryItemType the inventoryItemType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoryItemType, or with status {@code 400 (Bad Request)} if the inventoryItemType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InventoryItemType> createInventoryItemType(@Valid @RequestBody InventoryItemType inventoryItemType)
        throws URISyntaxException {
        LOG.debug("REST request to save InventoryItemType : {}", inventoryItemType);
        if (inventoryItemType.getId() != null) {
            throw new BadRequestAlertException("A new inventoryItemType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inventoryItemType = inventoryItemTypeService.save(inventoryItemType);
        return ResponseEntity.created(new URI("/api/inventory-item-types/" + inventoryItemType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inventoryItemType.getId().toString()))
            .body(inventoryItemType);
    }

    /**
     * {@code PUT  /inventory-item-types/:id} : Updates an existing inventoryItemType.
     *
     * @param id the id of the inventoryItemType to save.
     * @param inventoryItemType the inventoryItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryItemType,
     * or with status {@code 400 (Bad Request)} if the inventoryItemType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventoryItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemType> updateInventoryItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InventoryItemType inventoryItemType
    ) throws URISyntaxException {
        LOG.debug("REST request to update InventoryItemType : {}, {}", id, inventoryItemType);
        if (inventoryItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inventoryItemType = inventoryItemTypeService.update(inventoryItemType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryItemType.getId().toString()))
            .body(inventoryItemType);
    }

    /**
     * {@code PATCH  /inventory-item-types/:id} : Partial updates given fields of an existing inventoryItemType, field will ignore if it is null
     *
     * @param id the id of the inventoryItemType to save.
     * @param inventoryItemType the inventoryItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryItemType,
     * or with status {@code 400 (Bad Request)} if the inventoryItemType is not valid,
     * or with status {@code 404 (Not Found)} if the inventoryItemType is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoryItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InventoryItemType> partialUpdateInventoryItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InventoryItemType inventoryItemType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InventoryItemType : {}, {}", id, inventoryItemType);
        if (inventoryItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InventoryItemType> result = inventoryItemTypeService.partialUpdate(inventoryItemType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryItemType.getId().toString())
        );
    }

    /**
     * {@code GET  /inventory-item-types} : get all the Inventory Item Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Inventory Item Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InventoryItemType>> getAllInventoryItemTypes(InventoryItemTypeCriteria criteria) {
        LOG.debug("REST request to get InventoryItemTypes by criteria: {}", criteria);

        List<InventoryItemType> entityList = inventoryItemTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /inventory-item-types/count} : count all the inventoryItemTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInventoryItemTypes(InventoryItemTypeCriteria criteria) {
        LOG.debug("REST request to count InventoryItemTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(inventoryItemTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inventory-item-types/:id} : get the "id" inventoryItemType.
     *
     * @param id the id of the inventoryItemType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoryItemType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemType> getInventoryItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InventoryItemType : {}", id);
        Optional<InventoryItemType> inventoryItemType = inventoryItemTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoryItemType);
    }

    /**
     * {@code DELETE  /inventory-item-types/:id} : delete the "id" inventoryItemType.
     *
     * @param id the id of the inventoryItemType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InventoryItemType : {}", id);
        inventoryItemTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
