package com.snp.web.rest;

import com.snp.domain.ProductFeature;
import com.snp.repository.ProductFeatureRepository;
import com.snp.service.ProductFeatureQueryService;
import com.snp.service.ProductFeatureService;
import com.snp.service.criteria.ProductFeatureCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFeature}.
 */
@RestController
@RequestMapping("/api/product-features")
public class ProductFeatureResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureResource.class);

    private static final String ENTITY_NAME = "productFeature";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFeatureService productFeatureService;

    private final ProductFeatureRepository productFeatureRepository;

    private final ProductFeatureQueryService productFeatureQueryService;

    public ProductFeatureResource(
        ProductFeatureService productFeatureService,
        ProductFeatureRepository productFeatureRepository,
        ProductFeatureQueryService productFeatureQueryService
    ) {
        this.productFeatureService = productFeatureService;
        this.productFeatureRepository = productFeatureRepository;
        this.productFeatureQueryService = productFeatureQueryService;
    }

    /**
     * {@code POST  /product-features} : Create a new productFeature.
     *
     * @param productFeature the productFeature to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeature, or with status {@code 400 (Bad Request)} if the productFeature already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFeature> createProductFeature(@Valid @RequestBody ProductFeature productFeature)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductFeature : {}", productFeature);
        if (productFeature.getId() != null) {
            throw new BadRequestAlertException("A new productFeature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFeature = productFeatureService.save(productFeature);
        return ResponseEntity.created(new URI("/api/product-features/" + productFeature.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFeature.getId().toString()))
            .body(productFeature);
    }

    /**
     * {@code PUT  /product-features/:id} : Updates an existing productFeature.
     *
     * @param id the id of the productFeature to save.
     * @param productFeature the productFeature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeature,
     * or with status {@code 400 (Bad Request)} if the productFeature is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFeature> updateProductFeature(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFeature productFeature
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFeature : {}, {}", id, productFeature);
        if (productFeature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFeature = productFeatureService.update(productFeature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeature.getId().toString()))
            .body(productFeature);
    }

    /**
     * {@code PATCH  /product-features/:id} : Partial updates given fields of an existing productFeature, field will ignore if it is null
     *
     * @param id the id of the productFeature to save.
     * @param productFeature the productFeature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeature,
     * or with status {@code 400 (Bad Request)} if the productFeature is not valid,
     * or with status {@code 404 (Not Found)} if the productFeature is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeature> partialUpdateProductFeature(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFeature productFeature
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFeature : {}, {}", id, productFeature);
        if (productFeature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeature> result = productFeatureService.partialUpdate(productFeature);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeature.getId().toString())
        );
    }

    /**
     * {@code GET  /product-features} : get all the Product Features.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Features in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFeature>> getAllProductFeatures(
        ProductFeatureCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductFeatures by criteria: {}", criteria);

        Page<ProductFeature> page = productFeatureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-features/count} : count all the productFeatures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFeatures(ProductFeatureCriteria criteria) {
        LOG.debug("REST request to count ProductFeatures by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFeatureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-features/:id} : get the "id" productFeature.
     *
     * @param id the id of the productFeature to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeature, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeature> getProductFeature(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFeature : {}", id);
        Optional<ProductFeature> productFeature = productFeatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFeature);
    }

    /**
     * {@code DELETE  /product-features/:id} : delete the "id" productFeature.
     *
     * @param id the id of the productFeature to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFeature(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFeature : {}", id);
        productFeatureService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
