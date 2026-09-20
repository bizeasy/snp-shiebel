package com.snp.web.rest;

import com.snp.domain.InventoryTransfer;
import com.snp.repository.InventoryTransferRepository;
import com.snp.service.InventoryTransferQueryService;
import com.snp.service.InventoryTransferService;
import com.snp.service.criteria.InventoryTransferCriteria;
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
 * REST controller for managing {@link com.snp.domain.InventoryTransfer}.
 */
@RestController
@RequestMapping("/api/inventory-transfers")
public class InventoryTransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryTransferResource.class);

    private static final String ENTITY_NAME = "inventoryTransfer";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InventoryTransferService inventoryTransferService;

    private final InventoryTransferRepository inventoryTransferRepository;

    private final InventoryTransferQueryService inventoryTransferQueryService;

    public InventoryTransferResource(
        InventoryTransferService inventoryTransferService,
        InventoryTransferRepository inventoryTransferRepository,
        InventoryTransferQueryService inventoryTransferQueryService
    ) {
        this.inventoryTransferService = inventoryTransferService;
        this.inventoryTransferRepository = inventoryTransferRepository;
        this.inventoryTransferQueryService = inventoryTransferQueryService;
    }

    /**
     * {@code POST  /inventory-transfers} : Create a new inventoryTransfer.
     *
     * @param inventoryTransfer the inventoryTransfer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoryTransfer, or with status {@code 400 (Bad Request)} if the inventoryTransfer already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InventoryTransfer> createInventoryTransfer(@Valid @RequestBody InventoryTransfer inventoryTransfer)
        throws URISyntaxException {
        LOG.debug("REST request to save InventoryTransfer : {}", inventoryTransfer);
        if (inventoryTransfer.getId() != null) {
            throw new BadRequestAlertException("A new inventoryTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inventoryTransfer = inventoryTransferService.save(inventoryTransfer);
        return ResponseEntity.created(new URI("/api/inventory-transfers/" + inventoryTransfer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inventoryTransfer.getId().toString()))
            .body(inventoryTransfer);
    }

    /**
     * {@code PUT  /inventory-transfers/:id} : Updates an existing inventoryTransfer.
     *
     * @param id the id of the inventoryTransfer to save.
     * @param inventoryTransfer the inventoryTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryTransfer,
     * or with status {@code 400 (Bad Request)} if the inventoryTransfer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventoryTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryTransfer> updateInventoryTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InventoryTransfer inventoryTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to update InventoryTransfer : {}, {}", id, inventoryTransfer);
        if (inventoryTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inventoryTransfer = inventoryTransferService.update(inventoryTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryTransfer.getId().toString()))
            .body(inventoryTransfer);
    }

    /**
     * {@code PATCH  /inventory-transfers/:id} : Partial updates given fields of an existing inventoryTransfer, field will ignore if it is null
     *
     * @param id the id of the inventoryTransfer to save.
     * @param inventoryTransfer the inventoryTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryTransfer,
     * or with status {@code 400 (Bad Request)} if the inventoryTransfer is not valid,
     * or with status {@code 404 (Not Found)} if the inventoryTransfer is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoryTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InventoryTransfer> partialUpdateInventoryTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InventoryTransfer inventoryTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InventoryTransfer : {}, {}", id, inventoryTransfer);
        if (inventoryTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InventoryTransfer> result = inventoryTransferService.partialUpdate(inventoryTransfer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryTransfer.getId().toString())
        );
    }

    /**
     * {@code GET  /inventory-transfers} : get all the Inventory Transfers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Inventory Transfers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InventoryTransfer>> getAllInventoryTransfers(
        InventoryTransferCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InventoryTransfers by criteria: {}", criteria);

        Page<InventoryTransfer> page = inventoryTransferQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inventory-transfers/count} : count all the inventoryTransfers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInventoryTransfers(InventoryTransferCriteria criteria) {
        LOG.debug("REST request to count InventoryTransfers by criteria: {}", criteria);
        return ResponseEntity.ok().body(inventoryTransferQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inventory-transfers/:id} : get the "id" inventoryTransfer.
     *
     * @param id the id of the inventoryTransfer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoryTransfer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryTransfer> getInventoryTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InventoryTransfer : {}", id);
        Optional<InventoryTransfer> inventoryTransfer = inventoryTransferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoryTransfer);
    }

    /**
     * {@code DELETE  /inventory-transfers/:id} : delete the "id" inventoryTransfer.
     *
     * @param id the id of the inventoryTransfer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InventoryTransfer : {}", id);
        inventoryTransferService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
