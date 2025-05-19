package greenbuildings.idp.repository;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.idp.entity.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends AbstractBaseRepository<UserEntity> {
    
    boolean existsByEmail(String email);
    
    Optional<UserEntity> findByEmail(String email);
    
    @EntityGraph(UserEntity.WITH_ENTERPRISE_PERMISSIONS_ENTITY_GRAPH)
    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.id = :id
            """)
    Optional<UserEntity> findByIdWithBuildingPermissions(UUID id);
    
    @EntityGraph(UserEntity.WITH_ENTERPRISE_PERMISSIONS_ENTITY_GRAPH)
    @Query("""
                SELECT u
                FROM UserEntity u
                WHERE u.id IN (:ids)
            """)
    List<UserEntity> findByIDsWithPermissions(Set<UUID> ids);
    
    @Query("""
            SELECT u.id
            FROM UserEntity u
            WHERE (LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
            """
    )
    Page<UUID> findByEmail(String email, Pageable pageable);
    
    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.id IN :ids
            """
    )
    List<UserEntity> findByIDs(Set<UUID> ids);
    
}
