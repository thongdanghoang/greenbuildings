package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.enterprise.repositories.WalletRepository;
import greenbuildings.enterprise.services.WalletService;
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
    public double getBalance(){
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        return walRepo
                .findByEnterpriseId(enterpriseId)
                .getBalance();
    }
    
}
