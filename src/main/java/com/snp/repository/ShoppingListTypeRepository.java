package com.snp.repository;

import com.snp.domain.ShoppingListType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShoppingListType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingListTypeRepository extends JpaRepository<ShoppingListType, Long>, JpaSpecificationExecutor<ShoppingListType> {}
