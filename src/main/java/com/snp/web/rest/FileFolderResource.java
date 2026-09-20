package com.snp.web.rest;

import com.snp.domain.FileFolder;
import com.snp.repository.FileFolderRepository;
import com.snp.service.FileFolderQueryService;
import com.snp.service.FileFolderService;
import com.snp.service.criteria.FileFolderCriteria;
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
 * REST controller for managing {@link com.snp.domain.FileFolder}.
 */
@RestController
@RequestMapping("/api/file-folders")
public class FileFolderResource {

    private static final Logger LOG = LoggerFactory.getLogger(FileFolderResource.class);

    private static final String ENTITY_NAME = "fileFolder";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FileFolderService fileFolderService;

    private final FileFolderRepository fileFolderRepository;

    private final FileFolderQueryService fileFolderQueryService;

    public FileFolderResource(
        FileFolderService fileFolderService,
        FileFolderRepository fileFolderRepository,
        FileFolderQueryService fileFolderQueryService
    ) {
        this.fileFolderService = fileFolderService;
        this.fileFolderRepository = fileFolderRepository;
        this.fileFolderQueryService = fileFolderQueryService;
    }

    /**
     * {@code POST  /file-folders} : Create a new fileFolder.
     *
     * @param fileFolder the fileFolder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileFolder, or with status {@code 400 (Bad Request)} if the fileFolder already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FileFolder> createFileFolder(@Valid @RequestBody FileFolder fileFolder) throws URISyntaxException {
        LOG.debug("REST request to save FileFolder : {}", fileFolder);
        if (fileFolder.getId() != null) {
            throw new BadRequestAlertException("A new fileFolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fileFolder = fileFolderService.save(fileFolder);
        return ResponseEntity.created(new URI("/api/file-folders/" + fileFolder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fileFolder.getId().toString()))
            .body(fileFolder);
    }

    /**
     * {@code PUT  /file-folders/:id} : Updates an existing fileFolder.
     *
     * @param id the id of the fileFolder to save.
     * @param fileFolder the fileFolder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileFolder,
     * or with status {@code 400 (Bad Request)} if the fileFolder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileFolder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FileFolder> updateFileFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FileFolder fileFolder
    ) throws URISyntaxException {
        LOG.debug("REST request to update FileFolder : {}, {}", id, fileFolder);
        if (fileFolder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileFolder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fileFolder = fileFolderService.update(fileFolder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileFolder.getId().toString()))
            .body(fileFolder);
    }

    /**
     * {@code PATCH  /file-folders/:id} : Partial updates given fields of an existing fileFolder, field will ignore if it is null
     *
     * @param id the id of the fileFolder to save.
     * @param fileFolder the fileFolder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileFolder,
     * or with status {@code 400 (Bad Request)} if the fileFolder is not valid,
     * or with status {@code 404 (Not Found)} if the fileFolder is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileFolder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileFolder> partialUpdateFileFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FileFolder fileFolder
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FileFolder : {}, {}", id, fileFolder);
        if (fileFolder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileFolder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileFolder> result = fileFolderService.partialUpdate(fileFolder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileFolder.getId().toString())
        );
    }

    /**
     * {@code GET  /file-folders} : get all the File Folders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of File Folders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FileFolder>> getAllFileFolders(
        FileFolderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FileFolders by criteria: {}", criteria);

        Page<FileFolder> page = fileFolderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-folders/count} : count all the fileFolders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFileFolders(FileFolderCriteria criteria) {
        LOG.debug("REST request to count FileFolders by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileFolderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /file-folders/:id} : get the "id" fileFolder.
     *
     * @param id the id of the fileFolder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileFolder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FileFolder> getFileFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FileFolder : {}", id);
        Optional<FileFolder> fileFolder = fileFolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileFolder);
    }

    /**
     * {@code DELETE  /file-folders/:id} : delete the "id" fileFolder.
     *
     * @param id the id of the fileFolder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFileFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FileFolder : {}", id);
        fileFolderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
