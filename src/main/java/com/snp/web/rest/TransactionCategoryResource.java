package com.snp.web.rest;

import com.snp.domain.TransactionCategory;
import com.snp.repository.TransactionCategoryRepository;
import com.snp.service.TransactionCategoryQueryService;
import com.snp.service.TransactionCategoryService;
import com.snp.service.criteria.TransactionCategoryCriteria;
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
 * REST controller for managing {@link com.snp.domain.TransactionCategory}.
 */
@RestController
@RequestMapping("/api/transaction-categories")
public class TransactionCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionCategoryResource.class);

    private static final String ENTITY_NAME = "transactionCategory";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final TransactionCategoryService transactionCategoryService;

    private final TransactionCategoryRepository transactionCategoryRepository;

    private final TransactionCategoryQueryService transactionCategoryQueryService;

    public TransactionCategoryResource(
        TransactionCategoryService transactionCategoryService,
        TransactionCategoryRepository transactionCategoryRepository,
        TransactionCategoryQueryService transactionCategoryQueryService
    ) {
        this.transactionCategoryService = transactionCategoryService;
        this.transactionCategoryRepository = transactionCategoryRepository;
        this.transactionCategoryQueryService = transactionCategoryQueryService;
    }

    /**
     * {@code POST  /transaction-categories} : Create a new transactionCategory.
     *
     * @param transactionCategory the transactionCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionCategory, or with status {@code 400 (Bad Request)} if the transactionCategory already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionCategory> createTransactionCategory(@Valid @RequestBody TransactionCategory transactionCategory)
        throws URISyntaxException {
        LOG.debug("REST request to save TransactionCategory : {}", transactionCategory);
        if (transactionCategory.getId() != null) {
            throw new BadRequestAlertException("A new transactionCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionCategory = transactionCategoryService.save(transactionCategory);
        return ResponseEntity.created(new URI("/api/transaction-categories/" + transactionCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transactionCategory.getId().toString()))
            .body(transactionCategory);
    }

    /**
     * {@code PUT  /transaction-categories/:id} : Updates an existing transactionCategory.
     *
     * @param id the id of the transactionCategory to save.
     * @param transactionCategory the transactionCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionCategory,
     * or with status {@code 400 (Bad Request)} if the transactionCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionCategory> updateTransactionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionCategory transactionCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransactionCategory : {}, {}", id, transactionCategory);
        if (transactionCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transactionCategory = transactionCategoryService.update(transactionCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionCategory.getId().toString()))
            .body(transactionCategory);
    }

    /**
     * {@code PATCH  /transaction-categories/:id} : Partial updates given fields of an existing transactionCategory, field will ignore if it is null
     *
     * @param id the id of the transactionCategory to save.
     * @param transactionCategory the transactionCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionCategory,
     * or with status {@code 400 (Bad Request)} if the transactionCategory is not valid,
     * or with status {@code 404 (Not Found)} if the transactionCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionCategory> partialUpdateTransactionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionCategory transactionCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TransactionCategory : {}, {}", id, transactionCategory);
        if (transactionCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionCategory> result = transactionCategoryService.partialUpdate(transactionCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-categories} : get all the Transaction Categories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Transaction Categories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionCategory>> getAllTransactionCategories(TransactionCategoryCriteria criteria) {
        LOG.debug("REST request to get TransactionCategories by criteria: {}", criteria);

        List<TransactionCategory> entityList = transactionCategoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /transaction-categories/count} : count all the transactionCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransactionCategories(TransactionCategoryCriteria criteria) {
        LOG.debug("REST request to count TransactionCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-categories/:id} : get the "id" transactionCategory.
     *
     * @param id the id of the transactionCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionCategory> getTransactionCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransactionCategory : {}", id);
        Optional<TransactionCategory> transactionCategory = transactionCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionCategory);
    }

    /**
     * {@code DELETE  /transaction-categories/:id} : delete the "id" transactionCategory.
     *
     * @param id the id of the transactionCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransactionCategory : {}", id);
        transactionCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
