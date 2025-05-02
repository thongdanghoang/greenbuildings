package greenbuildings.enterprise.services;

import greenbuildings.enterprise.dtos.CreditConvertRatioDTO;
import greenbuildings.enterprise.dtos.SubscribeRequestDTO;
import greenbuildings.enterprise.entities.CreditConvertRatioEntity;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    List<CreditConvertRatioEntity> getCreditConvertRatios();
    
    void subscribe(@Valid SubscribeRequestDTO request, UUID enterpriseId);

    CreditConvertRatioEntity getCreditConvertRatioDetail(UUID id);

    void updateCreditConvertRatio(CreditConvertRatioDTO creditConvertRatioDTO);
}
