package com.snp.web.rest;

import com.snp.domain.ShoppingListType;
import com.snp.repository.ShoppingListTypeRepository;
import com.snp.service.ShoppingListTypeQueryService;
import com.snp.service.ShoppingListTypeService;
import com.snp.service.criteria.ShoppingListTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ShoppingListType}.
 */
@RestController
@RequestMapping("/api/shopping-list-types")
public class ShoppingListTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListTypeResource.class);

    private static final String ENTITY_NAME = "shoppingListType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShoppingListTypeService shoppingListTypeService;

    private final ShoppingListTypeRepository shoppingListTypeRepository;

    private final ShoppingListTypeQueryService shoppingListTypeQueryService;

    public ShoppingListTypeResource(
        ShoppingListTypeService shoppingListTypeService,
        ShoppingListTypeRepository shoppingListTypeRepository,
        ShoppingListTypeQueryService shoppingListTypeQueryService
    ) {
        this.shoppingListTypeService = shoppingListTypeService;
        this.shoppingListTypeRepository = shoppingListTypeRepository;
        this.shoppingListTypeQueryService = shoppingListTypeQueryService;
    }

    /**
     * {@code POST  /shopping-list-types} : Create a new shoppingListType.
     *
     * @param shoppingListType the shoppingListType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingListType, or with status {@code 400 (Bad Request)} if the shoppingListType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShoppingListType> createShoppingListType(@Valid @RequestBody ShoppingListType shoppingListType)
        throws URISyntaxException {
        LOG.debug("REST request to save ShoppingListType : {}", shoppingListType);
        if (shoppingListType.getId() != null) {
            throw new BadRequestAlertException("A new shoppingListType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shoppingListType = shoppingListTypeService.save(shoppingListType);
        return ResponseEntity.created(new URI("/api/shopping-list-types/" + shoppingListType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shoppingListType.getId().toString()))
            .body(shoppingListType);
    }

    /**
     * {@code PUT  /shopping-list-types/:id} : Updates an existing shoppingListType.
     *
     * @param id the id of the shoppingListType to save.
     * @param shoppingListType the shoppingListType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingListType,
     * or with status {@code 400 (Bad Request)} if the shoppingListType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingListType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListType> updateShoppingListType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShoppingListType shoppingListType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShoppingListType : {}, {}", id, shoppingListType);
        if (shoppingListType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingListType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingListTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shoppingListType = shoppingListTypeService.update(shoppingListType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingListType.getId().toString()))
            .body(shoppingListType);
    }

    /**
     * {@code PATCH  /shopping-list-types/:id} : Partial updates given fields of an existing shoppingListType, field will ignore if it is null
     *
     * @param id the id of the shoppingListType to save.
     * @param shoppingListType the shoppingListType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingListType,
     * or with status {@code 400 (Bad Request)} if the shoppingListType is not valid,
     * or with status {@code 404 (Not Found)} if the shoppingListType is not found,
     * or with status {@code 500 (Internal Server Error)} if the shoppingListType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShoppingListType> partialUpdateShoppingListType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShoppingListType shoppingListType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShoppingListType : {}, {}", id, shoppingListType);
        if (shoppingListType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingListType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingListTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShoppingListType> result = shoppingListTypeService.partialUpdate(shoppingListType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingListType.getId().toString())
        );
    }

    /**
     * {@code GET  /shopping-list-types} : get all the Shopping List Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shopping List Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShoppingListType>> getAllShoppingListTypes(ShoppingListTypeCriteria criteria) {
        LOG.debug("REST request to get ShoppingListTypes by criteria: {}", criteria);

        List<ShoppingListType> entityList = shoppingListTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /shopping-list-types/count} : count all the shoppingListTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShoppingListTypes(ShoppingListTypeCriteria criteria) {
        LOG.debug("REST request to count ShoppingListTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(shoppingListTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shopping-list-types/:id} : get the "id" shoppingListType.
     *
     * @param id the id of the shoppingListType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingListType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListType> getShoppingListType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShoppingListType : {}", id);
        Optional<ShoppingListType> shoppingListType = shoppingListTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shoppingListType);
    }

    /**
     * {@code DELETE  /shopping-list-types/:id} : delete the "id" shoppingListType.
     *
     * @param id the id of the shoppingListType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingListType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShoppingListType : {}", id);
        shoppingListTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
