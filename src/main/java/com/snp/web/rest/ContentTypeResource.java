package com.snp.web.rest;

import com.snp.domain.ContentType;
import com.snp.repository.ContentTypeRepository;
import com.snp.service.ContentTypeQueryService;
import com.snp.service.ContentTypeService;
import com.snp.service.criteria.ContentTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ContentType}.
 */
@RestController
@RequestMapping("/api/content-types")
public class ContentTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContentTypeResource.class);

    private static final String ENTITY_NAME = "contentType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ContentTypeService contentTypeService;

    private final ContentTypeRepository contentTypeRepository;

    private final ContentTypeQueryService contentTypeQueryService;

    public ContentTypeResource(
        ContentTypeService contentTypeService,
        ContentTypeRepository contentTypeRepository,
        ContentTypeQueryService contentTypeQueryService
    ) {
        this.contentTypeService = contentTypeService;
        this.contentTypeRepository = contentTypeRepository;
        this.contentTypeQueryService = contentTypeQueryService;
    }

    /**
     * {@code POST  /content-types} : Create a new contentType.
     *
     * @param contentType the contentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentType, or with status {@code 400 (Bad Request)} if the contentType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContentType> createContentType(@Valid @RequestBody ContentType contentType) throws URISyntaxException {
        LOG.debug("REST request to save ContentType : {}", contentType);
        if (contentType.getId() != null) {
            throw new BadRequestAlertException("A new contentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contentType = contentTypeService.save(contentType);
        return ResponseEntity.created(new URI("/api/content-types/" + contentType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contentType.getId().toString()))
            .body(contentType);
    }

    /**
     * {@code PUT  /content-types/:id} : Updates an existing contentType.
     *
     * @param id the id of the contentType to save.
     * @param contentType the contentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentType,
     * or with status {@code 400 (Bad Request)} if the contentType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContentType> updateContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentType contentType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContentType : {}, {}", id, contentType);
        if (contentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contentType = contentTypeService.update(contentType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentType.getId().toString()))
            .body(contentType);
    }

    /**
     * {@code PATCH  /content-types/:id} : Partial updates given fields of an existing contentType, field will ignore if it is null
     *
     * @param id the id of the contentType to save.
     * @param contentType the contentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentType,
     * or with status {@code 400 (Bad Request)} if the contentType is not valid,
     * or with status {@code 404 (Not Found)} if the contentType is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentType> partialUpdateContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContentType contentType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ContentType : {}, {}", id, contentType);
        if (contentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentType> result = contentTypeService.partialUpdate(contentType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentType.getId().toString())
        );
    }

    /**
     * {@code GET  /content-types} : get all the Content Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Content Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContentType>> getAllContentTypes(ContentTypeCriteria criteria) {
        LOG.debug("REST request to get ContentTypes by criteria: {}", criteria);

        List<ContentType> entityList = contentTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /content-types/count} : count all the contentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContentTypes(ContentTypeCriteria criteria) {
        LOG.debug("REST request to count ContentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /content-types/:id} : get the "id" contentType.
     *
     * @param id the id of the contentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContentType> getContentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContentType : {}", id);
        Optional<ContentType> contentType = contentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentType);
    }

    /**
     * {@code DELETE  /content-types/:id} : delete the "id" contentType.
     *
     * @param id the id of the contentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContentType : {}", id);
        contentTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
