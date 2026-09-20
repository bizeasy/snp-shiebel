package com.snp.web.rest;

import com.snp.domain.ProductFacilityLocation;
import com.snp.repository.ProductFacilityLocationRepository;
import com.snp.service.ProductFacilityLocationQueryService;
import com.snp.service.ProductFacilityLocationService;
import com.snp.service.criteria.ProductFacilityLocationCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFacilityLocation}.
 */
@RestController
@RequestMapping("/api/product-facility-locations")
public class ProductFacilityLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityLocationResource.class);

    private static final String ENTITY_NAME = "productFacilityLocation";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFacilityLocationService productFacilityLocationService;

    private final ProductFacilityLocationRepository productFacilityLocationRepository;

    private final ProductFacilityLocationQueryService productFacilityLocationQueryService;

    public ProductFacilityLocationResource(
        ProductFacilityLocationService productFacilityLocationService,
        ProductFacilityLocationRepository productFacilityLocationRepository,
        ProductFacilityLocationQueryService productFacilityLocationQueryService
    ) {
        this.productFacilityLocationService = productFacilityLocationService;
        this.productFacilityLocationRepository = productFacilityLocationRepository;
        this.productFacilityLocationQueryService = productFacilityLocationQueryService;
    }

    /**
     * {@code POST  /product-facility-locations} : Create a new productFacilityLocation.
     *
     * @param productFacilityLocation the productFacilityLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFacilityLocation, or with status {@code 400 (Bad Request)} if the productFacilityLocation already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFacilityLocation> createProductFacilityLocation(
        @Valid @RequestBody ProductFacilityLocation productFacilityLocation
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductFacilityLocation : {}", productFacilityLocation);
        if (productFacilityLocation.getId() != null) {
            throw new BadRequestAlertException("A new productFacilityLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFacilityLocation = productFacilityLocationService.save(productFacilityLocation);
        return ResponseEntity.created(new URI("/api/product-facility-locations/" + productFacilityLocation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFacilityLocation.getId().toString()))
            .body(productFacilityLocation);
    }

    /**
     * {@code PUT  /product-facility-locations/:id} : Updates an existing productFacilityLocation.
     *
     * @param id the id of the productFacilityLocation to save.
     * @param productFacilityLocation the productFacilityLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFacilityLocation,
     * or with status {@code 400 (Bad Request)} if the productFacilityLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFacilityLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFacilityLocation> updateProductFacilityLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFacilityLocation productFacilityLocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFacilityLocation : {}, {}", id, productFacilityLocation);
        if (productFacilityLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFacilityLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFacilityLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFacilityLocation = productFacilityLocationService.update(productFacilityLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFacilityLocation.getId().toString()))
            .body(productFacilityLocation);
    }

    /**
     * {@code PATCH  /product-facility-locations/:id} : Partial updates given fields of an existing productFacilityLocation, field will ignore if it is null
     *
     * @param id the id of the productFacilityLocation to save.
     * @param productFacilityLocation the productFacilityLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFacilityLocation,
     * or with status {@code 400 (Bad Request)} if the productFacilityLocation is not valid,
     * or with status {@code 404 (Not Found)} if the productFacilityLocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFacilityLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFacilityLocation> partialUpdateProductFacilityLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFacilityLocation productFacilityLocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFacilityLocation : {}, {}", id, productFacilityLocation);
        if (productFacilityLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFacilityLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFacilityLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFacilityLocation> result = productFacilityLocationService.partialUpdate(productFacilityLocation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFacilityLocation.getId().toString())
        );
    }

    /**
     * {@code GET  /product-facility-locations} : get all the Product Facility Locations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Facility Locations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFacilityLocation>> getAllProductFacilityLocations(
        ProductFacilityLocationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductFacilityLocations by criteria: {}", criteria);

        Page<ProductFacilityLocation> page = productFacilityLocationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-facility-locations/count} : count all the productFacilityLocations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFacilityLocations(ProductFacilityLocationCriteria criteria) {
        LOG.debug("REST request to count ProductFacilityLocations by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFacilityLocationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-facility-locations/:id} : get the "id" productFacilityLocation.
     *
     * @param id the id of the productFacilityLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFacilityLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFacilityLocation> getProductFacilityLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFacilityLocation : {}", id);
        Optional<ProductFacilityLocation> productFacilityLocation = productFacilityLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFacilityLocation);
    }

    /**
     * {@code DELETE  /product-facility-locations/:id} : delete the "id" productFacilityLocation.
     *
     * @param id the id of the productFacilityLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFacilityLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFacilityLocation : {}", id);
        productFacilityLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
