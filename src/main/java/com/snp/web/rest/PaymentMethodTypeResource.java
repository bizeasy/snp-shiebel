package com.snp.web.rest;

import com.snp.domain.PaymentMethodType;
import com.snp.repository.PaymentMethodTypeRepository;
import com.snp.service.PaymentMethodTypeQueryService;
import com.snp.service.PaymentMethodTypeService;
import com.snp.service.criteria.PaymentMethodTypeCriteria;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.PaymentMethodType}.
 */
@RestController
@RequestMapping("/api/payment-method-types")
public class PaymentMethodTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodTypeResource.class);

    private static final String ENTITY_NAME = "paymentMethodType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PaymentMethodTypeService paymentMethodTypeService;

    private final PaymentMethodTypeRepository paymentMethodTypeRepository;

    private final PaymentMethodTypeQueryService paymentMethodTypeQueryService;

    public PaymentMethodTypeResource(
        PaymentMethodTypeService paymentMethodTypeService,
        PaymentMethodTypeRepository paymentMethodTypeRepository,
        PaymentMethodTypeQueryService paymentMethodTypeQueryService
    ) {
        this.paymentMethodTypeService = paymentMethodTypeService;
        this.paymentMethodTypeRepository = paymentMethodTypeRepository;
        this.paymentMethodTypeQueryService = paymentMethodTypeQueryService;
    }

    /**
     * {@code POST  /payment-method-types} : Create a new paymentMethodType.
     *
     * @param paymentMethodType the paymentMethodType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentMethodType, or with status {@code 400 (Bad Request)} if the paymentMethodType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentMethodType> createPaymentMethodType(@Valid @RequestBody PaymentMethodType paymentMethodType)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentMethodType : {}", paymentMethodType);
        if (paymentMethodType.getId() != null) {
            throw new BadRequestAlertException("A new paymentMethodType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentMethodType = paymentMethodTypeService.save(paymentMethodType);
        return ResponseEntity.created(new URI("/api/payment-method-types/" + paymentMethodType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentMethodType.getId().toString()))
            .body(paymentMethodType);
    }

    /**
     * {@code PUT  /payment-method-types/:id} : Updates an existing paymentMethodType.
     *
     * @param id the id of the paymentMethodType to save.
     * @param paymentMethodType the paymentMethodType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMethodType,
     * or with status {@code 400 (Bad Request)} if the paymentMethodType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethodType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodType> updatePaymentMethodType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentMethodType paymentMethodType
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentMethodType : {}, {}", id, paymentMethodType);
        if (paymentMethodType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMethodTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentMethodType = paymentMethodTypeService.update(paymentMethodType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodType.getId().toString()))
            .body(paymentMethodType);
    }

    /**
     * {@code PATCH  /payment-method-types/:id} : Partial updates given fields of an existing paymentMethodType, field will ignore if it is null
     *
     * @param id the id of the paymentMethodType to save.
     * @param paymentMethodType the paymentMethodType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMethodType,
     * or with status {@code 400 (Bad Request)} if the paymentMethodType is not valid,
     * or with status {@code 404 (Not Found)} if the paymentMethodType is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethodType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentMethodType> partialUpdatePaymentMethodType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentMethodType paymentMethodType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PaymentMethodType : {}, {}", id, paymentMethodType);
        if (paymentMethodType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMethodTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMethodType> result = paymentMethodTypeService.partialUpdate(paymentMethodType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodType.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-method-types} : get all the Payment Method Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Payment Method Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentMethodType>> getAllPaymentMethodTypes(PaymentMethodTypeCriteria criteria) {
        LOG.debug("REST request to get PaymentMethodTypes by criteria: {}", criteria);

        List<PaymentMethodType> entityList = paymentMethodTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /payment-method-types/count} : count all the paymentMethodTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentMethodTypes(PaymentMethodTypeCriteria criteria) {
        LOG.debug("REST request to count PaymentMethodTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentMethodTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-method-types/:id} : get the "id" paymentMethodType.
     *
     * @param id the id of the paymentMethodType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMethodType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodType> getPaymentMethodType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentMethodType : {}", id);
        Optional<PaymentMethodType> paymentMethodType = paymentMethodTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMethodType);
    }

    /**
     * {@code DELETE  /payment-method-types/:id} : delete the "id" paymentMethodType.
     *
     * @param id the id of the paymentMethodType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethodType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentMethodType : {}", id);
        paymentMethodTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
