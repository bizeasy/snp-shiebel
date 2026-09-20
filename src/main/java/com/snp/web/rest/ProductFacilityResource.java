package com.snp.web.rest;

import com.snp.domain.ProductFacility;
import com.snp.repository.ProductFacilityRepository;
import com.snp.service.ProductFacilityQueryService;
import com.snp.service.ProductFacilityService;
import com.snp.service.criteria.ProductFacilityCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFacility}.
 */
@RestController
@RequestMapping("/api/product-facilities")
public class ProductFacilityResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityResource.class);

    private static final String ENTITY_NAME = "productFacility";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFacilityService productFacilityService;

    private final ProductFacilityRepository productFacilityRepository;

    private final ProductFacilityQueryService productFacilityQueryService;

    public ProductFacilityResource(
        ProductFacilityService productFacilityService,
        ProductFacilityRepository productFacilityRepository,
        ProductFacilityQueryService productFacilityQueryService
    ) {
        this.productFacilityService = productFacilityService;
        this.productFacilityRepository = productFacilityRepository;
        this.productFacilityQueryService = productFacilityQueryService;
    }

    /**
     * {@code POST  /product-facilities} : Create a new productFacility.
     *
     * @param productFacility the productFacility to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFacility, or with status {@code 400 (Bad Request)} if the productFacility already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFacility> createProductFacility(@Valid @RequestBody ProductFacility productFacility)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductFacility : {}", productFacility);
        if (productFacility.getId() != null) {
            throw new BadRequestAlertException("A new productFacility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFacility = productFacilityService.save(productFacility);
        return ResponseEntity.created(new URI("/api/product-facilities/" + productFacility.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFacility.getId().toString()))
            .body(productFacility);
    }

    /**
     * {@code PUT  /product-facilities/:id} : Updates an existing productFacility.
     *
     * @param id the id of the productFacility to save.
     * @param productFacility the productFacility to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFacility,
     * or with status {@code 400 (Bad Request)} if the productFacility is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFacility couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFacility> updateProductFacility(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFacility productFacility
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFacility : {}, {}", id, productFacility);
        if (productFacility.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFacility.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFacilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFacility = productFacilityService.update(productFacility);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFacility.getId().toString()))
            .body(productFacility);
    }

    /**
     * {@code PATCH  /product-facilities/:id} : Partial updates given fields of an existing productFacility, field will ignore if it is null
     *
     * @param id the id of the productFacility to save.
     * @param productFacility the productFacility to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFacility,
     * or with status {@code 400 (Bad Request)} if the productFacility is not valid,
     * or with status {@code 404 (Not Found)} if the productFacility is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFacility couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFacility> partialUpdateProductFacility(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFacility productFacility
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFacility : {}, {}", id, productFacility);
        if (productFacility.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFacility.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFacilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFacility> result = productFacilityService.partialUpdate(productFacility);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFacility.getId().toString())
        );
    }

    /**
     * {@code GET  /product-facilities} : get all the Product Facilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Facilities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFacility>> getAllProductFacilities(
        ProductFacilityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductFacilities by criteria: {}", criteria);

        Page<ProductFacility> page = productFacilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-facilities/count} : count all the productFacilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFacilities(ProductFacilityCriteria criteria) {
        LOG.debug("REST request to count ProductFacilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFacilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-facilities/:id} : get the "id" productFacility.
     *
     * @param id the id of the productFacility to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFacility, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFacility> getProductFacility(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFacility : {}", id);
        Optional<ProductFacility> productFacility = productFacilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFacility);
    }

    /**
     * {@code DELETE  /product-facilities/:id} : delete the "id" productFacility.
     *
     * @param id the id of the productFacility to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFacility(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFacility : {}", id);
        productFacilityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
