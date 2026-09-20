package com.snp.web.rest;

import com.snp.domain.Lot;
import com.snp.repository.LotRepository;
import com.snp.service.LotQueryService;
import com.snp.service.LotService;
import com.snp.service.criteria.LotCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.snp.domain.Lot}.
 */
@RestController
@RequestMapping("/api/lots")
public class LotResource {

    private static final Logger LOG = LoggerFactory.getLogger(LotResource.class);

    private static final String ENTITY_NAME = "lot";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final LotService lotService;

    private final LotRepository lotRepository;

    private final LotQueryService lotQueryService;

    public LotResource(LotService lotService, LotRepository lotRepository, LotQueryService lotQueryService) {
        this.lotService = lotService;
        this.lotRepository = lotRepository;
        this.lotQueryService = lotQueryService;
    }

    /**
     * {@code POST  /lots} : Create a new lot.
     *
     * @param lot the lot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lot, or with status {@code 400 (Bad Request)} if the lot already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Lot> createLot(@RequestBody Lot lot) throws URISyntaxException {
        LOG.debug("REST request to save Lot : {}", lot);
        if (lot.getId() != null) {
            throw new BadRequestAlertException("A new lot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lot = lotService.save(lot);
        return ResponseEntity.created(new URI("/api/lots/" + lot.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lot.getId().toString()))
            .body(lot);
    }

    /**
     * {@code PUT  /lots/:id} : Updates an existing lot.
     *
     * @param id the id of the lot to save.
     * @param lot the lot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lot,
     * or with status {@code 400 (Bad Request)} if the lot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lot> updateLot(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lot lot)
        throws URISyntaxException {
        LOG.debug("REST request to update Lot : {}, {}", id, lot);
        if (lot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lot = lotService.update(lot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lot.getId().toString()))
            .body(lot);
    }

    /**
     * {@code PATCH  /lots/:id} : Partial updates given fields of an existing lot, field will ignore if it is null
     *
     * @param id the id of the lot to save.
     * @param lot the lot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lot,
     * or with status {@code 400 (Bad Request)} if the lot is not valid,
     * or with status {@code 404 (Not Found)} if the lot is not found,
     * or with status {@code 500 (Internal Server Error)} if the lot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lot> partialUpdateLot(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lot lot)
        throws URISyntaxException {
        LOG.debug("REST request to partially update Lot : {}, {}", id, lot);
        if (lot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lot> result = lotService.partialUpdate(lot);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lot.getId().toString())
        );
    }

    /**
     * {@code GET  /lots} : get all the Lots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Lots in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Lot>> getAllLots(LotCriteria criteria) {
        LOG.debug("REST request to get Lots by criteria: {}", criteria);

        List<Lot> entityList = lotQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /lots/count} : count all the lots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLots(LotCriteria criteria) {
        LOG.debug("REST request to count Lots by criteria: {}", criteria);
        return ResponseEntity.ok().body(lotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lots/:id} : get the "id" lot.
     *
     * @param id the id of the lot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lot> getLot(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Lot : {}", id);
        Optional<Lot> lot = lotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lot);
    }

    /**
     * {@code DELETE  /lots/:id} : delete the "id" lot.
     *
     * @param id the id of the lot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLot(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Lot : {}", id);
        lotService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
