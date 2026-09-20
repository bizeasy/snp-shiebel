package com.snp.repository;

import com.snp.domain.FileShare;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FileShare entity.
 */
@Repository
public interface FileShareRepository extends JpaRepository<FileShare, Long>, JpaSpecificationExecutor<FileShare> {
    default Optional<FileShare> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FileShare> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FileShare> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select fileShare from FileShare fileShare left join fetch fileShare.folder left join fetch fileShare.attachment",
        countQuery = "select count(fileShare) from FileShare fileShare"
    )
    Page<FileShare> findAllWithToOneRelationships(Pageable pageable);

    @Query("select fileShare from FileShare fileShare left join fetch fileShare.folder left join fetch fileShare.attachment")
    List<FileShare> findAllWithToOneRelationships();

    @Query(
        "select fileShare from FileShare fileShare left join fetch fileShare.folder left join fetch fileShare.attachment where fileShare.id =:id"
    )
    Optional<FileShare> findOneWithToOneRelationships(@Param("id") Long id);
}
