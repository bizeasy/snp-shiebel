package com.snp.web.rest;

import com.snp.domain.InventoryItemVariance;
import com.snp.repository.InventoryItemVarianceRepository;
import com.snp.service.InventoryItemVarianceQueryService;
import com.snp.service.InventoryItemVarianceService;
import com.snp.service.criteria.InventoryItemVarianceCriteria;
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
 * REST controller for managing {@link com.snp.domain.InventoryItemVariance}.
 */
@RestController
@RequestMapping("/api/inventory-item-variances")
public class InventoryItemVarianceResource {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemVarianceResource.class);

    private static final String ENTITY_NAME = "inventoryItemVariance";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InventoryItemVarianceService inventoryItemVarianceService;

    private final InventoryItemVarianceRepository inventoryItemVarianceRepository;

    private final InventoryItemVarianceQueryService inventoryItemVarianceQueryService;

    public InventoryItemVarianceResource(
        InventoryItemVarianceService inventoryItemVarianceService,
        InventoryItemVarianceRepository inventoryItemVarianceRepository,
        InventoryItemVarianceQueryService inventoryItemVarianceQueryService
    ) {
        this.inventoryItemVarianceService = inventoryItemVarianceService;
        this.inventoryItemVarianceRepository = inventoryItemVarianceRepository;
        this.inventoryItemVarianceQueryService = inventoryItemVarianceQueryService;
    }

    /**
     * {@code POST  /inventory-item-variances} : Create a new inventoryItemVariance.
     *
     * @param inventoryItemVariance the inventoryItemVariance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoryItemVariance, or with status {@code 400 (Bad Request)} if the inventoryItemVariance already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InventoryItemVariance> createInventoryItemVariance(
        @Valid @RequestBody InventoryItemVariance inventoryItemVariance
    ) throws URISyntaxException {
        LOG.debug("REST request to save InventoryItemVariance : {}", inventoryItemVariance);
        if (inventoryItemVariance.getId() != null) {
            throw new BadRequestAlertException("A new inventoryItemVariance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inventoryItemVariance = inventoryItemVarianceService.save(inventoryItemVariance);
        return ResponseEntity.created(new URI("/api/inventory-item-variances/" + inventoryItemVariance.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inventoryItemVariance.getId().toString()))
            .body(inventoryItemVariance);
    }

    /**
     * {@code PUT  /inventory-item-variances/:id} : Updates an existing inventoryItemVariance.
     *
     * @param id the id of the inventoryItemVariance to save.
     * @param inventoryItemVariance the inventoryItemVariance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryItemVariance,
     * or with status {@code 400 (Bad Request)} if the inventoryItemVariance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventoryItemVariance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemVariance> updateInventoryItemVariance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InventoryItemVariance inventoryItemVariance
    ) throws URISyntaxException {
        LOG.debug("REST request to update InventoryItemVariance : {}, {}", id, inventoryItemVariance);
        if (inventoryItemVariance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryItemVariance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryItemVarianceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inventoryItemVariance = inventoryItemVarianceService.update(inventoryItemVariance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryItemVariance.getId().toString()))
            .body(inventoryItemVariance);
    }

    /**
     * {@code PATCH  /inventory-item-variances/:id} : Partial updates given fields of an existing inventoryItemVariance, field will ignore if it is null
     *
     * @param id the id of the inventoryItemVariance to save.
     * @param inventoryItemVariance the inventoryItemVariance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryItemVariance,
     * or with status {@code 400 (Bad Request)} if the inventoryItemVariance is not valid,
     * or with status {@code 404 (Not Found)} if the inventoryItemVariance is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoryItemVariance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InventoryItemVariance> partialUpdateInventoryItemVariance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InventoryItemVariance inventoryItemVariance
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InventoryItemVariance : {}, {}", id, inventoryItemVariance);
        if (inventoryItemVariance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryItemVariance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryItemVarianceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InventoryItemVariance> result = inventoryItemVarianceService.partialUpdate(inventoryItemVariance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryItemVariance.getId().toString())
        );
    }

    /**
     * {@code GET  /inventory-item-variances} : get all the Inventory Item Variances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Inventory Item Variances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InventoryItemVariance>> getAllInventoryItemVariances(
        InventoryItemVarianceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InventoryItemVariances by criteria: {}", criteria);

        Page<InventoryItemVariance> page = inventoryItemVarianceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inventory-item-variances/count} : count all the inventoryItemVariances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInventoryItemVariances(InventoryItemVarianceCriteria criteria) {
        LOG.debug("REST request to count InventoryItemVariances by criteria: {}", criteria);
        return ResponseEntity.ok().body(inventoryItemVarianceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inventory-item-variances/:id} : get the "id" inventoryItemVariance.
     *
     * @param id the id of the inventoryItemVariance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoryItemVariance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemVariance> getInventoryItemVariance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InventoryItemVariance : {}", id);
        Optional<InventoryItemVariance> inventoryItemVariance = inventoryItemVarianceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoryItemVariance);
    }

    /**
     * {@code DELETE  /inventory-item-variances/:id} : delete the "id" inventoryItemVariance.
     *
     * @param id the id of the inventoryItemVariance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItemVariance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InventoryItemVariance : {}", id);
        inventoryItemVarianceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
