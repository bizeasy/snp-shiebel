package com.snp.web.rest;

import com.snp.domain.KeywordType;
import com.snp.repository.KeywordTypeRepository;
import com.snp.service.KeywordTypeQueryService;
import com.snp.service.KeywordTypeService;
import com.snp.service.criteria.KeywordTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.KeywordType}.
 */
@RestController
@RequestMapping("/api/keyword-types")
public class KeywordTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(KeywordTypeResource.class);

    private static final String ENTITY_NAME = "keywordType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final KeywordTypeService keywordTypeService;

    private final KeywordTypeRepository keywordTypeRepository;

    private final KeywordTypeQueryService keywordTypeQueryService;

    public KeywordTypeResource(
        KeywordTypeService keywordTypeService,
        KeywordTypeRepository keywordTypeRepository,
        KeywordTypeQueryService keywordTypeQueryService
    ) {
        this.keywordTypeService = keywordTypeService;
        this.keywordTypeRepository = keywordTypeRepository;
        this.keywordTypeQueryService = keywordTypeQueryService;
    }

    /**
     * {@code POST  /keyword-types} : Create a new keywordType.
     *
     * @param keywordType the keywordType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new keywordType, or with status {@code 400 (Bad Request)} if the keywordType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KeywordType> createKeywordType(@Valid @RequestBody KeywordType keywordType) throws URISyntaxException {
        LOG.debug("REST request to save KeywordType : {}", keywordType);
        if (keywordType.getId() != null) {
            throw new BadRequestAlertException("A new keywordType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        keywordType = keywordTypeService.save(keywordType);
        return ResponseEntity.created(new URI("/api/keyword-types/" + keywordType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, keywordType.getId().toString()))
            .body(keywordType);
    }

    /**
     * {@code PUT  /keyword-types/:id} : Updates an existing keywordType.
     *
     * @param id the id of the keywordType to save.
     * @param keywordType the keywordType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keywordType,
     * or with status {@code 400 (Bad Request)} if the keywordType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the keywordType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KeywordType> updateKeywordType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KeywordType keywordType
    ) throws URISyntaxException {
        LOG.debug("REST request to update KeywordType : {}, {}", id, keywordType);
        if (keywordType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keywordType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keywordTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        keywordType = keywordTypeService.update(keywordType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, keywordType.getId().toString()))
            .body(keywordType);
    }

    /**
     * {@code PATCH  /keyword-types/:id} : Partial updates given fields of an existing keywordType, field will ignore if it is null
     *
     * @param id the id of the keywordType to save.
     * @param keywordType the keywordType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keywordType,
     * or with status {@code 400 (Bad Request)} if the keywordType is not valid,
     * or with status {@code 404 (Not Found)} if the keywordType is not found,
     * or with status {@code 500 (Internal Server Error)} if the keywordType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KeywordType> partialUpdateKeywordType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KeywordType keywordType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update KeywordType : {}, {}", id, keywordType);
        if (keywordType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keywordType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keywordTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KeywordType> result = keywordTypeService.partialUpdate(keywordType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, keywordType.getId().toString())
        );
    }

    /**
     * {@code GET  /keyword-types} : get all the Keyword Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Keyword Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KeywordType>> getAllKeywordTypes(KeywordTypeCriteria criteria) {
        LOG.debug("REST request to get KeywordTypes by criteria: {}", criteria);

        List<KeywordType> entityList = keywordTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /keyword-types/count} : count all the keywordTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countKeywordTypes(KeywordTypeCriteria criteria) {
        LOG.debug("REST request to count KeywordTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(keywordTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /keyword-types/:id} : get the "id" keywordType.
     *
     * @param id the id of the keywordType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the keywordType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KeywordType> getKeywordType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KeywordType : {}", id);
        Optional<KeywordType> keywordType = keywordTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(keywordType);
    }

    /**
     * {@code DELETE  /keyword-types/:id} : delete the "id" keywordType.
     *
     * @param id the id of the keywordType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeywordType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KeywordType : {}", id);
        keywordTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
