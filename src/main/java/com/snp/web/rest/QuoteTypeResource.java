package com.snp.web.rest;

import com.snp.domain.QuoteType;
import com.snp.repository.QuoteTypeRepository;
import com.snp.service.QuoteTypeQueryService;
import com.snp.service.QuoteTypeService;
import com.snp.service.criteria.QuoteTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.QuoteType}.
 */
@RestController
@RequestMapping("/api/quote-types")
public class QuoteTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteTypeResource.class);

    private static final String ENTITY_NAME = "quoteType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final QuoteTypeService quoteTypeService;

    private final QuoteTypeRepository quoteTypeRepository;

    private final QuoteTypeQueryService quoteTypeQueryService;

    public QuoteTypeResource(
        QuoteTypeService quoteTypeService,
        QuoteTypeRepository quoteTypeRepository,
        QuoteTypeQueryService quoteTypeQueryService
    ) {
        this.quoteTypeService = quoteTypeService;
        this.quoteTypeRepository = quoteTypeRepository;
        this.quoteTypeQueryService = quoteTypeQueryService;
    }

    /**
     * {@code POST  /quote-types} : Create a new quoteType.
     *
     * @param quoteType the quoteType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quoteType, or with status {@code 400 (Bad Request)} if the quoteType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuoteType> createQuoteType(@Valid @RequestBody QuoteType quoteType) throws URISyntaxException {
        LOG.debug("REST request to save QuoteType : {}", quoteType);
        if (quoteType.getId() != null) {
            throw new BadRequestAlertException("A new quoteType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quoteType = quoteTypeService.save(quoteType);
        return ResponseEntity.created(new URI("/api/quote-types/" + quoteType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quoteType.getId().toString()))
            .body(quoteType);
    }

    /**
     * {@code PUT  /quote-types/:id} : Updates an existing quoteType.
     *
     * @param id the id of the quoteType to save.
     * @param quoteType the quoteType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteType,
     * or with status {@code 400 (Bad Request)} if the quoteType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quoteType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuoteType> updateQuoteType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuoteType quoteType
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuoteType : {}, {}", id, quoteType);
        if (quoteType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quoteType = quoteTypeService.update(quoteType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteType.getId().toString()))
            .body(quoteType);
    }

    /**
     * {@code PATCH  /quote-types/:id} : Partial updates given fields of an existing quoteType, field will ignore if it is null
     *
     * @param id the id of the quoteType to save.
     * @param quoteType the quoteType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteType,
     * or with status {@code 400 (Bad Request)} if the quoteType is not valid,
     * or with status {@code 404 (Not Found)} if the quoteType is not found,
     * or with status {@code 500 (Internal Server Error)} if the quoteType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuoteType> partialUpdateQuoteType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuoteType quoteType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update QuoteType : {}, {}", id, quoteType);
        if (quoteType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuoteType> result = quoteTypeService.partialUpdate(quoteType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteType.getId().toString())
        );
    }

    /**
     * {@code GET  /quote-types} : get all the Quote Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Quote Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuoteType>> getAllQuoteTypes(QuoteTypeCriteria criteria) {
        LOG.debug("REST request to get QuoteTypes by criteria: {}", criteria);

        List<QuoteType> entityList = quoteTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /quote-types/count} : count all the quoteTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuoteTypes(QuoteTypeCriteria criteria) {
        LOG.debug("REST request to count QuoteTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(quoteTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quote-types/:id} : get the "id" quoteType.
     *
     * @param id the id of the quoteType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quoteType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuoteType> getQuoteType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuoteType : {}", id);
        Optional<QuoteType> quoteType = quoteTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quoteType);
    }

    /**
     * {@code DELETE  /quote-types/:id} : delete the "id" quoteType.
     *
     * @param id the id of the quoteType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuoteType : {}", id);
        quoteTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
