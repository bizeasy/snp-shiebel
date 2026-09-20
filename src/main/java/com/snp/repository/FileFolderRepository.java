package com.snp.repository;

import com.snp.domain.FileFolder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FileFolder entity.
 */
@Repository
public interface FileFolderRepository extends JpaRepository<FileFolder, Long>, JpaSpecificationExecutor<FileFolder> {
    default Optional<FileFolder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FileFolder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FileFolder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select fileFolder from FileFolder fileFolder left join fetch fileFolder.parent",
        countQuery = "select count(fileFolder) from FileFolder fileFolder"
    )
    Page<FileFolder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select fileFolder from FileFolder fileFolder left join fetch fileFolder.parent")
    List<FileFolder> findAllWithToOneRelationships();

    @Query("select fileFolder from FileFolder fileFolder left join fetch fileFolder.parent where fileFolder.id =:id")
    Optional<FileFolder> findOneWithToOneRelationships(@Param("id") Long id);
}
