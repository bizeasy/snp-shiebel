package com.snp.web.rest;

import com.snp.domain.CustRequest;
import com.snp.repository.CustRequestRepository;
import com.snp.service.CustRequestQueryService;
import com.snp.service.CustRequestService;
import com.snp.service.criteria.CustRequestCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustRequest}.
 */
@RestController
@RequestMapping("/api/cust-requests")
public class CustRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestResource.class);

    private static final String ENTITY_NAME = "custRequest";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustRequestService custRequestService;

    private final CustRequestRepository custRequestRepository;

    private final CustRequestQueryService custRequestQueryService;

    public CustRequestResource(
        CustRequestService custRequestService,
        CustRequestRepository custRequestRepository,
        CustRequestQueryService custRequestQueryService
    ) {
        this.custRequestService = custRequestService;
        this.custRequestRepository = custRequestRepository;
        this.custRequestQueryService = custRequestQueryService;
    }

    /**
     * {@code POST  /cust-requests} : Create a new custRequest.
     *
     * @param custRequest the custRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new custRequest, or with status {@code 400 (Bad Request)} if the custRequest already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustRequest> createCustRequest(@Valid @RequestBody CustRequest custRequest) throws URISyntaxException {
        LOG.debug("REST request to save CustRequest : {}", custRequest);
        if (custRequest.getId() != null) {
            throw new BadRequestAlertException("A new custRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        custRequest = custRequestService.save(custRequest);
        return ResponseEntity.created(new URI("/api/cust-requests/" + custRequest.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, custRequest.getId().toString()))
            .body(custRequest);
    }

    /**
     * {@code PUT  /cust-requests/:id} : Updates an existing custRequest.
     *
     * @param id the id of the custRequest to save.
     * @param custRequest the custRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequest,
     * or with status {@code 400 (Bad Request)} if the custRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the custRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustRequest> updateCustRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustRequest custRequest
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustRequest : {}, {}", id, custRequest);
        if (custRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        custRequest = custRequestService.update(custRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequest.getId().toString()))
            .body(custRequest);
    }

    /**
     * {@code PATCH  /cust-requests/:id} : Partial updates given fields of an existing custRequest, field will ignore if it is null
     *
     * @param id the id of the custRequest to save.
     * @param custRequest the custRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequest,
     * or with status {@code 400 (Bad Request)} if the custRequest is not valid,
     * or with status {@code 404 (Not Found)} if the custRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the custRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustRequest> partialUpdateCustRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustRequest custRequest
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustRequest : {}, {}", id, custRequest);
        if (custRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustRequest> result = custRequestService.partialUpdate(custRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /cust-requests} : get all the Cust Requests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cust Requests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustRequest>> getAllCustRequests(
        CustRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustRequests by criteria: {}", criteria);

        Page<CustRequest> page = custRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cust-requests/count} : count all the custRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustRequests(CustRequestCriteria criteria) {
        LOG.debug("REST request to count CustRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(custRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cust-requests/:id} : get the "id" custRequest.
     *
     * @param id the id of the custRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the custRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustRequest> getCustRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustRequest : {}", id);
        Optional<CustRequest> custRequest = custRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(custRequest);
    }

    /**
     * {@code DELETE  /cust-requests/:id} : delete the "id" custRequest.
     *
     * @param id the id of the custRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustRequest : {}", id);
        custRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
