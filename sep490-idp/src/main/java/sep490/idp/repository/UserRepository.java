package sep490.idp.repository;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.idp.entity.UserEntity;

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
            WHERE u.email = :email
            """)
    Optional<UserEntity> findByEmailWithBuildingPermissions(String email);
    
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
            WHERE u.enterprise.role <> sep490.common.api.security.UserRole.ENTERPRISE_OWNER
            AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')))
            """
    )
    Page<UUID> findByName(String name, Pageable pageable);
    
    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.id IN :ids
            """
    )
    List<UserEntity> findByIDs(Set<UUID> ids);
}
