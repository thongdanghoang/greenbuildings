package enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import enterprise.entities.WalletEntity;
import enterprise.repositories.WalletRepository;
import enterprise.services.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackOn = Throwable.class)
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walRepo;
    
    @Override
    public Long getBalance(){
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        return walRepo
                .findByEnterpriseId(enterpriseId)
                .map(WalletEntity::getBalance)
                .orElseThrow();
    }
    
}
