package com.snp.web.rest;

import com.snp.domain.PaymentApplication;
import com.snp.repository.PaymentApplicationRepository;
import com.snp.service.PaymentApplicationQueryService;
import com.snp.service.PaymentApplicationService;
import com.snp.service.criteria.PaymentApplicationCriteria;
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
 * REST controller for managing {@link com.snp.domain.PaymentApplication}.
 */
@RestController
@RequestMapping("/api/payment-applications")
public class PaymentApplicationResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentApplicationResource.class);

    private static final String ENTITY_NAME = "paymentApplication";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PaymentApplicationService paymentApplicationService;

    private final PaymentApplicationRepository paymentApplicationRepository;

    private final PaymentApplicationQueryService paymentApplicationQueryService;

    public PaymentApplicationResource(
        PaymentApplicationService paymentApplicationService,
        PaymentApplicationRepository paymentApplicationRepository,
        PaymentApplicationQueryService paymentApplicationQueryService
    ) {
        this.paymentApplicationService = paymentApplicationService;
        this.paymentApplicationRepository = paymentApplicationRepository;
        this.paymentApplicationQueryService = paymentApplicationQueryService;
    }

    /**
     * {@code POST  /payment-applications} : Create a new paymentApplication.
     *
     * @param paymentApplication the paymentApplication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentApplication, or with status {@code 400 (Bad Request)} if the paymentApplication already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentApplication> createPaymentApplication(@Valid @RequestBody PaymentApplication paymentApplication)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentApplication : {}", paymentApplication);
        if (paymentApplication.getId() != null) {
            throw new BadRequestAlertException("A new paymentApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentApplication = paymentApplicationService.save(paymentApplication);
        return ResponseEntity.created(new URI("/api/payment-applications/" + paymentApplication.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentApplication.getId().toString()))
            .body(paymentApplication);
    }

    /**
     * {@code PUT  /payment-applications/:id} : Updates an existing paymentApplication.
     *
     * @param id the id of the paymentApplication to save.
     * @param paymentApplication the paymentApplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentApplication,
     * or with status {@code 400 (Bad Request)} if the paymentApplication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentApplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentApplication> updatePaymentApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentApplication paymentApplication
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentApplication : {}, {}", id, paymentApplication);
        if (paymentApplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentApplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentApplication = paymentApplicationService.update(paymentApplication);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentApplication.getId().toString()))
            .body(paymentApplication);
    }

    /**
     * {@code PATCH  /payment-applications/:id} : Partial updates given fields of an existing paymentApplication, field will ignore if it is null
     *
     * @param id the id of the paymentApplication to save.
     * @param paymentApplication the paymentApplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentApplication,
     * or with status {@code 400 (Bad Request)} if the paymentApplication is not valid,
     * or with status {@code 404 (Not Found)} if the paymentApplication is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentApplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentApplication> partialUpdatePaymentApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentApplication paymentApplication
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PaymentApplication : {}, {}", id, paymentApplication);
        if (paymentApplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentApplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentApplication> result = paymentApplicationService.partialUpdate(paymentApplication);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentApplication.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-applications} : get all the Payment Applications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Payment Applications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentApplication>> getAllPaymentApplications(
        PaymentApplicationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentApplications by criteria: {}", criteria);

        Page<PaymentApplication> page = paymentApplicationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-applications/count} : count all the paymentApplications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentApplications(PaymentApplicationCriteria criteria) {
        LOG.debug("REST request to count PaymentApplications by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentApplicationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-applications/:id} : get the "id" paymentApplication.
     *
     * @param id the id of the paymentApplication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentApplication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentApplication> getPaymentApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentApplication : {}", id);
        Optional<PaymentApplication> paymentApplication = paymentApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentApplication);
    }

    /**
     * {@code DELETE  /payment-applications/:id} : delete the "id" paymentApplication.
     *
     * @param id the id of the paymentApplication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentApplication : {}", id);
        paymentApplicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
