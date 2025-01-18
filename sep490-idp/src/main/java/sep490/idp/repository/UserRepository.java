package sep490.idp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.idp.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    
    boolean existsByEmail(String email);
    
    Optional<UserEntity> findByEmail(String email);
    
    @EntityGraph(UserEntity.USER_PERMISSIONS_ENTITY_GRAPH)
    @Query(value = """
            SELECT u
            FROM UserEntity u
            WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
            OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<UserEntity> searchByName(String name, Pageable pageable);
}
