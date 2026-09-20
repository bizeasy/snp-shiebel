package com.snp.web.rest;

import com.snp.domain.QuoteAdjustment;
import com.snp.repository.QuoteAdjustmentRepository;
import com.snp.service.QuoteAdjustmentQueryService;
import com.snp.service.QuoteAdjustmentService;
import com.snp.service.criteria.QuoteAdjustmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.QuoteAdjustment}.
 */
@RestController
@RequestMapping("/api/quote-adjustments")
public class QuoteAdjustmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteAdjustmentResource.class);

    private static final String ENTITY_NAME = "quoteAdjustment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final QuoteAdjustmentService quoteAdjustmentService;

    private final QuoteAdjustmentRepository quoteAdjustmentRepository;

    private final QuoteAdjustmentQueryService quoteAdjustmentQueryService;

    public QuoteAdjustmentResource(
        QuoteAdjustmentService quoteAdjustmentService,
        QuoteAdjustmentRepository quoteAdjustmentRepository,
        QuoteAdjustmentQueryService quoteAdjustmentQueryService
    ) {
        this.quoteAdjustmentService = quoteAdjustmentService;
        this.quoteAdjustmentRepository = quoteAdjustmentRepository;
        this.quoteAdjustmentQueryService = quoteAdjustmentQueryService;
    }

    /**
     * {@code POST  /quote-adjustments} : Create a new quoteAdjustment.
     *
     * @param quoteAdjustment the quoteAdjustment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quoteAdjustment, or with status {@code 400 (Bad Request)} if the quoteAdjustment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuoteAdjustment> createQuoteAdjustment(@Valid @RequestBody QuoteAdjustment quoteAdjustment)
        throws URISyntaxException {
        LOG.debug("REST request to save QuoteAdjustment : {}", quoteAdjustment);
        if (quoteAdjustment.getId() != null) {
            throw new BadRequestAlertException("A new quoteAdjustment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quoteAdjustment = quoteAdjustmentService.save(quoteAdjustment);
        return ResponseEntity.created(new URI("/api/quote-adjustments/" + quoteAdjustment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quoteAdjustment.getId().toString()))
            .body(quoteAdjustment);
    }

    /**
     * {@code PUT  /quote-adjustments/:id} : Updates an existing quoteAdjustment.
     *
     * @param id the id of the quoteAdjustment to save.
     * @param quoteAdjustment the quoteAdjustment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteAdjustment,
     * or with status {@code 400 (Bad Request)} if the quoteAdjustment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quoteAdjustment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuoteAdjustment> updateQuoteAdjustment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuoteAdjustment quoteAdjustment
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuoteAdjustment : {}, {}", id, quoteAdjustment);
        if (quoteAdjustment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteAdjustment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteAdjustmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quoteAdjustment = quoteAdjustmentService.update(quoteAdjustment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteAdjustment.getId().toString()))
            .body(quoteAdjustment);
    }

    /**
     * {@code PATCH  /quote-adjustments/:id} : Partial updates given fields of an existing quoteAdjustment, field will ignore if it is null
     *
     * @param id the id of the quoteAdjustment to save.
     * @param quoteAdjustment the quoteAdjustment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteAdjustment,
     * or with status {@code 400 (Bad Request)} if the quoteAdjustment is not valid,
     * or with status {@code 404 (Not Found)} if the quoteAdjustment is not found,
     * or with status {@code 500 (Internal Server Error)} if the quoteAdjustment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuoteAdjustment> partialUpdateQuoteAdjustment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuoteAdjustment quoteAdjustment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update QuoteAdjustment : {}, {}", id, quoteAdjustment);
        if (quoteAdjustment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteAdjustment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteAdjustmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuoteAdjustment> result = quoteAdjustmentService.partialUpdate(quoteAdjustment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteAdjustment.getId().toString())
        );
    }

    /**
     * {@code GET  /quote-adjustments} : get all the Quote Adjustments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Quote Adjustments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuoteAdjustment>> getAllQuoteAdjustments(
        QuoteAdjustmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuoteAdjustments by criteria: {}", criteria);

        Page<QuoteAdjustment> page = quoteAdjustmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quote-adjustments/count} : count all the quoteAdjustments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuoteAdjustments(QuoteAdjustmentCriteria criteria) {
        LOG.debug("REST request to count QuoteAdjustments by criteria: {}", criteria);
        return ResponseEntity.ok().body(quoteAdjustmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quote-adjustments/:id} : get the "id" quoteAdjustment.
     *
     * @param id the id of the quoteAdjustment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quoteAdjustment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuoteAdjustment> getQuoteAdjustment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuoteAdjustment : {}", id);
        Optional<QuoteAdjustment> quoteAdjustment = quoteAdjustmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quoteAdjustment);
    }

    /**
     * {@code DELETE  /quote-adjustments/:id} : delete the "id" quoteAdjustment.
     *
     * @param id the id of the quoteAdjustment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteAdjustment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuoteAdjustment : {}", id);
        quoteAdjustmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
