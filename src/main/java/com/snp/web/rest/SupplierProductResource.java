package com.snp.web.rest;

import com.snp.domain.SupplierProduct;
import com.snp.repository.SupplierProductRepository;
import com.snp.service.SupplierProductQueryService;
import com.snp.service.SupplierProductService;
import com.snp.service.criteria.SupplierProductCriteria;
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
 * REST controller for managing {@link com.snp.domain.SupplierProduct}.
 */
@RestController
@RequestMapping("/api/supplier-products")
public class SupplierProductResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierProductResource.class);

    private static final String ENTITY_NAME = "supplierProduct";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final SupplierProductService supplierProductService;

    private final SupplierProductRepository supplierProductRepository;

    private final SupplierProductQueryService supplierProductQueryService;

    public SupplierProductResource(
        SupplierProductService supplierProductService,
        SupplierProductRepository supplierProductRepository,
        SupplierProductQueryService supplierProductQueryService
    ) {
        this.supplierProductService = supplierProductService;
        this.supplierProductRepository = supplierProductRepository;
        this.supplierProductQueryService = supplierProductQueryService;
    }

    /**
     * {@code POST  /supplier-products} : Create a new supplierProduct.
     *
     * @param supplierProduct the supplierProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierProduct, or with status {@code 400 (Bad Request)} if the supplierProduct already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierProduct> createSupplierProduct(@Valid @RequestBody SupplierProduct supplierProduct)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierProduct : {}", supplierProduct);
        if (supplierProduct.getId() != null) {
            throw new BadRequestAlertException("A new supplierProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierProduct = supplierProductService.save(supplierProduct);
        return ResponseEntity.created(new URI("/api/supplier-products/" + supplierProduct.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierProduct.getId().toString()))
            .body(supplierProduct);
    }

    /**
     * {@code PUT  /supplier-products/:id} : Updates an existing supplierProduct.
     *
     * @param id the id of the supplierProduct to save.
     * @param supplierProduct the supplierProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierProduct,
     * or with status {@code 400 (Bad Request)} if the supplierProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierProduct> updateSupplierProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierProduct supplierProduct
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierProduct : {}, {}", id, supplierProduct);
        if (supplierProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierProduct = supplierProductService.update(supplierProduct);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierProduct.getId().toString()))
            .body(supplierProduct);
    }

    /**
     * {@code PATCH  /supplier-products/:id} : Partial updates given fields of an existing supplierProduct, field will ignore if it is null
     *
     * @param id the id of the supplierProduct to save.
     * @param supplierProduct the supplierProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierProduct,
     * or with status {@code 400 (Bad Request)} if the supplierProduct is not valid,
     * or with status {@code 404 (Not Found)} if the supplierProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierProduct> partialUpdateSupplierProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierProduct supplierProduct
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update SupplierProduct : {}, {}", id, supplierProduct);
        if (supplierProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierProduct> result = supplierProductService.partialUpdate(supplierProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-products} : get all the Supplier Products.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Supplier Products in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierProduct>> getAllSupplierProducts(
        SupplierProductCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierProducts by criteria: {}", criteria);

        Page<SupplierProduct> page = supplierProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-products/count} : count all the supplierProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierProducts(SupplierProductCriteria criteria) {
        LOG.debug("REST request to count SupplierProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-products/:id} : get the "id" supplierProduct.
     *
     * @param id the id of the supplierProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierProduct> getSupplierProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierProduct : {}", id);
        Optional<SupplierProduct> supplierProduct = supplierProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierProduct);
    }

    /**
     * {@code DELETE  /supplier-products/:id} : delete the "id" supplierProduct.
     *
     * @param id the id of the supplierProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierProduct : {}", id);
        supplierProductService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
