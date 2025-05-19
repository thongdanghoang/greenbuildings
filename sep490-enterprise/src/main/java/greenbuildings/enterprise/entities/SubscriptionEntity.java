package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionEntity extends AbstractAuditableEntity {
    
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "building_id")
    private BuildingEntity building;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "max_number_of_devices")
    private int maxNumberOfDevices;
    
    @OneToMany(fetch = FetchType.LAZY,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
               mappedBy = "subscription")
    private List<TransactionEntity> transaction = new ArrayList<>();
    
    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate)
               && !now.isAfter(endDate);
    }
    
    public void addTransaction(TransactionEntity transaction) {
        if (transaction != null) {
            this.transaction.add(transaction);
            transaction.setSubscription(this);
        }
    }
}
