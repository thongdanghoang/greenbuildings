package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends AbstractBaseRepository<SubscriptionEntity> {
    
    @Query(value = """
                select s from SubscriptionEntity s
                    where s.building.id = :buildingId
                        and s.startDate <= :now
                        and s.endDate >= :now
            """)
    List<SubscriptionEntity> findAllValidSubscriptions(@Param("now") LocalDate now, @Param("buildingId")UUID buildingId);
    
}
