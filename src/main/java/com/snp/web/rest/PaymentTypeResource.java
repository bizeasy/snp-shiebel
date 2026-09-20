package com.snp.web.rest;

import com.snp.domain.PaymentType;
import com.snp.repository.PaymentTypeRepository;
import com.snp.service.PaymentTypeQueryService;
import com.snp.service.PaymentTypeService;
import com.snp.service.criteria.PaymentTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.PaymentType}.
 */
@RestController
@RequestMapping("/api/payment-types")
public class PaymentTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentTypeResource.class);

    private static final String ENTITY_NAME = "paymentType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PaymentTypeService paymentTypeService;

    private final PaymentTypeRepository paymentTypeRepository;

    private final PaymentTypeQueryService paymentTypeQueryService;

    public PaymentTypeResource(
        PaymentTypeService paymentTypeService,
        PaymentTypeRepository paymentTypeRepository,
        PaymentTypeQueryService paymentTypeQueryService
    ) {
        this.paymentTypeService = paymentTypeService;
        this.paymentTypeRepository = paymentTypeRepository;
        this.paymentTypeQueryService = paymentTypeQueryService;
    }

    /**
     * {@code POST  /payment-types} : Create a new paymentType.
     *
     * @param paymentType the paymentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentType, or with status {@code 400 (Bad Request)} if the paymentType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentType> createPaymentType(@Valid @RequestBody PaymentType paymentType) throws URISyntaxException {
        LOG.debug("REST request to save PaymentType : {}", paymentType);
        if (paymentType.getId() != null) {
            throw new BadRequestAlertException("A new paymentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentType = paymentTypeService.save(paymentType);
        return ResponseEntity.created(new URI("/api/payment-types/" + paymentType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentType.getId().toString()))
            .body(paymentType);
    }

    /**
     * {@code PUT  /payment-types/:id} : Updates an existing paymentType.
     *
     * @param id the id of the paymentType to save.
     * @param paymentType the paymentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentType,
     * or with status {@code 400 (Bad Request)} if the paymentType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentType> updatePaymentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentType paymentType
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentType : {}, {}", id, paymentType);
        if (paymentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentType = paymentTypeService.update(paymentType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentType.getId().toString()))
            .body(paymentType);
    }

    /**
     * {@code PATCH  /payment-types/:id} : Partial updates given fields of an existing paymentType, field will ignore if it is null
     *
     * @param id the id of the paymentType to save.
     * @param paymentType the paymentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentType,
     * or with status {@code 400 (Bad Request)} if the paymentType is not valid,
     * or with status {@code 404 (Not Found)} if the paymentType is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentType> partialUpdatePaymentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentType paymentType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PaymentType : {}, {}", id, paymentType);
        if (paymentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentType> result = paymentTypeService.partialUpdate(paymentType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentType.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-types} : get all the Payment Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Payment Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentType>> getAllPaymentTypes(PaymentTypeCriteria criteria) {
        LOG.debug("REST request to get PaymentTypes by criteria: {}", criteria);

        List<PaymentType> entityList = paymentTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /payment-types/count} : count all the paymentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentTypes(PaymentTypeCriteria criteria) {
        LOG.debug("REST request to count PaymentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-types/:id} : get the "id" paymentType.
     *
     * @param id the id of the paymentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentType> getPaymentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentType : {}", id);
        Optional<PaymentType> paymentType = paymentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentType);
    }

    /**
     * {@code DELETE  /payment-types/:id} : delete the "id" paymentType.
     *
     * @param id the id of the paymentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentType : {}", id);
        paymentTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
