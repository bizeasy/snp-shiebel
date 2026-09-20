package com.snp.web.rest;

import com.snp.domain.QuoteAttachment;
import com.snp.repository.QuoteAttachmentRepository;
import com.snp.service.QuoteAttachmentQueryService;
import com.snp.service.QuoteAttachmentService;
import com.snp.service.criteria.QuoteAttachmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.QuoteAttachment}.
 */
@RestController
@RequestMapping("/api/quote-attachments")
public class QuoteAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteAttachmentResource.class);

    private static final String ENTITY_NAME = "quoteAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final QuoteAttachmentService quoteAttachmentService;

    private final QuoteAttachmentRepository quoteAttachmentRepository;

    private final QuoteAttachmentQueryService quoteAttachmentQueryService;

    public QuoteAttachmentResource(
        QuoteAttachmentService quoteAttachmentService,
        QuoteAttachmentRepository quoteAttachmentRepository,
        QuoteAttachmentQueryService quoteAttachmentQueryService
    ) {
        this.quoteAttachmentService = quoteAttachmentService;
        this.quoteAttachmentRepository = quoteAttachmentRepository;
        this.quoteAttachmentQueryService = quoteAttachmentQueryService;
    }

    /**
     * {@code POST  /quote-attachments} : Create a new quoteAttachment.
     *
     * @param quoteAttachment the quoteAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quoteAttachment, or with status {@code 400 (Bad Request)} if the quoteAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuoteAttachment> createQuoteAttachment(@Valid @RequestBody QuoteAttachment quoteAttachment)
        throws URISyntaxException {
        LOG.debug("REST request to save QuoteAttachment : {}", quoteAttachment);
        if (quoteAttachment.getId() != null) {
            throw new BadRequestAlertException("A new quoteAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quoteAttachment = quoteAttachmentService.save(quoteAttachment);
        return ResponseEntity.created(new URI("/api/quote-attachments/" + quoteAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quoteAttachment.getId().toString()))
            .body(quoteAttachment);
    }

    /**
     * {@code PUT  /quote-attachments/:id} : Updates an existing quoteAttachment.
     *
     * @param id the id of the quoteAttachment to save.
     * @param quoteAttachment the quoteAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteAttachment,
     * or with status {@code 400 (Bad Request)} if the quoteAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quoteAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuoteAttachment> updateQuoteAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuoteAttachment quoteAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuoteAttachment : {}, {}", id, quoteAttachment);
        if (quoteAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quoteAttachment = quoteAttachmentService.update(quoteAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteAttachment.getId().toString()))
            .body(quoteAttachment);
    }

    /**
     * {@code PATCH  /quote-attachments/:id} : Partial updates given fields of an existing quoteAttachment, field will ignore if it is null
     *
     * @param id the id of the quoteAttachment to save.
     * @param quoteAttachment the quoteAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteAttachment,
     * or with status {@code 400 (Bad Request)} if the quoteAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the quoteAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the quoteAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuoteAttachment> partialUpdateQuoteAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuoteAttachment quoteAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update QuoteAttachment : {}, {}", id, quoteAttachment);
        if (quoteAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuoteAttachment> result = quoteAttachmentService.partialUpdate(quoteAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /quote-attachments} : get all the Quote Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Quote Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuoteAttachment>> getAllQuoteAttachments(
        QuoteAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuoteAttachments by criteria: {}", criteria);

        Page<QuoteAttachment> page = quoteAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quote-attachments/count} : count all the quoteAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuoteAttachments(QuoteAttachmentCriteria criteria) {
        LOG.debug("REST request to count QuoteAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(quoteAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quote-attachments/:id} : get the "id" quoteAttachment.
     *
     * @param id the id of the quoteAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quoteAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuoteAttachment> getQuoteAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuoteAttachment : {}", id);
        Optional<QuoteAttachment> quoteAttachment = quoteAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quoteAttachment);
    }

    /**
     * {@code DELETE  /quote-attachments/:id} : delete the "id" quoteAttachment.
     *
     * @param id the id of the quoteAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuoteAttachment : {}", id);
        quoteAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
