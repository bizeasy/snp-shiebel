package com.snp.web.rest;

import com.snp.domain.CustRequestCategory;
import com.snp.repository.CustRequestCategoryRepository;
import com.snp.service.CustRequestCategoryQueryService;
import com.snp.service.CustRequestCategoryService;
import com.snp.service.criteria.CustRequestCategoryCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustRequestCategory}.
 */
@RestController
@RequestMapping("/api/cust-request-categories")
public class CustRequestCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestCategoryResource.class);

    private static final String ENTITY_NAME = "custRequestCategory";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustRequestCategoryService custRequestCategoryService;

    private final CustRequestCategoryRepository custRequestCategoryRepository;

    private final CustRequestCategoryQueryService custRequestCategoryQueryService;

    public CustRequestCategoryResource(
        CustRequestCategoryService custRequestCategoryService,
        CustRequestCategoryRepository custRequestCategoryRepository,
        CustRequestCategoryQueryService custRequestCategoryQueryService
    ) {
        this.custRequestCategoryService = custRequestCategoryService;
        this.custRequestCategoryRepository = custRequestCategoryRepository;
        this.custRequestCategoryQueryService = custRequestCategoryQueryService;
    }

    /**
     * {@code POST  /cust-request-categories} : Create a new custRequestCategory.
     *
     * @param custRequestCategory the custRequestCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new custRequestCategory, or with status {@code 400 (Bad Request)} if the custRequestCategory already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustRequestCategory> createCustRequestCategory(@Valid @RequestBody CustRequestCategory custRequestCategory)
        throws URISyntaxException {
        LOG.debug("REST request to save CustRequestCategory : {}", custRequestCategory);
        if (custRequestCategory.getId() != null) {
            throw new BadRequestAlertException("A new custRequestCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        custRequestCategory = custRequestCategoryService.save(custRequestCategory);
        return ResponseEntity.created(new URI("/api/cust-request-categories/" + custRequestCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, custRequestCategory.getId().toString()))
            .body(custRequestCategory);
    }

    /**
     * {@code PUT  /cust-request-categories/:id} : Updates an existing custRequestCategory.
     *
     * @param id the id of the custRequestCategory to save.
     * @param custRequestCategory the custRequestCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestCategory,
     * or with status {@code 400 (Bad Request)} if the custRequestCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the custRequestCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustRequestCategory> updateCustRequestCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustRequestCategory custRequestCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustRequestCategory : {}, {}", id, custRequestCategory);
        if (custRequestCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        custRequestCategory = custRequestCategoryService.update(custRequestCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestCategory.getId().toString()))
            .body(custRequestCategory);
    }

    /**
     * {@code PATCH  /cust-request-categories/:id} : Partial updates given fields of an existing custRequestCategory, field will ignore if it is null
     *
     * @param id the id of the custRequestCategory to save.
     * @param custRequestCategory the custRequestCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestCategory,
     * or with status {@code 400 (Bad Request)} if the custRequestCategory is not valid,
     * or with status {@code 404 (Not Found)} if the custRequestCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the custRequestCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustRequestCategory> partialUpdateCustRequestCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustRequestCategory custRequestCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustRequestCategory : {}, {}", id, custRequestCategory);
        if (custRequestCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustRequestCategory> result = custRequestCategoryService.partialUpdate(custRequestCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /cust-request-categories} : get all the Cust Request Categories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cust Request Categories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustRequestCategory>> getAllCustRequestCategories(CustRequestCategoryCriteria criteria) {
        LOG.debug("REST request to get CustRequestCategories by criteria: {}", criteria);

        List<CustRequestCategory> entityList = custRequestCategoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cust-request-categories/count} : count all the custRequestCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustRequestCategories(CustRequestCategoryCriteria criteria) {
        LOG.debug("REST request to count CustRequestCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(custRequestCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cust-request-categories/:id} : get the "id" custRequestCategory.
     *
     * @param id the id of the custRequestCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the custRequestCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustRequestCategory> getCustRequestCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustRequestCategory : {}", id);
        Optional<CustRequestCategory> custRequestCategory = custRequestCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(custRequestCategory);
    }

    /**
     * {@code DELETE  /cust-request-categories/:id} : delete the "id" custRequestCategory.
     *
     * @param id the id of the custRequestCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustRequestCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustRequestCategory : {}", id);
        custRequestCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
