package com.snp.web.rest;

import com.snp.domain.ShoppingListItem;
import com.snp.repository.ShoppingListItemRepository;
import com.snp.service.ShoppingListItemQueryService;
import com.snp.service.ShoppingListItemService;
import com.snp.service.criteria.ShoppingListItemCriteria;
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
 * REST controller for managing {@link com.snp.domain.ShoppingListItem}.
 */
@RestController
@RequestMapping("/api/shopping-list-items")
public class ShoppingListItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListItemResource.class);

    private static final String ENTITY_NAME = "shoppingListItem";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShoppingListItemService shoppingListItemService;

    private final ShoppingListItemRepository shoppingListItemRepository;

    private final ShoppingListItemQueryService shoppingListItemQueryService;

    public ShoppingListItemResource(
        ShoppingListItemService shoppingListItemService,
        ShoppingListItemRepository shoppingListItemRepository,
        ShoppingListItemQueryService shoppingListItemQueryService
    ) {
        this.shoppingListItemService = shoppingListItemService;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListItemQueryService = shoppingListItemQueryService;
    }

    /**
     * {@code POST  /shopping-list-items} : Create a new shoppingListItem.
     *
     * @param shoppingListItem the shoppingListItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingListItem, or with status {@code 400 (Bad Request)} if the shoppingListItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShoppingListItem> createShoppingListItem(@Valid @RequestBody ShoppingListItem shoppingListItem)
        throws URISyntaxException {
        LOG.debug("REST request to save ShoppingListItem : {}", shoppingListItem);
        if (shoppingListItem.getId() != null) {
            throw new BadRequestAlertException("A new shoppingListItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shoppingListItem = shoppingListItemService.save(shoppingListItem);
        return ResponseEntity.created(new URI("/api/shopping-list-items/" + shoppingListItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shoppingListItem.getId().toString()))
            .body(shoppingListItem);
    }

    /**
     * {@code PUT  /shopping-list-items/:id} : Updates an existing shoppingListItem.
     *
     * @param id the id of the shoppingListItem to save.
     * @param shoppingListItem the shoppingListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingListItem,
     * or with status {@code 400 (Bad Request)} if the shoppingListItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListItem> updateShoppingListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShoppingListItem shoppingListItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShoppingListItem : {}, {}", id, shoppingListItem);
        if (shoppingListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shoppingListItem = shoppingListItemService.update(shoppingListItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingListItem.getId().toString()))
            .body(shoppingListItem);
    }

    /**
     * {@code PATCH  /shopping-list-items/:id} : Partial updates given fields of an existing shoppingListItem, field will ignore if it is null
     *
     * @param id the id of the shoppingListItem to save.
     * @param shoppingListItem the shoppingListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingListItem,
     * or with status {@code 400 (Bad Request)} if the shoppingListItem is not valid,
     * or with status {@code 404 (Not Found)} if the shoppingListItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the shoppingListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShoppingListItem> partialUpdateShoppingListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShoppingListItem shoppingListItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShoppingListItem : {}, {}", id, shoppingListItem);
        if (shoppingListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShoppingListItem> result = shoppingListItemService.partialUpdate(shoppingListItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingListItem.getId().toString())
        );
    }

    /**
     * {@code GET  /shopping-list-items} : get all the Shopping List Items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shopping List Items in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShoppingListItem>> getAllShoppingListItems(
        ShoppingListItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShoppingListItems by criteria: {}", criteria);

        Page<ShoppingListItem> page = shoppingListItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shopping-list-items/count} : count all the shoppingListItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShoppingListItems(ShoppingListItemCriteria criteria) {
        LOG.debug("REST request to count ShoppingListItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(shoppingListItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shopping-list-items/:id} : get the "id" shoppingListItem.
     *
     * @param id the id of the shoppingListItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingListItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListItem> getShoppingListItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShoppingListItem : {}", id);
        Optional<ShoppingListItem> shoppingListItem = shoppingListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shoppingListItem);
    }

    /**
     * {@code DELETE  /shopping-list-items/:id} : delete the "id" shoppingListItem.
     *
     * @param id the id of the shoppingListItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingListItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShoppingListItem : {}", id);
        shoppingListItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
