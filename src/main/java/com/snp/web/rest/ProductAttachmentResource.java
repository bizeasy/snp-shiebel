package com.snp.web.rest;

import com.snp.domain.ProductAttachment;
import com.snp.repository.ProductAttachmentRepository;
import com.snp.service.ProductAttachmentQueryService;
import com.snp.service.ProductAttachmentService;
import com.snp.service.criteria.ProductAttachmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductAttachment}.
 */
@RestController
@RequestMapping("/api/product-attachments")
public class ProductAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAttachmentResource.class);

    private static final String ENTITY_NAME = "productAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductAttachmentService productAttachmentService;

    private final ProductAttachmentRepository productAttachmentRepository;

    private final ProductAttachmentQueryService productAttachmentQueryService;

    public ProductAttachmentResource(
        ProductAttachmentService productAttachmentService,
        ProductAttachmentRepository productAttachmentRepository,
        ProductAttachmentQueryService productAttachmentQueryService
    ) {
        this.productAttachmentService = productAttachmentService;
        this.productAttachmentRepository = productAttachmentRepository;
        this.productAttachmentQueryService = productAttachmentQueryService;
    }

    /**
     * {@code POST  /product-attachments} : Create a new productAttachment.
     *
     * @param productAttachment the productAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productAttachment, or with status {@code 400 (Bad Request)} if the productAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductAttachment> createProductAttachment(@Valid @RequestBody ProductAttachment productAttachment)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductAttachment : {}", productAttachment);
        if (productAttachment.getId() != null) {
            throw new BadRequestAlertException("A new productAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productAttachment = productAttachmentService.save(productAttachment);
        return ResponseEntity.created(new URI("/api/product-attachments/" + productAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productAttachment.getId().toString()))
            .body(productAttachment);
    }

    /**
     * {@code PUT  /product-attachments/:id} : Updates an existing productAttachment.
     *
     * @param id the id of the productAttachment to save.
     * @param productAttachment the productAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAttachment,
     * or with status {@code 400 (Bad Request)} if the productAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAttachment> updateProductAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductAttachment productAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductAttachment : {}, {}", id, productAttachment);
        if (productAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productAttachment = productAttachmentService.update(productAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAttachment.getId().toString()))
            .body(productAttachment);
    }

    /**
     * {@code PATCH  /product-attachments/:id} : Partial updates given fields of an existing productAttachment, field will ignore if it is null
     *
     * @param id the id of the productAttachment to save.
     * @param productAttachment the productAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAttachment,
     * or with status {@code 400 (Bad Request)} if the productAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the productAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the productAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductAttachment> partialUpdateProductAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductAttachment productAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductAttachment : {}, {}", id, productAttachment);
        if (productAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductAttachment> result = productAttachmentService.partialUpdate(productAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /product-attachments} : get all the Product Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductAttachment>> getAllProductAttachments(
        ProductAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductAttachments by criteria: {}", criteria);

        Page<ProductAttachment> page = productAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-attachments/count} : count all the productAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductAttachments(ProductAttachmentCriteria criteria) {
        LOG.debug("REST request to count ProductAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(productAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-attachments/:id} : get the "id" productAttachment.
     *
     * @param id the id of the productAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAttachment> getProductAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductAttachment : {}", id);
        Optional<ProductAttachment> productAttachment = productAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productAttachment);
    }

    /**
     * {@code DELETE  /product-attachments/:id} : delete the "id" productAttachment.
     *
     * @param id the id of the productAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductAttachment : {}", id);
        productAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
