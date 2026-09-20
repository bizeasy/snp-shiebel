package com.snp.web.rest;

import com.snp.domain.CustRequestAttachment;
import com.snp.repository.CustRequestAttachmentRepository;
import com.snp.service.CustRequestAttachmentQueryService;
import com.snp.service.CustRequestAttachmentService;
import com.snp.service.criteria.CustRequestAttachmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustRequestAttachment}.
 */
@RestController
@RequestMapping("/api/cust-request-attachments")
public class CustRequestAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestAttachmentResource.class);

    private static final String ENTITY_NAME = "custRequestAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustRequestAttachmentService custRequestAttachmentService;

    private final CustRequestAttachmentRepository custRequestAttachmentRepository;

    private final CustRequestAttachmentQueryService custRequestAttachmentQueryService;

    public CustRequestAttachmentResource(
        CustRequestAttachmentService custRequestAttachmentService,
        CustRequestAttachmentRepository custRequestAttachmentRepository,
        CustRequestAttachmentQueryService custRequestAttachmentQueryService
    ) {
        this.custRequestAttachmentService = custRequestAttachmentService;
        this.custRequestAttachmentRepository = custRequestAttachmentRepository;
        this.custRequestAttachmentQueryService = custRequestAttachmentQueryService;
    }

    /**
     * {@code POST  /cust-request-attachments} : Create a new custRequestAttachment.
     *
     * @param custRequestAttachment the custRequestAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new custRequestAttachment, or with status {@code 400 (Bad Request)} if the custRequestAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustRequestAttachment> createCustRequestAttachment(
        @Valid @RequestBody CustRequestAttachment custRequestAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to save CustRequestAttachment : {}", custRequestAttachment);
        if (custRequestAttachment.getId() != null) {
            throw new BadRequestAlertException("A new custRequestAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        custRequestAttachment = custRequestAttachmentService.save(custRequestAttachment);
        return ResponseEntity.created(new URI("/api/cust-request-attachments/" + custRequestAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, custRequestAttachment.getId().toString()))
            .body(custRequestAttachment);
    }

    /**
     * {@code PUT  /cust-request-attachments/:id} : Updates an existing custRequestAttachment.
     *
     * @param id the id of the custRequestAttachment to save.
     * @param custRequestAttachment the custRequestAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestAttachment,
     * or with status {@code 400 (Bad Request)} if the custRequestAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the custRequestAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustRequestAttachment> updateCustRequestAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustRequestAttachment custRequestAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustRequestAttachment : {}, {}", id, custRequestAttachment);
        if (custRequestAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        custRequestAttachment = custRequestAttachmentService.update(custRequestAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestAttachment.getId().toString()))
            .body(custRequestAttachment);
    }

    /**
     * {@code PATCH  /cust-request-attachments/:id} : Partial updates given fields of an existing custRequestAttachment, field will ignore if it is null
     *
     * @param id the id of the custRequestAttachment to save.
     * @param custRequestAttachment the custRequestAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestAttachment,
     * or with status {@code 400 (Bad Request)} if the custRequestAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the custRequestAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the custRequestAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustRequestAttachment> partialUpdateCustRequestAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustRequestAttachment custRequestAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustRequestAttachment : {}, {}", id, custRequestAttachment);
        if (custRequestAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustRequestAttachment> result = custRequestAttachmentService.partialUpdate(custRequestAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /cust-request-attachments} : get all the Cust Request Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cust Request Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustRequestAttachment>> getAllCustRequestAttachments(
        CustRequestAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustRequestAttachments by criteria: {}", criteria);

        Page<CustRequestAttachment> page = custRequestAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cust-request-attachments/count} : count all the custRequestAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustRequestAttachments(CustRequestAttachmentCriteria criteria) {
        LOG.debug("REST request to count CustRequestAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(custRequestAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cust-request-attachments/:id} : get the "id" custRequestAttachment.
     *
     * @param id the id of the custRequestAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the custRequestAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustRequestAttachment> getCustRequestAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustRequestAttachment : {}", id);
        Optional<CustRequestAttachment> custRequestAttachment = custRequestAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(custRequestAttachment);
    }

    /**
     * {@code DELETE  /cust-request-attachments/:id} : delete the "id" custRequestAttachment.
     *
     * @param id the id of the custRequestAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustRequestAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustRequestAttachment : {}", id);
        custRequestAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
