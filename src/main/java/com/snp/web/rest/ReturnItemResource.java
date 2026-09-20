package com.snp.web.rest;

import com.snp.domain.ReturnItem;
import com.snp.repository.ReturnItemRepository;
import com.snp.service.ReturnItemQueryService;
import com.snp.service.ReturnItemService;
import com.snp.service.criteria.ReturnItemCriteria;
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
 * REST controller for managing {@link com.snp.domain.ReturnItem}.
 */
@RestController
@RequestMapping("/api/return-items")
public class ReturnItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnItemResource.class);

    private static final String ENTITY_NAME = "returnItem";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ReturnItemService returnItemService;

    private final ReturnItemRepository returnItemRepository;

    private final ReturnItemQueryService returnItemQueryService;

    public ReturnItemResource(
        ReturnItemService returnItemService,
        ReturnItemRepository returnItemRepository,
        ReturnItemQueryService returnItemQueryService
    ) {
        this.returnItemService = returnItemService;
        this.returnItemRepository = returnItemRepository;
        this.returnItemQueryService = returnItemQueryService;
    }

    /**
     * {@code POST  /return-items} : Create a new returnItem.
     *
     * @param returnItem the returnItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnItem, or with status {@code 400 (Bad Request)} if the returnItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReturnItem> createReturnItem(@Valid @RequestBody ReturnItem returnItem) throws URISyntaxException {
        LOG.debug("REST request to save ReturnItem : {}", returnItem);
        if (returnItem.getId() != null) {
            throw new BadRequestAlertException("A new returnItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        returnItem = returnItemService.save(returnItem);
        return ResponseEntity.created(new URI("/api/return-items/" + returnItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, returnItem.getId().toString()))
            .body(returnItem);
    }

    /**
     * {@code PUT  /return-items/:id} : Updates an existing returnItem.
     *
     * @param id the id of the returnItem to save.
     * @param returnItem the returnItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnItem,
     * or with status {@code 400 (Bad Request)} if the returnItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReturnItem> updateReturnItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReturnItem returnItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReturnItem : {}, {}", id, returnItem);
        if (returnItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        returnItem = returnItemService.update(returnItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnItem.getId().toString()))
            .body(returnItem);
    }

    /**
     * {@code PATCH  /return-items/:id} : Partial updates given fields of an existing returnItem, field will ignore if it is null
     *
     * @param id the id of the returnItem to save.
     * @param returnItem the returnItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnItem,
     * or with status {@code 400 (Bad Request)} if the returnItem is not valid,
     * or with status {@code 404 (Not Found)} if the returnItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the returnItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReturnItem> partialUpdateReturnItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReturnItem returnItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ReturnItem : {}, {}", id, returnItem);
        if (returnItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReturnItem> result = returnItemService.partialUpdate(returnItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnItem.getId().toString())
        );
    }

    /**
     * {@code GET  /return-items} : get all the Return Items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Return Items in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReturnItem>> getAllReturnItems(
        ReturnItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReturnItems by criteria: {}", criteria);

        Page<ReturnItem> page = returnItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /return-items/count} : count all the returnItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReturnItems(ReturnItemCriteria criteria) {
        LOG.debug("REST request to count ReturnItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(returnItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /return-items/:id} : get the "id" returnItem.
     *
     * @param id the id of the returnItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReturnItem> getReturnItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReturnItem : {}", id);
        Optional<ReturnItem> returnItem = returnItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnItem);
    }

    /**
     * {@code DELETE  /return-items/:id} : delete the "id" returnItem.
     *
     * @param id the id of the returnItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReturnItem : {}", id);
        returnItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
