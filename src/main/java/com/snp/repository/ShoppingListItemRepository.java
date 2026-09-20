package com.snp.repository;

import com.snp.domain.ShoppingListItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShoppingListItem entity.
 */
@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long>, JpaSpecificationExecutor<ShoppingListItem> {
    default Optional<ShoppingListItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShoppingListItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShoppingListItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select shoppingListItem from ShoppingListItem shoppingListItem left join fetch shoppingListItem.product left join fetch shoppingListItem.status",
        countQuery = "select count(shoppingListItem) from ShoppingListItem shoppingListItem"
    )
    Page<ShoppingListItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select shoppingListItem from ShoppingListItem shoppingListItem left join fetch shoppingListItem.product left join fetch shoppingListItem.status"
    )
    List<ShoppingListItem> findAllWithToOneRelationships();

    @Query(
        "select shoppingListItem from ShoppingListItem shoppingListItem left join fetch shoppingListItem.product left join fetch shoppingListItem.status where shoppingListItem.id =:id"
    )
    Optional<ShoppingListItem> findOneWithToOneRelationships(@Param("id") Long id);
}
