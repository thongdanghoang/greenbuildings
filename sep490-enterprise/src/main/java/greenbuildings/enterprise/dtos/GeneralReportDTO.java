package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.EmissionUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GeneralReportDTO {
    String enterpriseName;
    String enterpriseEmail;
    String enterpriseAddress;
    String enterpriseHotline;
    List<BuildingDTO> buildings = new ArrayList<>();
    
    @Setter
    @Getter
    public class BuildingDTO {
        String name;
        String address;
        List<BuildingGroupDTO> buildingGroups = new ArrayList<>();
        
        @NoArgsConstructor
        @Setter
        @Getter
        public class BuildingGroupDTO {
            String name;
            List<ActivityDTO> activities = new ArrayList<>();
            
            @Setter
            @Getter
            public class ActivityDTO {
                String name;
                String category;
                String type;
                String description;
                List<RecordDTO> records = new ArrayList<>();
                
                @Setter
                @Getter
                public class RecordDTO {
                    BigDecimal value;
                    int quantity;
                    EmissionUnit unit;
                    LocalDate startDate;
                    LocalDate endDate;
                    BigDecimal ghg;
                }
            }
        }
    }
    
}
