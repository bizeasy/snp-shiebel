package com.snp.web.rest;

import com.snp.domain.CarrierShipmentMethod;
import com.snp.repository.CarrierShipmentMethodRepository;
import com.snp.service.CarrierShipmentMethodQueryService;
import com.snp.service.CarrierShipmentMethodService;
import com.snp.service.criteria.CarrierShipmentMethodCriteria;
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
 * REST controller for managing {@link com.snp.domain.CarrierShipmentMethod}.
 */
@RestController
@RequestMapping("/api/carrier-shipment-methods")
public class CarrierShipmentMethodResource {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierShipmentMethodResource.class);

    private static final String ENTITY_NAME = "carrierShipmentMethod";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CarrierShipmentMethodService carrierShipmentMethodService;

    private final CarrierShipmentMethodRepository carrierShipmentMethodRepository;

    private final CarrierShipmentMethodQueryService carrierShipmentMethodQueryService;

    public CarrierShipmentMethodResource(
        CarrierShipmentMethodService carrierShipmentMethodService,
        CarrierShipmentMethodRepository carrierShipmentMethodRepository,
        CarrierShipmentMethodQueryService carrierShipmentMethodQueryService
    ) {
        this.carrierShipmentMethodService = carrierShipmentMethodService;
        this.carrierShipmentMethodRepository = carrierShipmentMethodRepository;
        this.carrierShipmentMethodQueryService = carrierShipmentMethodQueryService;
    }

    /**
     * {@code POST  /carrier-shipment-methods} : Create a new carrierShipmentMethod.
     *
     * @param carrierShipmentMethod the carrierShipmentMethod to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carrierShipmentMethod, or with status {@code 400 (Bad Request)} if the carrierShipmentMethod already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarrierShipmentMethod> createCarrierShipmentMethod(
        @Valid @RequestBody CarrierShipmentMethod carrierShipmentMethod
    ) throws URISyntaxException {
        LOG.debug("REST request to save CarrierShipmentMethod : {}", carrierShipmentMethod);
        if (carrierShipmentMethod.getId() != null) {
            throw new BadRequestAlertException("A new carrierShipmentMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carrierShipmentMethod = carrierShipmentMethodService.save(carrierShipmentMethod);
        return ResponseEntity.created(new URI("/api/carrier-shipment-methods/" + carrierShipmentMethod.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carrierShipmentMethod.getId().toString()))
            .body(carrierShipmentMethod);
    }

    /**
     * {@code PUT  /carrier-shipment-methods/:id} : Updates an existing carrierShipmentMethod.
     *
     * @param id the id of the carrierShipmentMethod to save.
     * @param carrierShipmentMethod the carrierShipmentMethod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carrierShipmentMethod,
     * or with status {@code 400 (Bad Request)} if the carrierShipmentMethod is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carrierShipmentMethod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarrierShipmentMethod> updateCarrierShipmentMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarrierShipmentMethod carrierShipmentMethod
    ) throws URISyntaxException {
        LOG.debug("REST request to update CarrierShipmentMethod : {}, {}", id, carrierShipmentMethod);
        if (carrierShipmentMethod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carrierShipmentMethod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carrierShipmentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carrierShipmentMethod = carrierShipmentMethodService.update(carrierShipmentMethod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carrierShipmentMethod.getId().toString()))
            .body(carrierShipmentMethod);
    }

    /**
     * {@code PATCH  /carrier-shipment-methods/:id} : Partial updates given fields of an existing carrierShipmentMethod, field will ignore if it is null
     *
     * @param id the id of the carrierShipmentMethod to save.
     * @param carrierShipmentMethod the carrierShipmentMethod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carrierShipmentMethod,
     * or with status {@code 400 (Bad Request)} if the carrierShipmentMethod is not valid,
     * or with status {@code 404 (Not Found)} if the carrierShipmentMethod is not found,
     * or with status {@code 500 (Internal Server Error)} if the carrierShipmentMethod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarrierShipmentMethod> partialUpdateCarrierShipmentMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarrierShipmentMethod carrierShipmentMethod
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CarrierShipmentMethod : {}, {}", id, carrierShipmentMethod);
        if (carrierShipmentMethod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carrierShipmentMethod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carrierShipmentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarrierShipmentMethod> result = carrierShipmentMethodService.partialUpdate(carrierShipmentMethod);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carrierShipmentMethod.getId().toString())
        );
    }

    /**
     * {@code GET  /carrier-shipment-methods} : get all the Carrier Shipment Methods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Carrier Shipment Methods in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarrierShipmentMethod>> getAllCarrierShipmentMethods(CarrierShipmentMethodCriteria criteria) {
        LOG.debug("REST request to get CarrierShipmentMethods by criteria: {}", criteria);

        List<CarrierShipmentMethod> entityList = carrierShipmentMethodQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /carrier-shipment-methods/count} : count all the carrierShipmentMethods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCarrierShipmentMethods(CarrierShipmentMethodCriteria criteria) {
        LOG.debug("REST request to count CarrierShipmentMethods by criteria: {}", criteria);
        return ResponseEntity.ok().body(carrierShipmentMethodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /carrier-shipment-methods/:id} : get the "id" carrierShipmentMethod.
     *
     * @param id the id of the carrierShipmentMethod to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carrierShipmentMethod, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarrierShipmentMethod> getCarrierShipmentMethod(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CarrierShipmentMethod : {}", id);
        Optional<CarrierShipmentMethod> carrierShipmentMethod = carrierShipmentMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carrierShipmentMethod);
    }

    /**
     * {@code DELETE  /carrier-shipment-methods/:id} : delete the "id" carrierShipmentMethod.
     *
     * @param id the id of the carrierShipmentMethod to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrierShipmentMethod(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CarrierShipmentMethod : {}", id);
        carrierShipmentMethodService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
