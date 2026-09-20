package com.snp.web.rest;

import com.snp.domain.CustRequestItem;
import com.snp.repository.CustRequestItemRepository;
import com.snp.service.CustRequestItemQueryService;
import com.snp.service.CustRequestItemService;
import com.snp.service.criteria.CustRequestItemCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustRequestItem}.
 */
@RestController
@RequestMapping("/api/cust-request-items")
public class CustRequestItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestItemResource.class);

    private static final String ENTITY_NAME = "custRequestItem";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustRequestItemService custRequestItemService;

    private final CustRequestItemRepository custRequestItemRepository;

    private final CustRequestItemQueryService custRequestItemQueryService;

    public CustRequestItemResource(
        CustRequestItemService custRequestItemService,
        CustRequestItemRepository custRequestItemRepository,
        CustRequestItemQueryService custRequestItemQueryService
    ) {
        this.custRequestItemService = custRequestItemService;
        this.custRequestItemRepository = custRequestItemRepository;
        this.custRequestItemQueryService = custRequestItemQueryService;
    }

    /**
     * {@code POST  /cust-request-items} : Create a new custRequestItem.
     *
     * @param custRequestItem the custRequestItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new custRequestItem, or with status {@code 400 (Bad Request)} if the custRequestItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustRequestItem> createCustRequestItem(@Valid @RequestBody CustRequestItem custRequestItem)
        throws URISyntaxException {
        LOG.debug("REST request to save CustRequestItem : {}", custRequestItem);
        if (custRequestItem.getId() != null) {
            throw new BadRequestAlertException("A new custRequestItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        custRequestItem = custRequestItemService.save(custRequestItem);
        return ResponseEntity.created(new URI("/api/cust-request-items/" + custRequestItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, custRequestItem.getId().toString()))
            .body(custRequestItem);
    }

    /**
     * {@code PUT  /cust-request-items/:id} : Updates an existing custRequestItem.
     *
     * @param id the id of the custRequestItem to save.
     * @param custRequestItem the custRequestItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestItem,
     * or with status {@code 400 (Bad Request)} if the custRequestItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the custRequestItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustRequestItem> updateCustRequestItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustRequestItem custRequestItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustRequestItem : {}, {}", id, custRequestItem);
        if (custRequestItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        custRequestItem = custRequestItemService.update(custRequestItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestItem.getId().toString()))
            .body(custRequestItem);
    }

    /**
     * {@code PATCH  /cust-request-items/:id} : Partial updates given fields of an existing custRequestItem, field will ignore if it is null
     *
     * @param id the id of the custRequestItem to save.
     * @param custRequestItem the custRequestItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestItem,
     * or with status {@code 400 (Bad Request)} if the custRequestItem is not valid,
     * or with status {@code 404 (Not Found)} if the custRequestItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the custRequestItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustRequestItem> partialUpdateCustRequestItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustRequestItem custRequestItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustRequestItem : {}, {}", id, custRequestItem);
        if (custRequestItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustRequestItem> result = custRequestItemService.partialUpdate(custRequestItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestItem.getId().toString())
        );
    }

    /**
     * {@code GET  /cust-request-items} : get all the Cust Request Items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cust Request Items in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustRequestItem>> getAllCustRequestItems(
        CustRequestItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustRequestItems by criteria: {}", criteria);

        Page<CustRequestItem> page = custRequestItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cust-request-items/count} : count all the custRequestItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustRequestItems(CustRequestItemCriteria criteria) {
        LOG.debug("REST request to count CustRequestItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(custRequestItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cust-request-items/:id} : get the "id" custRequestItem.
     *
     * @param id the id of the custRequestItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the custRequestItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustRequestItem> getCustRequestItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustRequestItem : {}", id);
        Optional<CustRequestItem> custRequestItem = custRequestItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(custRequestItem);
    }

    /**
     * {@code DELETE  /cust-request-items/:id} : delete the "id" custRequestItem.
     *
     * @param id the id of the custRequestItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustRequestItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustRequestItem : {}", id);
        custRequestItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
