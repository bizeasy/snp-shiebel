package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ContactMech;
import com.snp.repository.ContactMechRepository;
import com.snp.service.criteria.ContactMechCriteria;
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
 * Service for executing complex queries for {@link ContactMech} entities in the database.
 * The main input is a {@link ContactMechCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContactMech} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactMechQueryService extends QueryService<ContactMech> {

    private static final Logger LOG = LoggerFactory.getLogger(ContactMechQueryService.class);

    private final ContactMechRepository contactMechRepository;

    public ContactMechQueryService(ContactMechRepository contactMechRepository) {
        this.contactMechRepository = contactMechRepository;
    }

    /**
     * Return a {@link Page} of {@link ContactMech} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactMech> findByCriteria(ContactMechCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactMech> specification = createSpecification(criteria);
        return contactMechRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactMechCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ContactMech> specification = createSpecification(criteria);
        return contactMechRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactMechCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactMech> createSpecification(ContactMechCriteria criteria) {
        Specification<ContactMech> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ContactMech_.contactMechType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ContactMech_.id),
                    buildStringSpecification(criteria.getInfoString(), ContactMech_.infoString),
                    buildSpecification(criteria.getContactMechTypeId(), root ->
                        root.join(ContactMech_.contactMechType, JoinType.LEFT).get(ContactMechType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
