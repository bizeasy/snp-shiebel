package com.snp.web.rest;

import com.snp.domain.ProductAssoc;
import com.snp.repository.ProductAssocRepository;
import com.snp.service.ProductAssocQueryService;
import com.snp.service.ProductAssocService;
import com.snp.service.criteria.ProductAssocCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductAssoc}.
 */
@RestController
@RequestMapping("/api/product-assocs")
public class ProductAssocResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAssocResource.class);

    private static final String ENTITY_NAME = "productAssoc";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductAssocService productAssocService;

    private final ProductAssocRepository productAssocRepository;

    private final ProductAssocQueryService productAssocQueryService;

    public ProductAssocResource(
        ProductAssocService productAssocService,
        ProductAssocRepository productAssocRepository,
        ProductAssocQueryService productAssocQueryService
    ) {
        this.productAssocService = productAssocService;
        this.productAssocRepository = productAssocRepository;
        this.productAssocQueryService = productAssocQueryService;
    }

    /**
     * {@code POST  /product-assocs} : Create a new productAssoc.
     *
     * @param productAssoc the productAssoc to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productAssoc, or with status {@code 400 (Bad Request)} if the productAssoc already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductAssoc> createProductAssoc(@Valid @RequestBody ProductAssoc productAssoc) throws URISyntaxException {
        LOG.debug("REST request to save ProductAssoc : {}", productAssoc);
        if (productAssoc.getId() != null) {
            throw new BadRequestAlertException("A new productAssoc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productAssoc = productAssocService.save(productAssoc);
        return ResponseEntity.created(new URI("/api/product-assocs/" + productAssoc.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productAssoc.getId().toString()))
            .body(productAssoc);
    }

    /**
     * {@code PUT  /product-assocs/:id} : Updates an existing productAssoc.
     *
     * @param id the id of the productAssoc to save.
     * @param productAssoc the productAssoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAssoc,
     * or with status {@code 400 (Bad Request)} if the productAssoc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productAssoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAssoc> updateProductAssoc(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductAssoc productAssoc
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductAssoc : {}, {}", id, productAssoc);
        if (productAssoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAssoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAssocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productAssoc = productAssocService.update(productAssoc);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAssoc.getId().toString()))
            .body(productAssoc);
    }

    /**
     * {@code PATCH  /product-assocs/:id} : Partial updates given fields of an existing productAssoc, field will ignore if it is null
     *
     * @param id the id of the productAssoc to save.
     * @param productAssoc the productAssoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAssoc,
     * or with status {@code 400 (Bad Request)} if the productAssoc is not valid,
     * or with status {@code 404 (Not Found)} if the productAssoc is not found,
     * or with status {@code 500 (Internal Server Error)} if the productAssoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductAssoc> partialUpdateProductAssoc(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductAssoc productAssoc
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductAssoc : {}, {}", id, productAssoc);
        if (productAssoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAssoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAssocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductAssoc> result = productAssocService.partialUpdate(productAssoc);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAssoc.getId().toString())
        );
    }

    /**
     * {@code GET  /product-assocs} : get all the Product Assocs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Assocs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductAssoc>> getAllProductAssocs(
        ProductAssocCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductAssocs by criteria: {}", criteria);

        Page<ProductAssoc> page = productAssocQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-assocs/count} : count all the productAssocs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductAssocs(ProductAssocCriteria criteria) {
        LOG.debug("REST request to count ProductAssocs by criteria: {}", criteria);
        return ResponseEntity.ok().body(productAssocQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-assocs/:id} : get the "id" productAssoc.
     *
     * @param id the id of the productAssoc to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productAssoc, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAssoc> getProductAssoc(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductAssoc : {}", id);
        Optional<ProductAssoc> productAssoc = productAssocService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productAssoc);
    }

    /**
     * {@code DELETE  /product-assocs/:id} : delete the "id" productAssoc.
     *
     * @param id the id of the productAssoc to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAssoc(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductAssoc : {}", id);
        productAssocService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
