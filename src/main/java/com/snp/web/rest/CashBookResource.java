package com.snp.web.rest;

import com.snp.domain.CashBook;
import com.snp.repository.CashBookRepository;
import com.snp.service.CashBookQueryService;
import com.snp.service.CashBookService;
import com.snp.service.criteria.CashBookCriteria;
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
 * REST controller for managing {@link com.snp.domain.CashBook}.
 */
@RestController
@RequestMapping("/api/cash-books")
public class CashBookResource {

    private static final Logger LOG = LoggerFactory.getLogger(CashBookResource.class);

    private static final String ENTITY_NAME = "cashBook";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CashBookService cashBookService;

    private final CashBookRepository cashBookRepository;

    private final CashBookQueryService cashBookQueryService;

    public CashBookResource(
        CashBookService cashBookService,
        CashBookRepository cashBookRepository,
        CashBookQueryService cashBookQueryService
    ) {
        this.cashBookService = cashBookService;
        this.cashBookRepository = cashBookRepository;
        this.cashBookQueryService = cashBookQueryService;
    }

    /**
     * {@code POST  /cash-books} : Create a new cashBook.
     *
     * @param cashBook the cashBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashBook, or with status {@code 400 (Bad Request)} if the cashBook already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CashBook> createCashBook(@Valid @RequestBody CashBook cashBook) throws URISyntaxException {
        LOG.debug("REST request to save CashBook : {}", cashBook);
        if (cashBook.getId() != null) {
            throw new BadRequestAlertException("A new cashBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cashBook = cashBookService.save(cashBook);
        return ResponseEntity.created(new URI("/api/cash-books/" + cashBook.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cashBook.getId().toString()))
            .body(cashBook);
    }

    /**
     * {@code PUT  /cash-books/:id} : Updates an existing cashBook.
     *
     * @param id the id of the cashBook to save.
     * @param cashBook the cashBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBook,
     * or with status {@code 400 (Bad Request)} if the cashBook is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CashBook> updateCashBook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CashBook cashBook
    ) throws URISyntaxException {
        LOG.debug("REST request to update CashBook : {}, {}", id, cashBook);
        if (cashBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cashBook = cashBookService.update(cashBook);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cashBook.getId().toString()))
            .body(cashBook);
    }

    /**
     * {@code PATCH  /cash-books/:id} : Partial updates given fields of an existing cashBook, field will ignore if it is null
     *
     * @param id the id of the cashBook to save.
     * @param cashBook the cashBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBook,
     * or with status {@code 400 (Bad Request)} if the cashBook is not valid,
     * or with status {@code 404 (Not Found)} if the cashBook is not found,
     * or with status {@code 500 (Internal Server Error)} if the cashBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CashBook> partialUpdateCashBook(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CashBook cashBook
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CashBook : {}, {}", id, cashBook);
        if (cashBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CashBook> result = cashBookService.partialUpdate(cashBook);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cashBook.getId().toString())
        );
    }

    /**
     * {@code GET  /cash-books} : get all the Cash Books.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cash Books in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CashBook>> getAllCashBooks(
        CashBookCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CashBooks by criteria: {}", criteria);

        Page<CashBook> page = cashBookQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cash-books/count} : count all the cashBooks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCashBooks(CashBookCriteria criteria) {
        LOG.debug("REST request to count CashBooks by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashBookQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-books/:id} : get the "id" cashBook.
     *
     * @param id the id of the cashBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CashBook> getCashBook(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CashBook : {}", id);
        Optional<CashBook> cashBook = cashBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashBook);
    }

    /**
     * {@code DELETE  /cash-books/:id} : delete the "id" cashBook.
     *
     * @param id the id of the cashBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashBook(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CashBook : {}", id);
        cashBookService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
