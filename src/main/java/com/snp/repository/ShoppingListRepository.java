package com.snp.repository;

import com.snp.domain.ShoppingList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShoppingList entity.
 */
@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long>, JpaSpecificationExecutor<ShoppingList> {
    default Optional<ShoppingList> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShoppingList> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShoppingList> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select shoppingList from ShoppingList shoppingList left join fetch shoppingList.shoppingListType left join fetch shoppingList.status",
        countQuery = "select count(shoppingList) from ShoppingList shoppingList"
    )
    Page<ShoppingList> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select shoppingList from ShoppingList shoppingList left join fetch shoppingList.shoppingListType left join fetch shoppingList.status"
    )
    List<ShoppingList> findAllWithToOneRelationships();

    @Query(
        "select shoppingList from ShoppingList shoppingList left join fetch shoppingList.shoppingListType left join fetch shoppingList.status where shoppingList.id =:id"
    )
    Optional<ShoppingList> findOneWithToOneRelationships(@Param("id") Long id);
}
