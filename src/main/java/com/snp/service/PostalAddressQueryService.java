package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PostalAddress;
import com.snp.repository.PostalAddressRepository;
import com.snp.service.criteria.PostalAddressCriteria;
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
 * Service for executing complex queries for {@link PostalAddress} entities in the database.
 * The main input is a {@link PostalAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PostalAddress} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostalAddressQueryService extends QueryService<PostalAddress> {

    private static final Logger LOG = LoggerFactory.getLogger(PostalAddressQueryService.class);

    private final PostalAddressRepository postalAddressRepository;

    public PostalAddressQueryService(PostalAddressRepository postalAddressRepository) {
        this.postalAddressRepository = postalAddressRepository;
    }

    /**
     * Return a {@link Page} of {@link PostalAddress} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PostalAddress> findByCriteria(PostalAddressCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PostalAddress> specification = createSpecification(criteria);
        return postalAddressRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostalAddressCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PostalAddress> specification = createSpecification(criteria);
        return postalAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link PostalAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PostalAddress> createSpecification(PostalAddressCriteria criteria) {
        Specification<PostalAddress> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PostalAddress_.contactMech, JoinType.LEFT);
                root.fetch(PostalAddress_.state, JoinType.LEFT);
                root.fetch(PostalAddress_.pincode, JoinType.LEFT);
                root.fetch(PostalAddress_.country, JoinType.LEFT);
                root.fetch(PostalAddress_.geoPoint, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PostalAddress_.id),
                    buildStringSpecification(criteria.getToName(), PostalAddress_.toName),
                    buildStringSpecification(criteria.getAddress1(), PostalAddress_.address1),
                    buildStringSpecification(criteria.getAddress2(), PostalAddress_.address2),
                    buildStringSpecification(criteria.getCity(), PostalAddress_.city),
                    buildStringSpecification(criteria.getLandmark(), PostalAddress_.landmark),
                    buildStringSpecification(criteria.getPostalCode(), PostalAddress_.postalCode),
                    buildSpecification(criteria.getIsDefault(), PostalAddress_.isDefault),
                    buildStringSpecification(criteria.getCustomAddressType(), PostalAddress_.customAddressType),
                    buildStringSpecification(criteria.getStateStr(), PostalAddress_.stateStr),
                    buildStringSpecification(criteria.getCountryStr(), PostalAddress_.countryStr),
                    buildSpecification(criteria.getIsIndianAddress(), PostalAddress_.isIndianAddress),
                    buildStringSpecification(criteria.getNote(), PostalAddress_.note),
                    buildStringSpecification(criteria.getDirections(), PostalAddress_.directions),
                    buildSpecification(criteria.getContactMechId(), root ->
                        root.join(PostalAddress_.contactMech, JoinType.LEFT).get(ContactMech_.id)
                    ),
                    buildSpecification(criteria.getStateId(), root -> root.join(PostalAddress_.state, JoinType.LEFT).get(Geo_.id)),
                    buildSpecification(criteria.getPincodeId(), root -> root.join(PostalAddress_.pincode, JoinType.LEFT).get(Geo_.id)),
                    buildSpecification(criteria.getCountryId(), root -> root.join(PostalAddress_.country, JoinType.LEFT).get(Geo_.id)),
                    buildSpecification(criteria.getGeoPointId(), root ->
                        root.join(PostalAddress_.geoPoint, JoinType.LEFT).get(GeoPoint_.id)
                    )
                )
            );
        }
        return specification;
    }
}
