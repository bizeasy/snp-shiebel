package com.snp.web.rest;

import com.snp.domain.CustRequestItemAttachment;
import com.snp.repository.CustRequestItemAttachmentRepository;
import com.snp.service.CustRequestItemAttachmentQueryService;
import com.snp.service.CustRequestItemAttachmentService;
import com.snp.service.criteria.CustRequestItemAttachmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustRequestItemAttachment}.
 */
@RestController
@RequestMapping("/api/cust-request-item-attachments")
public class CustRequestItemAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestItemAttachmentResource.class);

    private static final String ENTITY_NAME = "custRequestItemAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustRequestItemAttachmentService custRequestItemAttachmentService;

    private final CustRequestItemAttachmentRepository custRequestItemAttachmentRepository;

    private final CustRequestItemAttachmentQueryService custRequestItemAttachmentQueryService;

    public CustRequestItemAttachmentResource(
        CustRequestItemAttachmentService custRequestItemAttachmentService,
        CustRequestItemAttachmentRepository custRequestItemAttachmentRepository,
        CustRequestItemAttachmentQueryService custRequestItemAttachmentQueryService
    ) {
        this.custRequestItemAttachmentService = custRequestItemAttachmentService;
        this.custRequestItemAttachmentRepository = custRequestItemAttachmentRepository;
        this.custRequestItemAttachmentQueryService = custRequestItemAttachmentQueryService;
    }

    /**
     * {@code POST  /cust-request-item-attachments} : Create a new custRequestItemAttachment.
     *
     * @param custRequestItemAttachment the custRequestItemAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new custRequestItemAttachment, or with status {@code 400 (Bad Request)} if the custRequestItemAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustRequestItemAttachment> createCustRequestItemAttachment(
        @Valid @RequestBody CustRequestItemAttachment custRequestItemAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to save CustRequestItemAttachment : {}", custRequestItemAttachment);
        if (custRequestItemAttachment.getId() != null) {
            throw new BadRequestAlertException("A new custRequestItemAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        custRequestItemAttachment = custRequestItemAttachmentService.save(custRequestItemAttachment);
        return ResponseEntity.created(new URI("/api/cust-request-item-attachments/" + custRequestItemAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, custRequestItemAttachment.getId().toString()))
            .body(custRequestItemAttachment);
    }

    /**
     * {@code PUT  /cust-request-item-attachments/:id} : Updates an existing custRequestItemAttachment.
     *
     * @param id the id of the custRequestItemAttachment to save.
     * @param custRequestItemAttachment the custRequestItemAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestItemAttachment,
     * or with status {@code 400 (Bad Request)} if the custRequestItemAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the custRequestItemAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustRequestItemAttachment> updateCustRequestItemAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustRequestItemAttachment custRequestItemAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustRequestItemAttachment : {}, {}", id, custRequestItemAttachment);
        if (custRequestItemAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestItemAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestItemAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        custRequestItemAttachment = custRequestItemAttachmentService.update(custRequestItemAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestItemAttachment.getId().toString()))
            .body(custRequestItemAttachment);
    }

    /**
     * {@code PATCH  /cust-request-item-attachments/:id} : Partial updates given fields of an existing custRequestItemAttachment, field will ignore if it is null
     *
     * @param id the id of the custRequestItemAttachment to save.
     * @param custRequestItemAttachment the custRequestItemAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestItemAttachment,
     * or with status {@code 400 (Bad Request)} if the custRequestItemAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the custRequestItemAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the custRequestItemAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustRequestItemAttachment> partialUpdateCustRequestItemAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustRequestItemAttachment custRequestItemAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustRequestItemAttachment : {}, {}", id, custRequestItemAttachment);
        if (custRequestItemAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestItemAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestItemAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustRequestItemAttachment> result = custRequestItemAttachmentService.partialUpdate(custRequestItemAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestItemAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /cust-request-item-attachments} : get all the Cust Request Item Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cust Request Item Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustRequestItemAttachment>> getAllCustRequestItemAttachments(
        CustRequestItemAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustRequestItemAttachments by criteria: {}", criteria);

        Page<CustRequestItemAttachment> page = custRequestItemAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cust-request-item-attachments/count} : count all the custRequestItemAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustRequestItemAttachments(CustRequestItemAttachmentCriteria criteria) {
        LOG.debug("REST request to count CustRequestItemAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(custRequestItemAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cust-request-item-attachments/:id} : get the "id" custRequestItemAttachment.
     *
     * @param id the id of the custRequestItemAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the custRequestItemAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustRequestItemAttachment> getCustRequestItemAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustRequestItemAttachment : {}", id);
        Optional<CustRequestItemAttachment> custRequestItemAttachment = custRequestItemAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(custRequestItemAttachment);
    }

    /**
     * {@code DELETE  /cust-request-item-attachments/:id} : delete the "id" custRequestItemAttachment.
     *
     * @param id the id of the custRequestItemAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustRequestItemAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustRequestItemAttachment : {}", id);
        custRequestItemAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
