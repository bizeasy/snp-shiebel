package com.snp.web.rest;

import com.snp.domain.ProductPrice;
import com.snp.repository.ProductPriceRepository;
import com.snp.service.ProductPriceQueryService;
import com.snp.service.ProductPriceService;
import com.snp.service.criteria.ProductPriceCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductPrice}.
 */
@RestController
@RequestMapping("/api/product-prices")
public class ProductPriceResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceResource.class);

    private static final String ENTITY_NAME = "productPrice";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductPriceService productPriceService;

    private final ProductPriceRepository productPriceRepository;

    private final ProductPriceQueryService productPriceQueryService;

    public ProductPriceResource(
        ProductPriceService productPriceService,
        ProductPriceRepository productPriceRepository,
        ProductPriceQueryService productPriceQueryService
    ) {
        this.productPriceService = productPriceService;
        this.productPriceRepository = productPriceRepository;
        this.productPriceQueryService = productPriceQueryService;
    }

    /**
     * {@code POST  /product-prices} : Create a new productPrice.
     *
     * @param productPrice the productPrice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productPrice, or with status {@code 400 (Bad Request)} if the productPrice already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductPrice> createProductPrice(@Valid @RequestBody ProductPrice productPrice) throws URISyntaxException {
        LOG.debug("REST request to save ProductPrice : {}", productPrice);
        if (productPrice.getId() != null) {
            throw new BadRequestAlertException("A new productPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productPrice = productPriceService.save(productPrice);
        return ResponseEntity.created(new URI("/api/product-prices/" + productPrice.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productPrice.getId().toString()))
            .body(productPrice);
    }

    /**
     * {@code PUT  /product-prices/:id} : Updates an existing productPrice.
     *
     * @param id the id of the productPrice to save.
     * @param productPrice the productPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPrice,
     * or with status {@code 400 (Bad Request)} if the productPrice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductPrice> updateProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductPrice productPrice
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductPrice : {}, {}", id, productPrice);
        if (productPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productPrice = productPriceService.update(productPrice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPrice.getId().toString()))
            .body(productPrice);
    }

    /**
     * {@code PATCH  /product-prices/:id} : Partial updates given fields of an existing productPrice, field will ignore if it is null
     *
     * @param id the id of the productPrice to save.
     * @param productPrice the productPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPrice,
     * or with status {@code 400 (Bad Request)} if the productPrice is not valid,
     * or with status {@code 404 (Not Found)} if the productPrice is not found,
     * or with status {@code 500 (Internal Server Error)} if the productPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductPrice> partialUpdateProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductPrice productPrice
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductPrice : {}, {}", id, productPrice);
        if (productPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductPrice> result = productPriceService.partialUpdate(productPrice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPrice.getId().toString())
        );
    }

    /**
     * {@code GET  /product-prices} : get all the Product Prices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Prices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductPrice>> getAllProductPrices(
        ProductPriceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductPrices by criteria: {}", criteria);

        Page<ProductPrice> page = productPriceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-prices/count} : count all the productPrices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductPrices(ProductPriceCriteria criteria) {
        LOG.debug("REST request to count ProductPrices by criteria: {}", criteria);
        return ResponseEntity.ok().body(productPriceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-prices/:id} : get the "id" productPrice.
     *
     * @param id the id of the productPrice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productPrice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductPrice> getProductPrice(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductPrice : {}", id);
        Optional<ProductPrice> productPrice = productPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productPrice);
    }

    /**
     * {@code DELETE  /product-prices/:id} : delete the "id" productPrice.
     *
     * @param id the id of the productPrice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductPrice(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductPrice : {}", id);
        productPriceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
