package com.snp.repository;

import com.snp.domain.InvoiceRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceRole entity.
 */
@Repository
public interface InvoiceRoleRepository extends JpaRepository<InvoiceRole, Long>, JpaSpecificationExecutor<InvoiceRole> {
    default Optional<InvoiceRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InvoiceRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InvoiceRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select invoiceRole from InvoiceRole invoiceRole left join fetch invoiceRole.roleType",
        countQuery = "select count(invoiceRole) from InvoiceRole invoiceRole"
    )
    Page<InvoiceRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select invoiceRole from InvoiceRole invoiceRole left join fetch invoiceRole.roleType")
    List<InvoiceRole> findAllWithToOneRelationships();

    @Query("select invoiceRole from InvoiceRole invoiceRole left join fetch invoiceRole.roleType where invoiceRole.id =:id")
    Optional<InvoiceRole> findOneWithToOneRelationships(@Param("id") Long id);
}
