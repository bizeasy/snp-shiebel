package com.snp.web.rest;

import com.snp.domain.CashBookAttachment;
import com.snp.repository.CashBookAttachmentRepository;
import com.snp.service.CashBookAttachmentQueryService;
import com.snp.service.CashBookAttachmentService;
import com.snp.service.criteria.CashBookAttachmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.CashBookAttachment}.
 */
@RestController
@RequestMapping("/api/cash-book-attachments")
public class CashBookAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CashBookAttachmentResource.class);

    private static final String ENTITY_NAME = "cashBookAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CashBookAttachmentService cashBookAttachmentService;

    private final CashBookAttachmentRepository cashBookAttachmentRepository;

    private final CashBookAttachmentQueryService cashBookAttachmentQueryService;

    public CashBookAttachmentResource(
        CashBookAttachmentService cashBookAttachmentService,
        CashBookAttachmentRepository cashBookAttachmentRepository,
        CashBookAttachmentQueryService cashBookAttachmentQueryService
    ) {
        this.cashBookAttachmentService = cashBookAttachmentService;
        this.cashBookAttachmentRepository = cashBookAttachmentRepository;
        this.cashBookAttachmentQueryService = cashBookAttachmentQueryService;
    }

    /**
     * {@code POST  /cash-book-attachments} : Create a new cashBookAttachment.
     *
     * @param cashBookAttachment the cashBookAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashBookAttachment, or with status {@code 400 (Bad Request)} if the cashBookAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CashBookAttachment> createCashBookAttachment(@Valid @RequestBody CashBookAttachment cashBookAttachment)
        throws URISyntaxException {
        LOG.debug("REST request to save CashBookAttachment : {}", cashBookAttachment);
        if (cashBookAttachment.getId() != null) {
            throw new BadRequestAlertException("A new cashBookAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cashBookAttachment = cashBookAttachmentService.save(cashBookAttachment);
        return ResponseEntity.created(new URI("/api/cash-book-attachments/" + cashBookAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cashBookAttachment.getId().toString()))
            .body(cashBookAttachment);
    }

    /**
     * {@code PUT  /cash-book-attachments/:id} : Updates an existing cashBookAttachment.
     *
     * @param id the id of the cashBookAttachment to save.
     * @param cashBookAttachment the cashBookAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBookAttachment,
     * or with status {@code 400 (Bad Request)} if the cashBookAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashBookAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CashBookAttachment> updateCashBookAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CashBookAttachment cashBookAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update CashBookAttachment : {}, {}", id, cashBookAttachment);
        if (cashBookAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashBookAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashBookAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cashBookAttachment = cashBookAttachmentService.update(cashBookAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cashBookAttachment.getId().toString()))
            .body(cashBookAttachment);
    }

    /**
     * {@code PATCH  /cash-book-attachments/:id} : Partial updates given fields of an existing cashBookAttachment, field will ignore if it is null
     *
     * @param id the id of the cashBookAttachment to save.
     * @param cashBookAttachment the cashBookAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBookAttachment,
     * or with status {@code 400 (Bad Request)} if the cashBookAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the cashBookAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the cashBookAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CashBookAttachment> partialUpdateCashBookAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CashBookAttachment cashBookAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CashBookAttachment : {}, {}", id, cashBookAttachment);
        if (cashBookAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashBookAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashBookAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CashBookAttachment> result = cashBookAttachmentService.partialUpdate(cashBookAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cashBookAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /cash-book-attachments} : get all the Cash Book Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cash Book Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CashBookAttachment>> getAllCashBookAttachments(
        CashBookAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CashBookAttachments by criteria: {}", criteria);

        Page<CashBookAttachment> page = cashBookAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cash-book-attachments/count} : count all the cashBookAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCashBookAttachments(CashBookAttachmentCriteria criteria) {
        LOG.debug("REST request to count CashBookAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashBookAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-book-attachments/:id} : get the "id" cashBookAttachment.
     *
     * @param id the id of the cashBookAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashBookAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CashBookAttachment> getCashBookAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CashBookAttachment : {}", id);
        Optional<CashBookAttachment> cashBookAttachment = cashBookAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashBookAttachment);
    }

    /**
     * {@code DELETE  /cash-book-attachments/:id} : delete the "id" cashBookAttachment.
     *
     * @param id the id of the cashBookAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashBookAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CashBookAttachment : {}", id);
        cashBookAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
