package com.snp.web.rest;

import com.snp.domain.ProductFacilityUser;
import com.snp.repository.ProductFacilityUserRepository;
import com.snp.service.ProductFacilityUserQueryService;
import com.snp.service.ProductFacilityUserService;
import com.snp.service.criteria.ProductFacilityUserCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFacilityUser}.
 */
@RestController
@RequestMapping("/api/product-facility-users")
public class ProductFacilityUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityUserResource.class);

    private static final String ENTITY_NAME = "productFacilityUser";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFacilityUserService productFacilityUserService;

    private final ProductFacilityUserRepository productFacilityUserRepository;

    private final ProductFacilityUserQueryService productFacilityUserQueryService;

    public ProductFacilityUserResource(
        ProductFacilityUserService productFacilityUserService,
        ProductFacilityUserRepository productFacilityUserRepository,
        ProductFacilityUserQueryService productFacilityUserQueryService
    ) {
        this.productFacilityUserService = productFacilityUserService;
        this.productFacilityUserRepository = productFacilityUserRepository;
        this.productFacilityUserQueryService = productFacilityUserQueryService;
    }

    /**
     * {@code POST  /product-facility-users} : Create a new productFacilityUser.
     *
     * @param productFacilityUser the productFacilityUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFacilityUser, or with status {@code 400 (Bad Request)} if the productFacilityUser already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFacilityUser> createProductFacilityUser(@Valid @RequestBody ProductFacilityUser productFacilityUser)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductFacilityUser : {}", productFacilityUser);
        if (productFacilityUser.getId() != null) {
            throw new BadRequestAlertException("A new productFacilityUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFacilityUser = productFacilityUserService.save(productFacilityUser);
        return ResponseEntity.created(new URI("/api/product-facility-users/" + productFacilityUser.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFacilityUser.getId().toString()))
            .body(productFacilityUser);
    }

    /**
     * {@code PUT  /product-facility-users/:id} : Updates an existing productFacilityUser.
     *
     * @param id the id of the productFacilityUser to save.
     * @param productFacilityUser the productFacilityUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFacilityUser,
     * or with status {@code 400 (Bad Request)} if the productFacilityUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFacilityUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFacilityUser> updateProductFacilityUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFacilityUser productFacilityUser
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFacilityUser : {}, {}", id, productFacilityUser);
        if (productFacilityUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFacilityUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFacilityUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFacilityUser = productFacilityUserService.update(productFacilityUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFacilityUser.getId().toString()))
            .body(productFacilityUser);
    }

    /**
     * {@code PATCH  /product-facility-users/:id} : Partial updates given fields of an existing productFacilityUser, field will ignore if it is null
     *
     * @param id the id of the productFacilityUser to save.
     * @param productFacilityUser the productFacilityUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFacilityUser,
     * or with status {@code 400 (Bad Request)} if the productFacilityUser is not valid,
     * or with status {@code 404 (Not Found)} if the productFacilityUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFacilityUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFacilityUser> partialUpdateProductFacilityUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFacilityUser productFacilityUser
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFacilityUser : {}, {}", id, productFacilityUser);
        if (productFacilityUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFacilityUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFacilityUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFacilityUser> result = productFacilityUserService.partialUpdate(productFacilityUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFacilityUser.getId().toString())
        );
    }

    /**
     * {@code GET  /product-facility-users} : get all the Product Facility Users.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Facility Users in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFacilityUser>> getAllProductFacilityUsers(
        ProductFacilityUserCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductFacilityUsers by criteria: {}", criteria);

        Page<ProductFacilityUser> page = productFacilityUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-facility-users/count} : count all the productFacilityUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFacilityUsers(ProductFacilityUserCriteria criteria) {
        LOG.debug("REST request to count ProductFacilityUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFacilityUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-facility-users/:id} : get the "id" productFacilityUser.
     *
     * @param id the id of the productFacilityUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFacilityUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFacilityUser> getProductFacilityUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFacilityUser : {}", id);
        Optional<ProductFacilityUser> productFacilityUser = productFacilityUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFacilityUser);
    }

    /**
     * {@code DELETE  /product-facility-users/:id} : delete the "id" productFacilityUser.
     *
     * @param id the id of the productFacilityUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFacilityUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFacilityUser : {}", id);
        productFacilityUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
