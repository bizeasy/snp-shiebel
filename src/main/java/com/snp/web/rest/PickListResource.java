package com.snp.web.rest;

import com.snp.domain.PickList;
import com.snp.repository.PickListRepository;
import com.snp.service.PickListQueryService;
import com.snp.service.PickListService;
import com.snp.service.criteria.PickListCriteria;
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
 * REST controller for managing {@link com.snp.domain.PickList}.
 */
@RestController
@RequestMapping("/api/pick-lists")
public class PickListResource {

    private static final Logger LOG = LoggerFactory.getLogger(PickListResource.class);

    private static final String ENTITY_NAME = "pickList";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PickListService pickListService;

    private final PickListRepository pickListRepository;

    private final PickListQueryService pickListQueryService;

    public PickListResource(
        PickListService pickListService,
        PickListRepository pickListRepository,
        PickListQueryService pickListQueryService
    ) {
        this.pickListService = pickListService;
        this.pickListRepository = pickListRepository;
        this.pickListQueryService = pickListQueryService;
    }

    /**
     * {@code POST  /pick-lists} : Create a new pickList.
     *
     * @param pickList the pickList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pickList, or with status {@code 400 (Bad Request)} if the pickList already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PickList> createPickList(@Valid @RequestBody PickList pickList) throws URISyntaxException {
        LOG.debug("REST request to save PickList : {}", pickList);
        if (pickList.getId() != null) {
            throw new BadRequestAlertException("A new pickList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pickList = pickListService.save(pickList);
        return ResponseEntity.created(new URI("/api/pick-lists/" + pickList.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pickList.getId().toString()))
            .body(pickList);
    }

    /**
     * {@code PUT  /pick-lists/:id} : Updates an existing pickList.
     *
     * @param id the id of the pickList to save.
     * @param pickList the pickList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pickList,
     * or with status {@code 400 (Bad Request)} if the pickList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pickList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PickList> updatePickList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PickList pickList
    ) throws URISyntaxException {
        LOG.debug("REST request to update PickList : {}, {}", id, pickList);
        if (pickList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pickList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pickListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pickList = pickListService.update(pickList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pickList.getId().toString()))
            .body(pickList);
    }

    /**
     * {@code PATCH  /pick-lists/:id} : Partial updates given fields of an existing pickList, field will ignore if it is null
     *
     * @param id the id of the pickList to save.
     * @param pickList the pickList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pickList,
     * or with status {@code 400 (Bad Request)} if the pickList is not valid,
     * or with status {@code 404 (Not Found)} if the pickList is not found,
     * or with status {@code 500 (Internal Server Error)} if the pickList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PickList> partialUpdatePickList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PickList pickList
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PickList : {}, {}", id, pickList);
        if (pickList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pickList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pickListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PickList> result = pickListService.partialUpdate(pickList);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pickList.getId().toString())
        );
    }

    /**
     * {@code GET  /pick-lists} : get all the Pick Lists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pick Lists in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PickList>> getAllPickLists(
        PickListCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PickLists by criteria: {}", criteria);

        Page<PickList> page = pickListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pick-lists/count} : count all the pickLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPickLists(PickListCriteria criteria) {
        LOG.debug("REST request to count PickLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(pickListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pick-lists/:id} : get the "id" pickList.
     *
     * @param id the id of the pickList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pickList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PickList> getPickList(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PickList : {}", id);
        Optional<PickList> pickList = pickListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pickList);
    }

    /**
     * {@code DELETE  /pick-lists/:id} : delete the "id" pickList.
     *
     * @param id the id of the pickList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePickList(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PickList : {}", id);
        pickListService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
