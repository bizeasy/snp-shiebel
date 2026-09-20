package com.snp.web.rest;

import com.snp.domain.OrderItemAttachment;
import com.snp.repository.OrderItemAttachmentRepository;
import com.snp.service.OrderItemAttachmentQueryService;
import com.snp.service.OrderItemAttachmentService;
import com.snp.service.criteria.OrderItemAttachmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderItemAttachment}.
 */
@RestController
@RequestMapping("/api/order-item-attachments")
public class OrderItemAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemAttachmentResource.class);

    private static final String ENTITY_NAME = "orderItemAttachment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderItemAttachmentService orderItemAttachmentService;

    private final OrderItemAttachmentRepository orderItemAttachmentRepository;

    private final OrderItemAttachmentQueryService orderItemAttachmentQueryService;

    public OrderItemAttachmentResource(
        OrderItemAttachmentService orderItemAttachmentService,
        OrderItemAttachmentRepository orderItemAttachmentRepository,
        OrderItemAttachmentQueryService orderItemAttachmentQueryService
    ) {
        this.orderItemAttachmentService = orderItemAttachmentService;
        this.orderItemAttachmentRepository = orderItemAttachmentRepository;
        this.orderItemAttachmentQueryService = orderItemAttachmentQueryService;
    }

    /**
     * {@code POST  /order-item-attachments} : Create a new orderItemAttachment.
     *
     * @param orderItemAttachment the orderItemAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderItemAttachment, or with status {@code 400 (Bad Request)} if the orderItemAttachment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderItemAttachment> createOrderItemAttachment(@Valid @RequestBody OrderItemAttachment orderItemAttachment)
        throws URISyntaxException {
        LOG.debug("REST request to save OrderItemAttachment : {}", orderItemAttachment);
        if (orderItemAttachment.getId() != null) {
            throw new BadRequestAlertException("A new orderItemAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderItemAttachment = orderItemAttachmentService.save(orderItemAttachment);
        return ResponseEntity.created(new URI("/api/order-item-attachments/" + orderItemAttachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderItemAttachment.getId().toString()))
            .body(orderItemAttachment);
    }

    /**
     * {@code PUT  /order-item-attachments/:id} : Updates an existing orderItemAttachment.
     *
     * @param id the id of the orderItemAttachment to save.
     * @param orderItemAttachment the orderItemAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemAttachment,
     * or with status {@code 400 (Bad Request)} if the orderItemAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderItemAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemAttachment> updateOrderItemAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderItemAttachment orderItemAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderItemAttachment : {}, {}", id, orderItemAttachment);
        if (orderItemAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderItemAttachment = orderItemAttachmentService.update(orderItemAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemAttachment.getId().toString()))
            .body(orderItemAttachment);
    }

    /**
     * {@code PATCH  /order-item-attachments/:id} : Partial updates given fields of an existing orderItemAttachment, field will ignore if it is null
     *
     * @param id the id of the orderItemAttachment to save.
     * @param orderItemAttachment the orderItemAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemAttachment,
     * or with status {@code 400 (Bad Request)} if the orderItemAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the orderItemAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderItemAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemAttachment> partialUpdateOrderItemAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderItemAttachment orderItemAttachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderItemAttachment : {}, {}", id, orderItemAttachment);
        if (orderItemAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderItemAttachment> result = orderItemAttachmentService.partialUpdate(orderItemAttachment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /order-item-attachments} : get all the Order Item Attachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Item Attachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderItemAttachment>> getAllOrderItemAttachments(
        OrderItemAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderItemAttachments by criteria: {}", criteria);

        Page<OrderItemAttachment> page = orderItemAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-item-attachments/count} : count all the orderItemAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderItemAttachments(OrderItemAttachmentCriteria criteria) {
        LOG.debug("REST request to count OrderItemAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderItemAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-item-attachments/:id} : get the "id" orderItemAttachment.
     *
     * @param id the id of the orderItemAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderItemAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemAttachment> getOrderItemAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderItemAttachment : {}", id);
        Optional<OrderItemAttachment> orderItemAttachment = orderItemAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemAttachment);
    }

    /**
     * {@code DELETE  /order-item-attachments/:id} : delete the "id" orderItemAttachment.
     *
     * @param id the id of the orderItemAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItemAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderItemAttachment : {}", id);
        orderItemAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
