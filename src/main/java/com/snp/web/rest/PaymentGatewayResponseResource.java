package com.snp.web.rest;

import com.snp.domain.PaymentGatewayResponse;
import com.snp.repository.PaymentGatewayResponseRepository;
import com.snp.service.PaymentGatewayResponseQueryService;
import com.snp.service.PaymentGatewayResponseService;
import com.snp.service.criteria.PaymentGatewayResponseCriteria;
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
 * REST controller for managing {@link com.snp.domain.PaymentGatewayResponse}.
 */
@RestController
@RequestMapping("/api/payment-gateway-responses")
public class PaymentGatewayResponseResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayResponseResource.class);

    private static final String ENTITY_NAME = "paymentGatewayResponse";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PaymentGatewayResponseService paymentGatewayResponseService;

    private final PaymentGatewayResponseRepository paymentGatewayResponseRepository;

    private final PaymentGatewayResponseQueryService paymentGatewayResponseQueryService;

    public PaymentGatewayResponseResource(
        PaymentGatewayResponseService paymentGatewayResponseService,
        PaymentGatewayResponseRepository paymentGatewayResponseRepository,
        PaymentGatewayResponseQueryService paymentGatewayResponseQueryService
    ) {
        this.paymentGatewayResponseService = paymentGatewayResponseService;
        this.paymentGatewayResponseRepository = paymentGatewayResponseRepository;
        this.paymentGatewayResponseQueryService = paymentGatewayResponseQueryService;
    }

    /**
     * {@code POST  /payment-gateway-responses} : Create a new paymentGatewayResponse.
     *
     * @param paymentGatewayResponse the paymentGatewayResponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentGatewayResponse, or with status {@code 400 (Bad Request)} if the paymentGatewayResponse already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentGatewayResponse> createPaymentGatewayResponse(
        @Valid @RequestBody PaymentGatewayResponse paymentGatewayResponse
    ) throws URISyntaxException {
        LOG.debug("REST request to save PaymentGatewayResponse : {}", paymentGatewayResponse);
        if (paymentGatewayResponse.getId() != null) {
            throw new BadRequestAlertException("A new paymentGatewayResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentGatewayResponse = paymentGatewayResponseService.save(paymentGatewayResponse);
        return ResponseEntity.created(new URI("/api/payment-gateway-responses/" + paymentGatewayResponse.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentGatewayResponse.getId().toString()))
            .body(paymentGatewayResponse);
    }

    /**
     * {@code PUT  /payment-gateway-responses/:id} : Updates an existing paymentGatewayResponse.
     *
     * @param id the id of the paymentGatewayResponse to save.
     * @param paymentGatewayResponse the paymentGatewayResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentGatewayResponse,
     * or with status {@code 400 (Bad Request)} if the paymentGatewayResponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentGatewayResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentGatewayResponse> updatePaymentGatewayResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentGatewayResponse paymentGatewayResponse
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentGatewayResponse : {}, {}", id, paymentGatewayResponse);
        if (paymentGatewayResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentGatewayResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentGatewayResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentGatewayResponse = paymentGatewayResponseService.update(paymentGatewayResponse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentGatewayResponse.getId().toString()))
            .body(paymentGatewayResponse);
    }

    /**
     * {@code PATCH  /payment-gateway-responses/:id} : Partial updates given fields of an existing paymentGatewayResponse, field will ignore if it is null
     *
     * @param id the id of the paymentGatewayResponse to save.
     * @param paymentGatewayResponse the paymentGatewayResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentGatewayResponse,
     * or with status {@code 400 (Bad Request)} if the paymentGatewayResponse is not valid,
     * or with status {@code 404 (Not Found)} if the paymentGatewayResponse is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentGatewayResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentGatewayResponse> partialUpdatePaymentGatewayResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentGatewayResponse paymentGatewayResponse
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PaymentGatewayResponse : {}, {}", id, paymentGatewayResponse);
        if (paymentGatewayResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentGatewayResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentGatewayResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentGatewayResponse> result = paymentGatewayResponseService.partialUpdate(paymentGatewayResponse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentGatewayResponse.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-gateway-responses} : get all the Payment Gateway Responses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Payment Gateway Responses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentGatewayResponse>> getAllPaymentGatewayResponses(
        PaymentGatewayResponseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentGatewayResponses by criteria: {}", criteria);

        Page<PaymentGatewayResponse> page = paymentGatewayResponseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-gateway-responses/count} : count all the paymentGatewayResponses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentGatewayResponses(PaymentGatewayResponseCriteria criteria) {
        LOG.debug("REST request to count PaymentGatewayResponses by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentGatewayResponseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-gateway-responses/:id} : get the "id" paymentGatewayResponse.
     *
     * @param id the id of the paymentGatewayResponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentGatewayResponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentGatewayResponse> getPaymentGatewayResponse(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentGatewayResponse : {}", id);
        Optional<PaymentGatewayResponse> paymentGatewayResponse = paymentGatewayResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentGatewayResponse);
    }

    /**
     * {@code DELETE  /payment-gateway-responses/:id} : delete the "id" paymentGatewayResponse.
     *
     * @param id the id of the paymentGatewayResponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentGatewayResponse(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentGatewayResponse : {}", id);
        paymentGatewayResponseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
