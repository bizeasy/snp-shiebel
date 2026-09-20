package com.snp.repository;

import com.snp.domain.CashBook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CashBook entity.
 */
@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Long>, JpaSpecificationExecutor<CashBook> {
    default Optional<CashBook> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CashBook> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CashBook> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cashBook from CashBook cashBook left join fetch cashBook.transactionType left join fetch cashBook.transactionCategory left join fetch cashBook.paymentMethodType left join fetch cashBook.department left join fetch cashBook.currencyUom left join fetch cashBook.status",
        countQuery = "select count(cashBook) from CashBook cashBook"
    )
    Page<CashBook> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cashBook from CashBook cashBook left join fetch cashBook.transactionType left join fetch cashBook.transactionCategory left join fetch cashBook.paymentMethodType left join fetch cashBook.department left join fetch cashBook.currencyUom left join fetch cashBook.status"
    )
    List<CashBook> findAllWithToOneRelationships();

    @Query(
        "select cashBook from CashBook cashBook left join fetch cashBook.transactionType left join fetch cashBook.transactionCategory left join fetch cashBook.paymentMethodType left join fetch cashBook.department left join fetch cashBook.currencyUom left join fetch cashBook.status where cashBook.id =:id"
    )
    Optional<CashBook> findOneWithToOneRelationships(@Param("id") Long id);
}
