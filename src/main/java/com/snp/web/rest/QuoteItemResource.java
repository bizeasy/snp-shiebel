package com.snp.web.rest;

import com.snp.domain.QuoteItem;
import com.snp.repository.QuoteItemRepository;
import com.snp.service.QuoteItemQueryService;
import com.snp.service.QuoteItemService;
import com.snp.service.criteria.QuoteItemCriteria;
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
 * REST controller for managing {@link com.snp.domain.QuoteItem}.
 */
@RestController
@RequestMapping("/api/quote-items")
public class QuoteItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteItemResource.class);

    private static final String ENTITY_NAME = "quoteItem";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final QuoteItemService quoteItemService;

    private final QuoteItemRepository quoteItemRepository;

    private final QuoteItemQueryService quoteItemQueryService;

    public QuoteItemResource(
        QuoteItemService quoteItemService,
        QuoteItemRepository quoteItemRepository,
        QuoteItemQueryService quoteItemQueryService
    ) {
        this.quoteItemService = quoteItemService;
        this.quoteItemRepository = quoteItemRepository;
        this.quoteItemQueryService = quoteItemQueryService;
    }

    /**
     * {@code POST  /quote-items} : Create a new quoteItem.
     *
     * @param quoteItem the quoteItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quoteItem, or with status {@code 400 (Bad Request)} if the quoteItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuoteItem> createQuoteItem(@Valid @RequestBody QuoteItem quoteItem) throws URISyntaxException {
        LOG.debug("REST request to save QuoteItem : {}", quoteItem);
        if (quoteItem.getId() != null) {
            throw new BadRequestAlertException("A new quoteItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quoteItem = quoteItemService.save(quoteItem);
        return ResponseEntity.created(new URI("/api/quote-items/" + quoteItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quoteItem.getId().toString()))
            .body(quoteItem);
    }

    /**
     * {@code PUT  /quote-items/:id} : Updates an existing quoteItem.
     *
     * @param id the id of the quoteItem to save.
     * @param quoteItem the quoteItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteItem,
     * or with status {@code 400 (Bad Request)} if the quoteItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quoteItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuoteItem> updateQuoteItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuoteItem quoteItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuoteItem : {}, {}", id, quoteItem);
        if (quoteItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quoteItem = quoteItemService.update(quoteItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteItem.getId().toString()))
            .body(quoteItem);
    }

    /**
     * {@code PATCH  /quote-items/:id} : Partial updates given fields of an existing quoteItem, field will ignore if it is null
     *
     * @param id the id of the quoteItem to save.
     * @param quoteItem the quoteItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteItem,
     * or with status {@code 400 (Bad Request)} if the quoteItem is not valid,
     * or with status {@code 404 (Not Found)} if the quoteItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the quoteItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuoteItem> partialUpdateQuoteItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuoteItem quoteItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update QuoteItem : {}, {}", id, quoteItem);
        if (quoteItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuoteItem> result = quoteItemService.partialUpdate(quoteItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteItem.getId().toString())
        );
    }

    /**
     * {@code GET  /quote-items} : get all the Quote Items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Quote Items in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuoteItem>> getAllQuoteItems(
        QuoteItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuoteItems by criteria: {}", criteria);

        Page<QuoteItem> page = quoteItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quote-items/count} : count all the quoteItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuoteItems(QuoteItemCriteria criteria) {
        LOG.debug("REST request to count QuoteItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(quoteItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quote-items/:id} : get the "id" quoteItem.
     *
     * @param id the id of the quoteItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quoteItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuoteItem> getQuoteItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuoteItem : {}", id);
        Optional<QuoteItem> quoteItem = quoteItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quoteItem);
    }

    /**
     * {@code DELETE  /quote-items/:id} : delete the "id" quoteItem.
     *
     * @param id the id of the quoteItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuoteItem : {}", id);
        quoteItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
