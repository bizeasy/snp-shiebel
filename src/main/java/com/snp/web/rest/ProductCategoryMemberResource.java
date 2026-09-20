package com.snp.web.rest;

import com.snp.domain.ProductCategoryMember;
import com.snp.repository.ProductCategoryMemberRepository;
import com.snp.service.ProductCategoryMemberQueryService;
import com.snp.service.ProductCategoryMemberService;
import com.snp.service.criteria.ProductCategoryMemberCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductCategoryMember}.
 */
@RestController
@RequestMapping("/api/product-category-members")
public class ProductCategoryMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryMemberResource.class);

    private static final String ENTITY_NAME = "productCategoryMember";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductCategoryMemberService productCategoryMemberService;

    private final ProductCategoryMemberRepository productCategoryMemberRepository;

    private final ProductCategoryMemberQueryService productCategoryMemberQueryService;

    public ProductCategoryMemberResource(
        ProductCategoryMemberService productCategoryMemberService,
        ProductCategoryMemberRepository productCategoryMemberRepository,
        ProductCategoryMemberQueryService productCategoryMemberQueryService
    ) {
        this.productCategoryMemberService = productCategoryMemberService;
        this.productCategoryMemberRepository = productCategoryMemberRepository;
        this.productCategoryMemberQueryService = productCategoryMemberQueryService;
    }

    /**
     * {@code POST  /product-category-members} : Create a new productCategoryMember.
     *
     * @param productCategoryMember the productCategoryMember to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCategoryMember, or with status {@code 400 (Bad Request)} if the productCategoryMember already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductCategoryMember> createProductCategoryMember(
        @Valid @RequestBody ProductCategoryMember productCategoryMember
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductCategoryMember : {}", productCategoryMember);
        if (productCategoryMember.getId() != null) {
            throw new BadRequestAlertException("A new productCategoryMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productCategoryMember = productCategoryMemberService.save(productCategoryMember);
        return ResponseEntity.created(new URI("/api/product-category-members/" + productCategoryMember.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productCategoryMember.getId().toString()))
            .body(productCategoryMember);
    }

    /**
     * {@code PUT  /product-category-members/:id} : Updates an existing productCategoryMember.
     *
     * @param id the id of the productCategoryMember to save.
     * @param productCategoryMember the productCategoryMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategoryMember,
     * or with status {@code 400 (Bad Request)} if the productCategoryMember is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCategoryMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryMember> updateProductCategoryMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductCategoryMember productCategoryMember
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductCategoryMember : {}, {}", id, productCategoryMember);
        if (productCategoryMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategoryMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productCategoryMember = productCategoryMemberService.update(productCategoryMember);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCategoryMember.getId().toString()))
            .body(productCategoryMember);
    }

    /**
     * {@code PATCH  /product-category-members/:id} : Partial updates given fields of an existing productCategoryMember, field will ignore if it is null
     *
     * @param id the id of the productCategoryMember to save.
     * @param productCategoryMember the productCategoryMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategoryMember,
     * or with status {@code 400 (Bad Request)} if the productCategoryMember is not valid,
     * or with status {@code 404 (Not Found)} if the productCategoryMember is not found,
     * or with status {@code 500 (Internal Server Error)} if the productCategoryMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductCategoryMember> partialUpdateProductCategoryMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductCategoryMember productCategoryMember
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductCategoryMember : {}, {}", id, productCategoryMember);
        if (productCategoryMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategoryMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductCategoryMember> result = productCategoryMemberService.partialUpdate(productCategoryMember);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCategoryMember.getId().toString())
        );
    }

    /**
     * {@code GET  /product-category-members} : get all the Product Category Members.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Category Members in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductCategoryMember>> getAllProductCategoryMembers(
        ProductCategoryMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductCategoryMembers by criteria: {}", criteria);

        Page<ProductCategoryMember> page = productCategoryMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-category-members/count} : count all the productCategoryMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductCategoryMembers(ProductCategoryMemberCriteria criteria) {
        LOG.debug("REST request to count ProductCategoryMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(productCategoryMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-category-members/:id} : get the "id" productCategoryMember.
     *
     * @param id the id of the productCategoryMember to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCategoryMember, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryMember> getProductCategoryMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductCategoryMember : {}", id);
        Optional<ProductCategoryMember> productCategoryMember = productCategoryMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productCategoryMember);
    }

    /**
     * {@code DELETE  /product-category-members/:id} : delete the "id" productCategoryMember.
     *
     * @param id the id of the productCategoryMember to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategoryMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductCategoryMember : {}", id);
        productCategoryMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
