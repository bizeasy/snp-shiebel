package com.snp.web.rest;

import com.snp.domain.ProductKeyword;
import com.snp.repository.ProductKeywordRepository;
import com.snp.service.ProductKeywordQueryService;
import com.snp.service.ProductKeywordService;
import com.snp.service.criteria.ProductKeywordCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductKeyword}.
 */
@RestController
@RequestMapping("/api/product-keywords")
public class ProductKeywordResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductKeywordResource.class);

    private static final String ENTITY_NAME = "productKeyword";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductKeywordService productKeywordService;

    private final ProductKeywordRepository productKeywordRepository;

    private final ProductKeywordQueryService productKeywordQueryService;

    public ProductKeywordResource(
        ProductKeywordService productKeywordService,
        ProductKeywordRepository productKeywordRepository,
        ProductKeywordQueryService productKeywordQueryService
    ) {
        this.productKeywordService = productKeywordService;
        this.productKeywordRepository = productKeywordRepository;
        this.productKeywordQueryService = productKeywordQueryService;
    }

    /**
     * {@code POST  /product-keywords} : Create a new productKeyword.
     *
     * @param productKeyword the productKeyword to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productKeyword, or with status {@code 400 (Bad Request)} if the productKeyword already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductKeyword> createProductKeyword(@Valid @RequestBody ProductKeyword productKeyword)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductKeyword : {}", productKeyword);
        if (productKeyword.getId() != null) {
            throw new BadRequestAlertException("A new productKeyword cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productKeyword = productKeywordService.save(productKeyword);
        return ResponseEntity.created(new URI("/api/product-keywords/" + productKeyword.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productKeyword.getId().toString()))
            .body(productKeyword);
    }

    /**
     * {@code PUT  /product-keywords/:id} : Updates an existing productKeyword.
     *
     * @param id the id of the productKeyword to save.
     * @param productKeyword the productKeyword to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productKeyword,
     * or with status {@code 400 (Bad Request)} if the productKeyword is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productKeyword couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductKeyword> updateProductKeyword(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductKeyword productKeyword
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductKeyword : {}, {}", id, productKeyword);
        if (productKeyword.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productKeyword.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productKeywordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productKeyword = productKeywordService.update(productKeyword);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productKeyword.getId().toString()))
            .body(productKeyword);
    }

    /**
     * {@code PATCH  /product-keywords/:id} : Partial updates given fields of an existing productKeyword, field will ignore if it is null
     *
     * @param id the id of the productKeyword to save.
     * @param productKeyword the productKeyword to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productKeyword,
     * or with status {@code 400 (Bad Request)} if the productKeyword is not valid,
     * or with status {@code 404 (Not Found)} if the productKeyword is not found,
     * or with status {@code 500 (Internal Server Error)} if the productKeyword couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductKeyword> partialUpdateProductKeyword(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductKeyword productKeyword
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductKeyword : {}, {}", id, productKeyword);
        if (productKeyword.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productKeyword.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productKeywordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductKeyword> result = productKeywordService.partialUpdate(productKeyword);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productKeyword.getId().toString())
        );
    }

    /**
     * {@code GET  /product-keywords} : get all the Product Keywords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Keywords in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductKeyword>> getAllProductKeywords(
        ProductKeywordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductKeywords by criteria: {}", criteria);

        Page<ProductKeyword> page = productKeywordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-keywords/count} : count all the productKeywords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductKeywords(ProductKeywordCriteria criteria) {
        LOG.debug("REST request to count ProductKeywords by criteria: {}", criteria);
        return ResponseEntity.ok().body(productKeywordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-keywords/:id} : get the "id" productKeyword.
     *
     * @param id the id of the productKeyword to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productKeyword, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductKeyword> getProductKeyword(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductKeyword : {}", id);
        Optional<ProductKeyword> productKeyword = productKeywordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productKeyword);
    }

    /**
     * {@code DELETE  /product-keywords/:id} : delete the "id" productKeyword.
     *
     * @param id the id of the productKeyword to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductKeyword(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductKeyword : {}", id);
        productKeywordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
