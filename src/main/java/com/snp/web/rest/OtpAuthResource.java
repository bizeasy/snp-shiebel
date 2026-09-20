package com.snp.web.rest;

import com.snp.domain.OtpAuth;
import com.snp.repository.OtpAuthRepository;
import com.snp.service.OtpAuthQueryService;
import com.snp.service.OtpAuthService;
import com.snp.service.criteria.OtpAuthCriteria;
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
 * REST controller for managing {@link com.snp.domain.OtpAuth}.
 */
@RestController
@RequestMapping("/api/otp-auths")
public class OtpAuthResource {

    private static final Logger LOG = LoggerFactory.getLogger(OtpAuthResource.class);

    private static final String ENTITY_NAME = "otpAuth";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OtpAuthService otpAuthService;

    private final OtpAuthRepository otpAuthRepository;

    private final OtpAuthQueryService otpAuthQueryService;

    public OtpAuthResource(OtpAuthService otpAuthService, OtpAuthRepository otpAuthRepository, OtpAuthQueryService otpAuthQueryService) {
        this.otpAuthService = otpAuthService;
        this.otpAuthRepository = otpAuthRepository;
        this.otpAuthQueryService = otpAuthQueryService;
    }

    /**
     * {@code POST  /otp-auths} : Create a new otpAuth.
     *
     * @param otpAuth the otpAuth to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otpAuth, or with status {@code 400 (Bad Request)} if the otpAuth already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OtpAuth> createOtpAuth(@Valid @RequestBody OtpAuth otpAuth) throws URISyntaxException {
        LOG.debug("REST request to save OtpAuth : {}", otpAuth);
        if (otpAuth.getId() != null) {
            throw new BadRequestAlertException("A new otpAuth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        otpAuth = otpAuthService.save(otpAuth);
        return ResponseEntity.created(new URI("/api/otp-auths/" + otpAuth.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, otpAuth.getId().toString()))
            .body(otpAuth);
    }

    /**
     * {@code PUT  /otp-auths/:id} : Updates an existing otpAuth.
     *
     * @param id the id of the otpAuth to save.
     * @param otpAuth the otpAuth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpAuth,
     * or with status {@code 400 (Bad Request)} if the otpAuth is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otpAuth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OtpAuth> updateOtpAuth(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OtpAuth otpAuth
    ) throws URISyntaxException {
        LOG.debug("REST request to update OtpAuth : {}, {}", id, otpAuth);
        if (otpAuth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpAuth.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpAuthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        otpAuth = otpAuthService.update(otpAuth);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpAuth.getId().toString()))
            .body(otpAuth);
    }

    /**
     * {@code PATCH  /otp-auths/:id} : Partial updates given fields of an existing otpAuth, field will ignore if it is null
     *
     * @param id the id of the otpAuth to save.
     * @param otpAuth the otpAuth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpAuth,
     * or with status {@code 400 (Bad Request)} if the otpAuth is not valid,
     * or with status {@code 404 (Not Found)} if the otpAuth is not found,
     * or with status {@code 500 (Internal Server Error)} if the otpAuth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OtpAuth> partialUpdateOtpAuth(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OtpAuth otpAuth
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OtpAuth : {}, {}", id, otpAuth);
        if (otpAuth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpAuth.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpAuthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OtpAuth> result = otpAuthService.partialUpdate(otpAuth);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpAuth.getId().toString())
        );
    }

    /**
     * {@code GET  /otp-auths} : get all the Otp Auths.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Otp Auths in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OtpAuth>> getAllOtpAuths(
        OtpAuthCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OtpAuths by criteria: {}", criteria);

        Page<OtpAuth> page = otpAuthQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /otp-auths/count} : count all the otpAuths.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOtpAuths(OtpAuthCriteria criteria) {
        LOG.debug("REST request to count OtpAuths by criteria: {}", criteria);
        return ResponseEntity.ok().body(otpAuthQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /otp-auths/:id} : get the "id" otpAuth.
     *
     * @param id the id of the otpAuth to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otpAuth, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OtpAuth> getOtpAuth(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OtpAuth : {}", id);
        Optional<OtpAuth> otpAuth = otpAuthService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otpAuth);
    }

    /**
     * {@code DELETE  /otp-auths/:id} : delete the "id" otpAuth.
     *
     * @param id the id of the otpAuth to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOtpAuth(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OtpAuth : {}", id);
        otpAuthService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
