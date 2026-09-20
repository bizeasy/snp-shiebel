package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Product;
import com.snp.repository.ProductRepository;
import com.snp.service.criteria.ProductCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Product} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Return a {@link Page} of {@link Product} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Product> findByCriteria(ProductCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Product_.productType, JoinType.LEFT);
                root.fetch(Product_.primaryProductCategory, JoinType.LEFT);
                root.fetch(Product_.virtualProduct, JoinType.LEFT);
                root.fetch(Product_.inventoryItemType, JoinType.LEFT);
                root.fetch(Product_.taxSlab, JoinType.LEFT);
                root.fetch(Product_.uom, JoinType.LEFT);
                root.fetch(Product_.quantityUom, JoinType.LEFT);
                root.fetch(Product_.weightUom, JoinType.LEFT);
                root.fetch(Product_.sizeUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Product_.id),
                    buildStringSpecification(criteria.getName(), Product_.name),
                    buildStringSpecification(criteria.getInternalName(), Product_.internalName),
                    buildStringSpecification(criteria.getBrandName(), Product_.brandName),
                    buildStringSpecification(criteria.getVariant(), Product_.variant),
                    buildStringSpecification(criteria.getSku(), Product_.sku),
                    buildRangeSpecification(criteria.getIntroductionDate(), Product_.introductionDate),
                    buildRangeSpecification(criteria.getReleaseDate(), Product_.releaseDate),
                    buildRangeSpecification(criteria.getQuantityIncluded(), Product_.quantityIncluded),
                    buildRangeSpecification(criteria.getPiecesIncluded(), Product_.piecesIncluded),
                    buildRangeSpecification(criteria.getWeight(), Product_.weight),
                    buildRangeSpecification(criteria.getHeight(), Product_.height),
                    buildRangeSpecification(criteria.getWidth(), Product_.width),
                    buildRangeSpecification(criteria.getDepth(), Product_.depth),
                    buildStringSpecification(criteria.getSmallImageUrl(), Product_.smallImageUrl),
                    buildStringSpecification(criteria.getMediumImageUrl(), Product_.mediumImageUrl),
                    buildStringSpecification(criteria.getLargeImageUrl(), Product_.largeImageUrl),
                    buildStringSpecification(criteria.getDetailImageUrl(), Product_.detailImageUrl),
                    buildStringSpecification(criteria.getOriginalImageUrl(), Product_.originalImageUrl),
                    buildStringSpecification(criteria.getAltImageUrl1(), Product_.altImageUrl1),
                    buildStringSpecification(criteria.getAltImageUrl2(), Product_.altImageUrl2),
                    buildStringSpecification(criteria.getAltImageUrl3(), Product_.altImageUrl3),
                    buildStringSpecification(criteria.getAltImageUrl4(), Product_.altImageUrl4),
                    buildStringSpecification(criteria.getAltImageUrl5(), Product_.altImageUrl5),
                    buildStringSpecification(criteria.getAltImageUrl6(), Product_.altImageUrl6),
                    buildStringSpecification(criteria.getVideoUrl(), Product_.videoUrl),
                    buildRangeSpecification(criteria.getQuantity(), Product_.quantity),
                    buildRangeSpecification(criteria.getPrice(), Product_.price),
                    buildRangeSpecification(criteria.getCgst(), Product_.cgst),
                    buildRangeSpecification(criteria.getIgst(), Product_.igst),
                    buildRangeSpecification(criteria.getSgst(), Product_.sgst),
                    buildRangeSpecification(criteria.getCgstPercentage(), Product_.cgstPercentage),
                    buildRangeSpecification(criteria.getIgstPercentage(), Product_.igstPercentage),
                    buildRangeSpecification(criteria.getSgstPercentage(), Product_.sgstPercentage),
                    buildRangeSpecification(criteria.getTotalPrice(), Product_.totalPrice),
                    buildStringSpecification(criteria.getDescription(), Product_.description),
                    buildStringSpecification(criteria.getLongDescription(), Product_.longDescription),
                    buildSpecification(criteria.getIsVirtual(), Product_.isVirtual),
                    buildSpecification(criteria.getIsVariant(), Product_.isVariant),
                    buildSpecification(criteria.getRequireInventory(), Product_.requireInventory),
                    buildSpecification(criteria.getReturnable(), Product_.returnable),
                    buildSpecification(criteria.getIsPopular(), Product_.isPopular),
                    buildSpecification(criteria.getIsBanner(), Product_.isBanner),
                    buildStringSpecification(criteria.getBannerUrl(), Product_.bannerUrl),
                    buildSpecification(criteria.getProductTypeId(), root ->
                        root.join(Product_.productType, JoinType.LEFT).get(ProductType_.id)
                    ),
                    buildSpecification(criteria.getPrimaryProductCategoryId(), root ->
                        root.join(Product_.primaryProductCategory, JoinType.LEFT).get(ProductCategory_.id)
                    ),
                    buildSpecification(criteria.getVirtualProductId(), root ->
                        root.join(Product_.virtualProduct, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getInventoryItemTypeId(), root ->
                        root.join(Product_.inventoryItemType, JoinType.LEFT).get(InventoryItemType_.id)
                    ),
                    buildSpecification(criteria.getTaxSlabId(), root -> root.join(Product_.taxSlab, JoinType.LEFT).get(TaxSlab_.id)),
                    buildSpecification(criteria.getUomId(), root -> root.join(Product_.uom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getQuantityUomId(), root -> root.join(Product_.quantityUom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getWeightUomId(), root -> root.join(Product_.weightUom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getSizeUomId(), root -> root.join(Product_.sizeUom, JoinType.LEFT).get(Uom_.id))
                )
            );
        }
        return specification;
    }
}
