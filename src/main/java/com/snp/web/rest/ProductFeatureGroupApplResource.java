package com.snp.web.rest;

import com.snp.domain.ProductFeatureGroupAppl;
import com.snp.repository.ProductFeatureGroupApplRepository;
import com.snp.service.ProductFeatureGroupApplQueryService;
import com.snp.service.ProductFeatureGroupApplService;
import com.snp.service.criteria.ProductFeatureGroupApplCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFeatureGroupAppl}.
 */
@RestController
@RequestMapping("/api/product-feature-group-appls")
public class ProductFeatureGroupApplResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureGroupApplResource.class);

    private static final String ENTITY_NAME = "productFeatureGroupAppl";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFeatureGroupApplService productFeatureGroupApplService;

    private final ProductFeatureGroupApplRepository productFeatureGroupApplRepository;

    private final ProductFeatureGroupApplQueryService productFeatureGroupApplQueryService;

    public ProductFeatureGroupApplResource(
        ProductFeatureGroupApplService productFeatureGroupApplService,
        ProductFeatureGroupApplRepository productFeatureGroupApplRepository,
        ProductFeatureGroupApplQueryService productFeatureGroupApplQueryService
    ) {
        this.productFeatureGroupApplService = productFeatureGroupApplService;
        this.productFeatureGroupApplRepository = productFeatureGroupApplRepository;
        this.productFeatureGroupApplQueryService = productFeatureGroupApplQueryService;
    }

    /**
     * {@code POST  /product-feature-group-appls} : Create a new productFeatureGroupAppl.
     *
     * @param productFeatureGroupAppl the productFeatureGroupAppl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeatureGroupAppl, or with status {@code 400 (Bad Request)} if the productFeatureGroupAppl already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFeatureGroupAppl> createProductFeatureGroupAppl(
        @Valid @RequestBody ProductFeatureGroupAppl productFeatureGroupAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductFeatureGroupAppl : {}", productFeatureGroupAppl);
        if (productFeatureGroupAppl.getId() != null) {
            throw new BadRequestAlertException("A new productFeatureGroupAppl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFeatureGroupAppl = productFeatureGroupApplService.save(productFeatureGroupAppl);
        return ResponseEntity.created(new URI("/api/product-feature-group-appls/" + productFeatureGroupAppl.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFeatureGroupAppl.getId().toString()))
            .body(productFeatureGroupAppl);
    }

    /**
     * {@code PUT  /product-feature-group-appls/:id} : Updates an existing productFeatureGroupAppl.
     *
     * @param id the id of the productFeatureGroupAppl to save.
     * @param productFeatureGroupAppl the productFeatureGroupAppl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureGroupAppl,
     * or with status {@code 400 (Bad Request)} if the productFeatureGroupAppl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureGroupAppl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFeatureGroupAppl> updateProductFeatureGroupAppl(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFeatureGroupAppl productFeatureGroupAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFeatureGroupAppl : {}, {}", id, productFeatureGroupAppl);
        if (productFeatureGroupAppl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureGroupAppl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureGroupApplRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFeatureGroupAppl = productFeatureGroupApplService.update(productFeatureGroupAppl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureGroupAppl.getId().toString()))
            .body(productFeatureGroupAppl);
    }

    /**
     * {@code PATCH  /product-feature-group-appls/:id} : Partial updates given fields of an existing productFeatureGroupAppl, field will ignore if it is null
     *
     * @param id the id of the productFeatureGroupAppl to save.
     * @param productFeatureGroupAppl the productFeatureGroupAppl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureGroupAppl,
     * or with status {@code 400 (Bad Request)} if the productFeatureGroupAppl is not valid,
     * or with status {@code 404 (Not Found)} if the productFeatureGroupAppl is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureGroupAppl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeatureGroupAppl> partialUpdateProductFeatureGroupAppl(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFeatureGroupAppl productFeatureGroupAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFeatureGroupAppl : {}, {}", id, productFeatureGroupAppl);
        if (productFeatureGroupAppl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureGroupAppl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureGroupApplRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeatureGroupAppl> result = productFeatureGroupApplService.partialUpdate(productFeatureGroupAppl);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureGroupAppl.getId().toString())
        );
    }

    /**
     * {@code GET  /product-feature-group-appls} : get all the Product Feature Group Appls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Feature Group Appls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFeatureGroupAppl>> getAllProductFeatureGroupAppls(
        ProductFeatureGroupApplCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductFeatureGroupAppls by criteria: {}", criteria);

        Page<ProductFeatureGroupAppl> page = productFeatureGroupApplQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-feature-group-appls/count} : count all the productFeatureGroupAppls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFeatureGroupAppls(ProductFeatureGroupApplCriteria criteria) {
        LOG.debug("REST request to count ProductFeatureGroupAppls by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFeatureGroupApplQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-feature-group-appls/:id} : get the "id" productFeatureGroupAppl.
     *
     * @param id the id of the productFeatureGroupAppl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeatureGroupAppl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureGroupAppl> getProductFeatureGroupAppl(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFeatureGroupAppl : {}", id);
        Optional<ProductFeatureGroupAppl> productFeatureGroupAppl = productFeatureGroupApplService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFeatureGroupAppl);
    }

    /**
     * {@code DELETE  /product-feature-group-appls/:id} : delete the "id" productFeatureGroupAppl.
     *
     * @param id the id of the productFeatureGroupAppl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFeatureGroupAppl(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFeatureGroupAppl : {}", id);
        productFeatureGroupApplService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
