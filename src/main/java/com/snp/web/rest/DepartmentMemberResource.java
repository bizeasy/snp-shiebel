package com.snp.web.rest;

import com.snp.domain.DepartmentMember;
import com.snp.repository.DepartmentMemberRepository;
import com.snp.service.DepartmentMemberQueryService;
import com.snp.service.DepartmentMemberService;
import com.snp.service.criteria.DepartmentMemberCriteria;
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
 * REST controller for managing {@link com.snp.domain.DepartmentMember}.
 */
@RestController
@RequestMapping("/api/department-members")
public class DepartmentMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentMemberResource.class);

    private static final String ENTITY_NAME = "departmentMember";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final DepartmentMemberService departmentMemberService;

    private final DepartmentMemberRepository departmentMemberRepository;

    private final DepartmentMemberQueryService departmentMemberQueryService;

    public DepartmentMemberResource(
        DepartmentMemberService departmentMemberService,
        DepartmentMemberRepository departmentMemberRepository,
        DepartmentMemberQueryService departmentMemberQueryService
    ) {
        this.departmentMemberService = departmentMemberService;
        this.departmentMemberRepository = departmentMemberRepository;
        this.departmentMemberQueryService = departmentMemberQueryService;
    }

    /**
     * {@code POST  /department-members} : Create a new departmentMember.
     *
     * @param departmentMember the departmentMember to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentMember, or with status {@code 400 (Bad Request)} if the departmentMember already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DepartmentMember> createDepartmentMember(@Valid @RequestBody DepartmentMember departmentMember)
        throws URISyntaxException {
        LOG.debug("REST request to save DepartmentMember : {}", departmentMember);
        if (departmentMember.getId() != null) {
            throw new BadRequestAlertException("A new departmentMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        departmentMember = departmentMemberService.save(departmentMember);
        return ResponseEntity.created(new URI("/api/department-members/" + departmentMember.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, departmentMember.getId().toString()))
            .body(departmentMember);
    }

    /**
     * {@code PUT  /department-members/:id} : Updates an existing departmentMember.
     *
     * @param id the id of the departmentMember to save.
     * @param departmentMember the departmentMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentMember,
     * or with status {@code 400 (Bad Request)} if the departmentMember is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentMember> updateDepartmentMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepartmentMember departmentMember
    ) throws URISyntaxException {
        LOG.debug("REST request to update DepartmentMember : {}, {}", id, departmentMember);
        if (departmentMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        departmentMember = departmentMemberService.update(departmentMember);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentMember.getId().toString()))
            .body(departmentMember);
    }

    /**
     * {@code PATCH  /department-members/:id} : Partial updates given fields of an existing departmentMember, field will ignore if it is null
     *
     * @param id the id of the departmentMember to save.
     * @param departmentMember the departmentMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentMember,
     * or with status {@code 400 (Bad Request)} if the departmentMember is not valid,
     * or with status {@code 404 (Not Found)} if the departmentMember is not found,
     * or with status {@code 500 (Internal Server Error)} if the departmentMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepartmentMember> partialUpdateDepartmentMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepartmentMember departmentMember
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update DepartmentMember : {}, {}", id, departmentMember);
        if (departmentMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartmentMember> result = departmentMemberService.partialUpdate(departmentMember);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentMember.getId().toString())
        );
    }

    /**
     * {@code GET  /department-members} : get all the Department Members.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Department Members in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DepartmentMember>> getAllDepartmentMembers(
        DepartmentMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DepartmentMembers by criteria: {}", criteria);

        Page<DepartmentMember> page = departmentMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /department-members/count} : count all the departmentMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDepartmentMembers(DepartmentMemberCriteria criteria) {
        LOG.debug("REST request to count DepartmentMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(departmentMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /department-members/:id} : get the "id" departmentMember.
     *
     * @param id the id of the departmentMember to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentMember, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentMember> getDepartmentMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DepartmentMember : {}", id);
        Optional<DepartmentMember> departmentMember = departmentMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentMember);
    }

    /**
     * {@code DELETE  /department-members/:id} : delete the "id" departmentMember.
     *
     * @param id the id of the departmentMember to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartmentMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DepartmentMember : {}", id);
        departmentMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
