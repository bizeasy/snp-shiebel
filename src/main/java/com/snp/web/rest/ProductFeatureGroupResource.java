package com.snp.web.rest;

import com.snp.domain.ProductFeatureGroup;
import com.snp.repository.ProductFeatureGroupRepository;
import com.snp.service.ProductFeatureGroupQueryService;
import com.snp.service.ProductFeatureGroupService;
import com.snp.service.criteria.ProductFeatureGroupCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFeatureGroup}.
 */
@RestController
@RequestMapping("/api/product-feature-groups")
public class ProductFeatureGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureGroupResource.class);

    private static final String ENTITY_NAME = "productFeatureGroup";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFeatureGroupService productFeatureGroupService;

    private final ProductFeatureGroupRepository productFeatureGroupRepository;

    private final ProductFeatureGroupQueryService productFeatureGroupQueryService;

    public ProductFeatureGroupResource(
        ProductFeatureGroupService productFeatureGroupService,
        ProductFeatureGroupRepository productFeatureGroupRepository,
        ProductFeatureGroupQueryService productFeatureGroupQueryService
    ) {
        this.productFeatureGroupService = productFeatureGroupService;
        this.productFeatureGroupRepository = productFeatureGroupRepository;
        this.productFeatureGroupQueryService = productFeatureGroupQueryService;
    }

    /**
     * {@code POST  /product-feature-groups} : Create a new productFeatureGroup.
     *
     * @param productFeatureGroup the productFeatureGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeatureGroup, or with status {@code 400 (Bad Request)} if the productFeatureGroup already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFeatureGroup> createProductFeatureGroup(@Valid @RequestBody ProductFeatureGroup productFeatureGroup)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductFeatureGroup : {}", productFeatureGroup);
        if (productFeatureGroup.getId() != null) {
            throw new BadRequestAlertException("A new productFeatureGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFeatureGroup = productFeatureGroupService.save(productFeatureGroup);
        return ResponseEntity.created(new URI("/api/product-feature-groups/" + productFeatureGroup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFeatureGroup.getId().toString()))
            .body(productFeatureGroup);
    }

    /**
     * {@code PUT  /product-feature-groups/:id} : Updates an existing productFeatureGroup.
     *
     * @param id the id of the productFeatureGroup to save.
     * @param productFeatureGroup the productFeatureGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureGroup,
     * or with status {@code 400 (Bad Request)} if the productFeatureGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFeatureGroup> updateProductFeatureGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFeatureGroup productFeatureGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFeatureGroup : {}, {}", id, productFeatureGroup);
        if (productFeatureGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFeatureGroup = productFeatureGroupService.update(productFeatureGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureGroup.getId().toString()))
            .body(productFeatureGroup);
    }

    /**
     * {@code PATCH  /product-feature-groups/:id} : Partial updates given fields of an existing productFeatureGroup, field will ignore if it is null
     *
     * @param id the id of the productFeatureGroup to save.
     * @param productFeatureGroup the productFeatureGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureGroup,
     * or with status {@code 400 (Bad Request)} if the productFeatureGroup is not valid,
     * or with status {@code 404 (Not Found)} if the productFeatureGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeatureGroup> partialUpdateProductFeatureGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFeatureGroup productFeatureGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFeatureGroup : {}, {}", id, productFeatureGroup);
        if (productFeatureGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeatureGroup> result = productFeatureGroupService.partialUpdate(productFeatureGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /product-feature-groups} : get all the Product Feature Groups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Feature Groups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFeatureGroup>> getAllProductFeatureGroups(ProductFeatureGroupCriteria criteria) {
        LOG.debug("REST request to get ProductFeatureGroups by criteria: {}", criteria);

        List<ProductFeatureGroup> entityList = productFeatureGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-feature-groups/count} : count all the productFeatureGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFeatureGroups(ProductFeatureGroupCriteria criteria) {
        LOG.debug("REST request to count ProductFeatureGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFeatureGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-feature-groups/:id} : get the "id" productFeatureGroup.
     *
     * @param id the id of the productFeatureGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeatureGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureGroup> getProductFeatureGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFeatureGroup : {}", id);
        Optional<ProductFeatureGroup> productFeatureGroup = productFeatureGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFeatureGroup);
    }

    /**
     * {@code DELETE  /product-feature-groups/:id} : delete the "id" productFeatureGroup.
     *
     * @param id the id of the productFeatureGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFeatureGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFeatureGroup : {}", id);
        productFeatureGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
