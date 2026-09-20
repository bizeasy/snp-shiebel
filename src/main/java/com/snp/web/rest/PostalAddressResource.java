package com.snp.web.rest;

import com.snp.domain.PostalAddress;
import com.snp.repository.PostalAddressRepository;
import com.snp.service.PostalAddressQueryService;
import com.snp.service.PostalAddressService;
import com.snp.service.criteria.PostalAddressCriteria;
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
 * REST controller for managing {@link com.snp.domain.PostalAddress}.
 */
@RestController
@RequestMapping("/api/postal-addresses")
public class PostalAddressResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostalAddressResource.class);

    private static final String ENTITY_NAME = "postalAddress";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PostalAddressService postalAddressService;

    private final PostalAddressRepository postalAddressRepository;

    private final PostalAddressQueryService postalAddressQueryService;

    public PostalAddressResource(
        PostalAddressService postalAddressService,
        PostalAddressRepository postalAddressRepository,
        PostalAddressQueryService postalAddressQueryService
    ) {
        this.postalAddressService = postalAddressService;
        this.postalAddressRepository = postalAddressRepository;
        this.postalAddressQueryService = postalAddressQueryService;
    }

    /**
     * {@code POST  /postal-addresses} : Create a new postalAddress.
     *
     * @param postalAddress the postalAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postalAddress, or with status {@code 400 (Bad Request)} if the postalAddress already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostalAddress> createPostalAddress(@Valid @RequestBody PostalAddress postalAddress) throws URISyntaxException {
        LOG.debug("REST request to save PostalAddress : {}", postalAddress);
        if (postalAddress.getId() != null) {
            throw new BadRequestAlertException("A new postalAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postalAddress = postalAddressService.save(postalAddress);
        return ResponseEntity.created(new URI("/api/postal-addresses/" + postalAddress.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, postalAddress.getId().toString()))
            .body(postalAddress);
    }

    /**
     * {@code PUT  /postal-addresses/:id} : Updates an existing postalAddress.
     *
     * @param id the id of the postalAddress to save.
     * @param postalAddress the postalAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postalAddress,
     * or with status {@code 400 (Bad Request)} if the postalAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postalAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostalAddress> updatePostalAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostalAddress postalAddress
    ) throws URISyntaxException {
        LOG.debug("REST request to update PostalAddress : {}, {}", id, postalAddress);
        if (postalAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postalAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postalAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postalAddress = postalAddressService.update(postalAddress);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postalAddress.getId().toString()))
            .body(postalAddress);
    }

    /**
     * {@code PATCH  /postal-addresses/:id} : Partial updates given fields of an existing postalAddress, field will ignore if it is null
     *
     * @param id the id of the postalAddress to save.
     * @param postalAddress the postalAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postalAddress,
     * or with status {@code 400 (Bad Request)} if the postalAddress is not valid,
     * or with status {@code 404 (Not Found)} if the postalAddress is not found,
     * or with status {@code 500 (Internal Server Error)} if the postalAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostalAddress> partialUpdatePostalAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostalAddress postalAddress
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PostalAddress : {}, {}", id, postalAddress);
        if (postalAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postalAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postalAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostalAddress> result = postalAddressService.partialUpdate(postalAddress);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postalAddress.getId().toString())
        );
    }

    /**
     * {@code GET  /postal-addresses} : get all the Postal Addresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Postal Addresses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PostalAddress>> getAllPostalAddresses(
        PostalAddressCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PostalAddresses by criteria: {}", criteria);

        Page<PostalAddress> page = postalAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /postal-addresses/count} : count all the postalAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPostalAddresses(PostalAddressCriteria criteria) {
        LOG.debug("REST request to count PostalAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(postalAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /postal-addresses/:id} : get the "id" postalAddress.
     *
     * @param id the id of the postalAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postalAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostalAddress> getPostalAddress(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PostalAddress : {}", id);
        Optional<PostalAddress> postalAddress = postalAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postalAddress);
    }

    /**
     * {@code DELETE  /postal-addresses/:id} : delete the "id" postalAddress.
     *
     * @param id the id of the postalAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostalAddress(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PostalAddress : {}", id);
        postalAddressService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
