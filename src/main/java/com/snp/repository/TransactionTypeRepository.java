package com.snp.repository;

import com.snp.domain.TransactionType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransactionType entity.
 */
@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long>, JpaSpecificationExecutor<TransactionType> {
    default Optional<TransactionType> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TransactionType> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TransactionType> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select transactionType from TransactionType transactionType left join fetch transactionType.category",
        countQuery = "select count(transactionType) from TransactionType transactionType"
    )
    Page<TransactionType> findAllWithToOneRelationships(Pageable pageable);

    @Query("select transactionType from TransactionType transactionType left join fetch transactionType.category")
    List<TransactionType> findAllWithToOneRelationships();

    @Query(
        "select transactionType from TransactionType transactionType left join fetch transactionType.category where transactionType.id =:id"
    )
    Optional<TransactionType> findOneWithToOneRelationships(@Param("id") Long id);
}
