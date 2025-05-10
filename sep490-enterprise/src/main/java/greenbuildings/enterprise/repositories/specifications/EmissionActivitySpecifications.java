package greenbuildings.enterprise.repositories.specifications;

import commons.springfw.impl.entities.AbstractBaseEntity;
import greenbuildings.enterprise.dtos.emission_activities.ActivityCriteria;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class EmissionActivitySpecifications {
    
    private static final String LIKE = "%";
    
    public static Specification<EmissionActivityEntity> withFilters(UUID enterpriseId, ActivityCriteria criteria) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            
            predicates.add(
                    cb.equal(root.get(EmissionActivityEntity.Fields.building).get(BuildingEntity.Fields.enterprise).get(AbstractBaseEntity.Fields.id),
                             enterpriseId)
                          );
            if(Objects.nonNull(criteria)) {
                if (!CollectionUtils.isEmpty(criteria.buildings())) {
                    predicates.add(root.get(EmissionActivityEntity.Fields.building).get(AbstractBaseEntity.Fields.id).in(criteria.buildings()));
                }
                if (!CollectionUtils.isEmpty(criteria.factors())) {
                    predicates.add(root.get(EmissionActivityEntity.Fields.emissionFactorEntity).get(AbstractBaseEntity.Fields.id).in(criteria.factors()));
                }
                if (StringUtils.isNotBlank(criteria.name())) {
                    predicates.add(cb.like(cb.lower(root.get(EmissionActivityEntity.Fields.name)), LIKE + criteria.name().toLowerCase() + LIKE));
                }
                if (StringUtils.isNotBlank(criteria.category())) {
                    predicates.add(cb.like(cb.lower(root.get(EmissionActivityEntity.Fields.category)), LIKE + criteria.category().toLowerCase() + LIKE));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
