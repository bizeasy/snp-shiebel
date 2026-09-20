package com.snp.web.rest;

import com.snp.domain.OrderAttachment;
import com.snp.repository.OrderAttachmentRepository;
import com.snp.service.OrderAttachmentQueryService;
import com.snp.service.OrderAttachmentService;
import com.snp.service.criteria.OrderAttachmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderAttachment}.
 */
@RestController
@RequestMapping("/api/order-attachments")
public class OrderAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAttachmentResource.class);

    private static final String ENTITY_NAME = "orderAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderAttachmentService orderAttachmentService;

    private final OrderAttachmentRepository orderAttachmentRepository;

    private final OrderAttachmentQueryService orderAttachmentQueryService;

    public OrderAttachmentResource(
        OrderAttachmentService orderAttachmentService,
        OrderAttachmentRepository orderAttachmentRepository,
        OrderAttachmentQueryService orderAttachmentQueryService
    ) {
        this.orderAttachmentService = orderAttachmentService;
        this.orderAttachmentRepository = orderAttachmentRepository;
        this.orderAttachmentQueryService = orderAttachmentQueryService;
    }

    /**
     * {@code POST  /order-attachments} : Create a new orderAttachment.
     *
     * @param orderAttachment the orderAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderAttachment, or with status {@code 400 (Bad Request)} if the orderAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderAttachment> createOrderAttachment(@Valid @RequestBody OrderAttachment orderAttachment)
        throws URISyntaxException {
        LOG.debug("REST request to save OrderAttachment : {}", orderAttachment);
        if (orderAttachment.getId() != null) {
            throw new BadRequestAlertException("A new orderAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderAttachment = orderAttachmentService.save(orderAttachment);
        return ResponseEntity.created(new URI("/api/order-attachments/" + orderAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderAttachment.getId().toString()))
            .body(orderAttachment);
    }

    /**
     * {@code PUT  /order-attachments/:id} : Updates an existing orderAttachment.
     *
     * @param id the id of the orderAttachment to save.
     * @param orderAttachment the orderAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAttachment,
     * or with status {@code 400 (Bad Request)} if the orderAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderAttachment> updateOrderAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderAttachment orderAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderAttachment : {}, {}", id, orderAttachment);
        if (orderAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderAttachment = orderAttachmentService.update(orderAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAttachment.getId().toString()))
            .body(orderAttachment);
    }

    /**
     * {@code PATCH  /order-attachments/:id} : Partial updates given fields of an existing orderAttachment, field will ignore if it is null
     *
     * @param id the id of the orderAttachment to save.
     * @param orderAttachment the orderAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAttachment,
     * or with status {@code 400 (Bad Request)} if the orderAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the orderAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderAttachment> partialUpdateOrderAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderAttachment orderAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderAttachment : {}, {}", id, orderAttachment);
        if (orderAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderAttachment> result = orderAttachmentService.partialUpdate(orderAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /order-attachments} : get all the Order Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderAttachment>> getAllOrderAttachments(
        OrderAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderAttachments by criteria: {}", criteria);

        Page<OrderAttachment> page = orderAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-attachments/count} : count all the orderAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderAttachments(OrderAttachmentCriteria criteria) {
        LOG.debug("REST request to count OrderAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-attachments/:id} : get the "id" orderAttachment.
     *
     * @param id the id of the orderAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderAttachment> getOrderAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderAttachment : {}", id);
        Optional<OrderAttachment> orderAttachment = orderAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderAttachment);
    }

    /**
     * {@code DELETE  /order-attachments/:id} : delete the "id" orderAttachment.
     *
     * @param id the id of the orderAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderAttachment : {}", id);
        orderAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
