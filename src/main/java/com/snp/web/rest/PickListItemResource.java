package com.snp.web.rest;

import com.snp.domain.PickListItem;
import com.snp.repository.PickListItemRepository;
import com.snp.service.PickListItemQueryService;
import com.snp.service.PickListItemService;
import com.snp.service.criteria.PickListItemCriteria;
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
 * REST controller for managing {@link com.snp.domain.PickListItem}.
 */
@RestController
@RequestMapping("/api/pick-list-items")
public class PickListItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(PickListItemResource.class);

    private static final String ENTITY_NAME = "pickListItem";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PickListItemService pickListItemService;

    private final PickListItemRepository pickListItemRepository;

    private final PickListItemQueryService pickListItemQueryService;

    public PickListItemResource(
        PickListItemService pickListItemService,
        PickListItemRepository pickListItemRepository,
        PickListItemQueryService pickListItemQueryService
    ) {
        this.pickListItemService = pickListItemService;
        this.pickListItemRepository = pickListItemRepository;
        this.pickListItemQueryService = pickListItemQueryService;
    }

    /**
     * {@code POST  /pick-list-items} : Create a new pickListItem.
     *
     * @param pickListItem the pickListItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pickListItem, or with status {@code 400 (Bad Request)} if the pickListItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PickListItem> createPickListItem(@Valid @RequestBody PickListItem pickListItem) throws URISyntaxException {
        LOG.debug("REST request to save PickListItem : {}", pickListItem);
        if (pickListItem.getId() != null) {
            throw new BadRequestAlertException("A new pickListItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pickListItem = pickListItemService.save(pickListItem);
        return ResponseEntity.created(new URI("/api/pick-list-items/" + pickListItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pickListItem.getId().toString()))
            .body(pickListItem);
    }

    /**
     * {@code PUT  /pick-list-items/:id} : Updates an existing pickListItem.
     *
     * @param id the id of the pickListItem to save.
     * @param pickListItem the pickListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pickListItem,
     * or with status {@code 400 (Bad Request)} if the pickListItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pickListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PickListItem> updatePickListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PickListItem pickListItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update PickListItem : {}, {}", id, pickListItem);
        if (pickListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pickListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pickListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pickListItem = pickListItemService.update(pickListItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pickListItem.getId().toString()))
            .body(pickListItem);
    }

    /**
     * {@code PATCH  /pick-list-items/:id} : Partial updates given fields of an existing pickListItem, field will ignore if it is null
     *
     * @param id the id of the pickListItem to save.
     * @param pickListItem the pickListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pickListItem,
     * or with status {@code 400 (Bad Request)} if the pickListItem is not valid,
     * or with status {@code 404 (Not Found)} if the pickListItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the pickListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PickListItem> partialUpdatePickListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PickListItem pickListItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PickListItem : {}, {}", id, pickListItem);
        if (pickListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pickListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pickListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PickListItem> result = pickListItemService.partialUpdate(pickListItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pickListItem.getId().toString())
        );
    }

    /**
     * {@code GET  /pick-list-items} : get all the Pick List Items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pick List Items in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PickListItem>> getAllPickListItems(
        PickListItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PickListItems by criteria: {}", criteria);

        Page<PickListItem> page = pickListItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pick-list-items/count} : count all the pickListItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPickListItems(PickListItemCriteria criteria) {
        LOG.debug("REST request to count PickListItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(pickListItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pick-list-items/:id} : get the "id" pickListItem.
     *
     * @param id the id of the pickListItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pickListItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PickListItem> getPickListItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PickListItem : {}", id);
        Optional<PickListItem> pickListItem = pickListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pickListItem);
    }

    /**
     * {@code DELETE  /pick-list-items/:id} : delete the "id" pickListItem.
     *
     * @param id the id of the pickListItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePickListItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PickListItem : {}", id);
        pickListItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
