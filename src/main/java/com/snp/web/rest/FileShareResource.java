package com.snp.web.rest;

import com.snp.domain.FileShare;
import com.snp.repository.FileShareRepository;
import com.snp.service.FileShareQueryService;
import com.snp.service.FileShareService;
import com.snp.service.criteria.FileShareCriteria;
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
 * REST controller for managing {@link com.snp.domain.FileShare}.
 */
@RestController
@RequestMapping("/api/file-shares")
public class FileShareResource {

    private static final Logger LOG = LoggerFactory.getLogger(FileShareResource.class);

    private static final String ENTITY_NAME = "fileShare";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FileShareService fileShareService;

    private final FileShareRepository fileShareRepository;

    private final FileShareQueryService fileShareQueryService;

    public FileShareResource(
        FileShareService fileShareService,
        FileShareRepository fileShareRepository,
        FileShareQueryService fileShareQueryService
    ) {
        this.fileShareService = fileShareService;
        this.fileShareRepository = fileShareRepository;
        this.fileShareQueryService = fileShareQueryService;
    }

    /**
     * {@code POST  /file-shares} : Create a new fileShare.
     *
     * @param fileShare the fileShare to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileShare, or with status {@code 400 (Bad Request)} if the fileShare already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FileShare> createFileShare(@Valid @RequestBody FileShare fileShare) throws URISyntaxException {
        LOG.debug("REST request to save FileShare : {}", fileShare);
        if (fileShare.getId() != null) {
            throw new BadRequestAlertException("A new fileShare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fileShare = fileShareService.save(fileShare);
        return ResponseEntity.created(new URI("/api/file-shares/" + fileShare.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fileShare.getId().toString()))
            .body(fileShare);
    }

    /**
     * {@code PUT  /file-shares/:id} : Updates an existing fileShare.
     *
     * @param id the id of the fileShare to save.
     * @param fileShare the fileShare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileShare,
     * or with status {@code 400 (Bad Request)} if the fileShare is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileShare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FileShare> updateFileShare(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FileShare fileShare
    ) throws URISyntaxException {
        LOG.debug("REST request to update FileShare : {}, {}", id, fileShare);
        if (fileShare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileShare.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileShareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fileShare = fileShareService.update(fileShare);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileShare.getId().toString()))
            .body(fileShare);
    }

    /**
     * {@code PATCH  /file-shares/:id} : Partial updates given fields of an existing fileShare, field will ignore if it is null
     *
     * @param id the id of the fileShare to save.
     * @param fileShare the fileShare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileShare,
     * or with status {@code 400 (Bad Request)} if the fileShare is not valid,
     * or with status {@code 404 (Not Found)} if the fileShare is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileShare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileShare> partialUpdateFileShare(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FileShare fileShare
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FileShare : {}, {}", id, fileShare);
        if (fileShare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileShare.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileShareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileShare> result = fileShareService.partialUpdate(fileShare);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileShare.getId().toString())
        );
    }

    /**
     * {@code GET  /file-shares} : get all the File Shares.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of File Shares in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FileShare>> getAllFileShares(
        FileShareCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FileShares by criteria: {}", criteria);

        Page<FileShare> page = fileShareQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-shares/count} : count all the fileShares.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFileShares(FileShareCriteria criteria) {
        LOG.debug("REST request to count FileShares by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileShareQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /file-shares/:id} : get the "id" fileShare.
     *
     * @param id the id of the fileShare to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileShare, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FileShare> getFileShare(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FileShare : {}", id);
        Optional<FileShare> fileShare = fileShareService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileShare);
    }

    /**
     * {@code DELETE  /file-shares/:id} : delete the "id" fileShare.
     *
     * @param id the id of the fileShare to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFileShare(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FileShare : {}", id);
        fileShareService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
