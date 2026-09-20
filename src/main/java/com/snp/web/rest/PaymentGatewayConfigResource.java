package com.snp.web.rest;

import com.snp.domain.PaymentGatewayConfig;
import com.snp.repository.PaymentGatewayConfigRepository;
import com.snp.service.PaymentGatewayConfigQueryService;
import com.snp.service.PaymentGatewayConfigService;
import com.snp.service.criteria.PaymentGatewayConfigCriteria;
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
 * REST controller for managing {@link com.snp.domain.PaymentGatewayConfig}.
 */
@RestController
@RequestMapping("/api/payment-gateway-configs")
public class PaymentGatewayConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayConfigResource.class);

    private static final String ENTITY_NAME = "paymentGatewayConfig";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PaymentGatewayConfigService paymentGatewayConfigService;

    private final PaymentGatewayConfigRepository paymentGatewayConfigRepository;

    private final PaymentGatewayConfigQueryService paymentGatewayConfigQueryService;

    public PaymentGatewayConfigResource(
        PaymentGatewayConfigService paymentGatewayConfigService,
        PaymentGatewayConfigRepository paymentGatewayConfigRepository,
        PaymentGatewayConfigQueryService paymentGatewayConfigQueryService
    ) {
        this.paymentGatewayConfigService = paymentGatewayConfigService;
        this.paymentGatewayConfigRepository = paymentGatewayConfigRepository;
        this.paymentGatewayConfigQueryService = paymentGatewayConfigQueryService;
    }

    /**
     * {@code POST  /payment-gateway-configs} : Create a new paymentGatewayConfig.
     *
     * @param paymentGatewayConfig the paymentGatewayConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentGatewayConfig, or with status {@code 400 (Bad Request)} if the paymentGatewayConfig already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentGatewayConfig> createPaymentGatewayConfig(@Valid @RequestBody PaymentGatewayConfig paymentGatewayConfig)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentGatewayConfig : {}", paymentGatewayConfig);
        if (paymentGatewayConfig.getId() != null) {
            throw new BadRequestAlertException("A new paymentGatewayConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentGatewayConfig = paymentGatewayConfigService.save(paymentGatewayConfig);
        return ResponseEntity.created(new URI("/api/payment-gateway-configs/" + paymentGatewayConfig.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentGatewayConfig.getId().toString()))
            .body(paymentGatewayConfig);
    }

    /**
     * {@code PUT  /payment-gateway-configs/:id} : Updates an existing paymentGatewayConfig.
     *
     * @param id the id of the paymentGatewayConfig to save.
     * @param paymentGatewayConfig the paymentGatewayConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentGatewayConfig,
     * or with status {@code 400 (Bad Request)} if the paymentGatewayConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentGatewayConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentGatewayConfig> updatePaymentGatewayConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentGatewayConfig paymentGatewayConfig
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentGatewayConfig : {}, {}", id, paymentGatewayConfig);
        if (paymentGatewayConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentGatewayConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentGatewayConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentGatewayConfig = paymentGatewayConfigService.update(paymentGatewayConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentGatewayConfig.getId().toString()))
            .body(paymentGatewayConfig);
    }

    /**
     * {@code PATCH  /payment-gateway-configs/:id} : Partial updates given fields of an existing paymentGatewayConfig, field will ignore if it is null
     *
     * @param id the id of the paymentGatewayConfig to save.
     * @param paymentGatewayConfig the paymentGatewayConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentGatewayConfig,
     * or with status {@code 400 (Bad Request)} if the paymentGatewayConfig is not valid,
     * or with status {@code 404 (Not Found)} if the paymentGatewayConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentGatewayConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentGatewayConfig> partialUpdatePaymentGatewayConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentGatewayConfig paymentGatewayConfig
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PaymentGatewayConfig : {}, {}", id, paymentGatewayConfig);
        if (paymentGatewayConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentGatewayConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentGatewayConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentGatewayConfig> result = paymentGatewayConfigService.partialUpdate(paymentGatewayConfig);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentGatewayConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-gateway-configs} : get all the Payment Gateway Configs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Payment Gateway Configs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentGatewayConfig>> getAllPaymentGatewayConfigs(PaymentGatewayConfigCriteria criteria) {
        LOG.debug("REST request to get PaymentGatewayConfigs by criteria: {}", criteria);

        List<PaymentGatewayConfig> entityList = paymentGatewayConfigQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /payment-gateway-configs/count} : count all the paymentGatewayConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentGatewayConfigs(PaymentGatewayConfigCriteria criteria) {
        LOG.debug("REST request to count PaymentGatewayConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentGatewayConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-gateway-configs/:id} : get the "id" paymentGatewayConfig.
     *
     * @param id the id of the paymentGatewayConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentGatewayConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentGatewayConfig> getPaymentGatewayConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentGatewayConfig : {}", id);
        Optional<PaymentGatewayConfig> paymentGatewayConfig = paymentGatewayConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentGatewayConfig);
    }

    /**
     * {@code DELETE  /payment-gateway-configs/:id} : delete the "id" paymentGatewayConfig.
     *
     * @param id the id of the paymentGatewayConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentGatewayConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentGatewayConfig : {}", id);
        paymentGatewayConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
