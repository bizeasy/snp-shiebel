package com.snp.web.rest;

import com.snp.domain.ItemIssuance;
import com.snp.repository.ItemIssuanceRepository;
import com.snp.service.ItemIssuanceQueryService;
import com.snp.service.ItemIssuanceService;
import com.snp.service.criteria.ItemIssuanceCriteria;
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
 * REST controller for managing {@link com.snp.domain.ItemIssuance}.
 */
@RestController
@RequestMapping("/api/item-issuances")
public class ItemIssuanceResource {

    private static final Logger LOG = LoggerFactory.getLogger(ItemIssuanceResource.class);

    private static final String ENTITY_NAME = "itemIssuance";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ItemIssuanceService itemIssuanceService;

    private final ItemIssuanceRepository itemIssuanceRepository;

    private final ItemIssuanceQueryService itemIssuanceQueryService;

    public ItemIssuanceResource(
        ItemIssuanceService itemIssuanceService,
        ItemIssuanceRepository itemIssuanceRepository,
        ItemIssuanceQueryService itemIssuanceQueryService
    ) {
        this.itemIssuanceService = itemIssuanceService;
        this.itemIssuanceRepository = itemIssuanceRepository;
        this.itemIssuanceQueryService = itemIssuanceQueryService;
    }

    /**
     * {@code POST  /item-issuances} : Create a new itemIssuance.
     *
     * @param itemIssuance the itemIssuance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemIssuance, or with status {@code 400 (Bad Request)} if the itemIssuance already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ItemIssuance> createItemIssuance(@Valid @RequestBody ItemIssuance itemIssuance) throws URISyntaxException {
        LOG.debug("REST request to save ItemIssuance : {}", itemIssuance);
        if (itemIssuance.getId() != null) {
            throw new BadRequestAlertException("A new itemIssuance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemIssuance = itemIssuanceService.save(itemIssuance);
        return ResponseEntity.created(new URI("/api/item-issuances/" + itemIssuance.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, itemIssuance.getId().toString()))
            .body(itemIssuance);
    }

    /**
     * {@code PUT  /item-issuances/:id} : Updates an existing itemIssuance.
     *
     * @param id the id of the itemIssuance to save.
     * @param itemIssuance the itemIssuance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemIssuance,
     * or with status {@code 400 (Bad Request)} if the itemIssuance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemIssuance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemIssuance> updateItemIssuance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemIssuance itemIssuance
    ) throws URISyntaxException {
        LOG.debug("REST request to update ItemIssuance : {}, {}", id, itemIssuance);
        if (itemIssuance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemIssuance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemIssuanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        itemIssuance = itemIssuanceService.update(itemIssuance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemIssuance.getId().toString()))
            .body(itemIssuance);
    }

    /**
     * {@code PATCH  /item-issuances/:id} : Partial updates given fields of an existing itemIssuance, field will ignore if it is null
     *
     * @param id the id of the itemIssuance to save.
     * @param itemIssuance the itemIssuance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemIssuance,
     * or with status {@code 400 (Bad Request)} if the itemIssuance is not valid,
     * or with status {@code 404 (Not Found)} if the itemIssuance is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemIssuance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemIssuance> partialUpdateItemIssuance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemIssuance itemIssuance
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ItemIssuance : {}, {}", id, itemIssuance);
        if (itemIssuance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemIssuance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemIssuanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemIssuance> result = itemIssuanceService.partialUpdate(itemIssuance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemIssuance.getId().toString())
        );
    }

    /**
     * {@code GET  /item-issuances} : get all the Item Issuances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Item Issuances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ItemIssuance>> getAllItemIssuances(
        ItemIssuanceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ItemIssuances by criteria: {}", criteria);

        Page<ItemIssuance> page = itemIssuanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-issuances/count} : count all the itemIssuances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countItemIssuances(ItemIssuanceCriteria criteria) {
        LOG.debug("REST request to count ItemIssuances by criteria: {}", criteria);
        return ResponseEntity.ok().body(itemIssuanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /item-issuances/:id} : get the "id" itemIssuance.
     *
     * @param id the id of the itemIssuance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemIssuance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemIssuance> getItemIssuance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ItemIssuance : {}", id);
        Optional<ItemIssuance> itemIssuance = itemIssuanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemIssuance);
    }

    /**
     * {@code DELETE  /item-issuances/:id} : delete the "id" itemIssuance.
     *
     * @param id the id of the itemIssuance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemIssuance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ItemIssuance : {}", id);
        itemIssuanceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
