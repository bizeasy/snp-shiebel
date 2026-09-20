package com.snp.web.rest;

import com.snp.domain.CustRequestType;
import com.snp.repository.CustRequestTypeRepository;
import com.snp.service.CustRequestTypeQueryService;
import com.snp.service.CustRequestTypeService;
import com.snp.service.criteria.CustRequestTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustRequestType}.
 */
@RestController
@RequestMapping("/api/cust-request-types")
public class CustRequestTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestTypeResource.class);

    private static final String ENTITY_NAME = "custRequestType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustRequestTypeService custRequestTypeService;

    private final CustRequestTypeRepository custRequestTypeRepository;

    private final CustRequestTypeQueryService custRequestTypeQueryService;

    public CustRequestTypeResource(
        CustRequestTypeService custRequestTypeService,
        CustRequestTypeRepository custRequestTypeRepository,
        CustRequestTypeQueryService custRequestTypeQueryService
    ) {
        this.custRequestTypeService = custRequestTypeService;
        this.custRequestTypeRepository = custRequestTypeRepository;
        this.custRequestTypeQueryService = custRequestTypeQueryService;
    }

    /**
     * {@code POST  /cust-request-types} : Create a new custRequestType.
     *
     * @param custRequestType the custRequestType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new custRequestType, or with status {@code 400 (Bad Request)} if the custRequestType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustRequestType> createCustRequestType(@Valid @RequestBody CustRequestType custRequestType)
        throws URISyntaxException {
        LOG.debug("REST request to save CustRequestType : {}", custRequestType);
        if (custRequestType.getId() != null) {
            throw new BadRequestAlertException("A new custRequestType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        custRequestType = custRequestTypeService.save(custRequestType);
        return ResponseEntity.created(new URI("/api/cust-request-types/" + custRequestType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, custRequestType.getId().toString()))
            .body(custRequestType);
    }

    /**
     * {@code PUT  /cust-request-types/:id} : Updates an existing custRequestType.
     *
     * @param id the id of the custRequestType to save.
     * @param custRequestType the custRequestType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestType,
     * or with status {@code 400 (Bad Request)} if the custRequestType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the custRequestType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustRequestType> updateCustRequestType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustRequestType custRequestType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustRequestType : {}, {}", id, custRequestType);
        if (custRequestType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        custRequestType = custRequestTypeService.update(custRequestType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestType.getId().toString()))
            .body(custRequestType);
    }

    /**
     * {@code PATCH  /cust-request-types/:id} : Partial updates given fields of an existing custRequestType, field will ignore if it is null
     *
     * @param id the id of the custRequestType to save.
     * @param custRequestType the custRequestType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestType,
     * or with status {@code 400 (Bad Request)} if the custRequestType is not valid,
     * or with status {@code 404 (Not Found)} if the custRequestType is not found,
     * or with status {@code 500 (Internal Server Error)} if the custRequestType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustRequestType> partialUpdateCustRequestType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustRequestType custRequestType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustRequestType : {}, {}", id, custRequestType);
        if (custRequestType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustRequestType> result = custRequestTypeService.partialUpdate(custRequestType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestType.getId().toString())
        );
    }

    /**
     * {@code GET  /cust-request-types} : get all the Cust Request Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cust Request Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustRequestType>> getAllCustRequestTypes(CustRequestTypeCriteria criteria) {
        LOG.debug("REST request to get CustRequestTypes by criteria: {}", criteria);

        List<CustRequestType> entityList = custRequestTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cust-request-types/count} : count all the custRequestTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustRequestTypes(CustRequestTypeCriteria criteria) {
        LOG.debug("REST request to count CustRequestTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(custRequestTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cust-request-types/:id} : get the "id" custRequestType.
     *
     * @param id the id of the custRequestType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the custRequestType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustRequestType> getCustRequestType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustRequestType : {}", id);
        Optional<CustRequestType> custRequestType = custRequestTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(custRequestType);
    }

    /**
     * {@code DELETE  /cust-request-types/:id} : delete the "id" custRequestType.
     *
     * @param id the id of the custRequestType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustRequestType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustRequestType : {}", id);
        custRequestTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
