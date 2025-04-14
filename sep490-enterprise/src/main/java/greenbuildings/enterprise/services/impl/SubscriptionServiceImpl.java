package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.enterprise.dtos.CreditConvertRatioDTO;
import greenbuildings.enterprise.dtos.SubscribeRequestDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.CreditConvertRatioEntity;
import greenbuildings.enterprise.enums.CreditConvertType;
import greenbuildings.enterprise.entities.SubscriptionEntity;
import greenbuildings.enterprise.entities.TransactionEntity;
import greenbuildings.enterprise.enums.TransactionType;
import greenbuildings.enterprise.entities.WalletEntity;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.CreditConvertRatioRepository;
import greenbuildings.enterprise.repositories.SubscriptionRepository;
import greenbuildings.enterprise.repositories.WalletRepository;
import greenbuildings.enterprise.services.SubscriptionService;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    
    private final CreditConvertRatioRepository creditConvertRatioRepository;
    private final BuildingRepository buildingRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WalletRepository walletRepository;
    
    @Override
    public List<CreditConvertRatioEntity> getCreditConvertRatios() {
        return creditConvertRatioRepository.findAll();
    }
    
    @Override
    public void subscribe(SubscribeRequestDTO request) {
        // Prepare building
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        BuildingEntity buildingEntity = buildingRepository.findByIdAndEnterpriseId(request.buildingId(), enterpriseId).orElseThrow();
        
        // Prepare Transaction
        double amount = calculateTransactionAmount(request);
        TransactionEntity transaction = createNewTransaction(request, buildingEntity, amount);
        
        // Handel wallet
        WalletEntity walletEntity = walletRepository.findByEnterpriseId(enterpriseId);
        try {
            walletEntity.withdraw((long) transaction.getAmount());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(null, "You have not enough credit");
        }
        
        // Handel Subscription
        SubscriptionEntity subscription = null;
        if (request.type().equals(TransactionType.NEW_PURCHASE)) {
            subscription = createNewSubscription(transaction, buildingEntity, request);
        } else if (request.type().equals(TransactionType.UPGRADE)) {
            subscription = upgradeSubscription(transaction, request);
        } else {
            throw new TechnicalException("Unknown transaction type: " + request.type());
        }
        
        walletRepository.save(walletEntity);
        subscriptionRepository.save(subscription);
    }
    
    private double calculateTransactionAmount(SubscribeRequestDTO request) {
        List<CreditConvertRatioEntity> creditConvertRatios = getCreditConvertRatios();
        CreditConvertRatioEntity monthRatio = creditConvertRatios
                .stream()
                .filter(x -> x.getConvertType().equals(CreditConvertType.MONTH))
                .findFirst()
                .orElseThrow();
        CreditConvertRatioEntity noDevicesRatio = creditConvertRatios
                .stream()
                .filter(x -> x.getConvertType().equals(CreditConvertType.DEVICE))
                .findFirst()
                .orElseThrow();
        
        if (request.monthRatio() != monthRatio.getRatio() || request.deviceRatio() != noDevicesRatio.getRatio()) {
            throw new BusinessException("", "validation.business.buildings.ratioNotMatch");
        }
        if (request.type() == TransactionType.NEW_PURCHASE) {
            return calculateTransactionAmountForNew(request, monthRatio, noDevicesRatio);
        } else {
            return calculateTransactionAmountForUpdate(request, monthRatio, noDevicesRatio);
        }
    }
    
    private double calculateTransactionAmountForUpdate(SubscribeRequestDTO requestDTO, CreditConvertRatioEntity monthRatio,
                                                       CreditConvertRatioEntity noDevicesRatio) {
        double months = requestDTO.months();
        double numberOfDevices = requestDTO.numberOfDevices();
        SubscriptionEntity subscription = subscriptionRepository.findAllValidSubscriptions(LocalDate.now(), requestDTO.buildingId())
                                                                .stream()
                                                                .findFirst()
                                                                .orElseThrow();
        
        if (months == 0 && numberOfDevices == 0) {
            throw new BusinessException(null, "validation.business.buildings.ratioNotMatch");
        } else if (months > 0 && numberOfDevices == 0) {
            double maxNumberOfDevices = subscription.getMaxNumberOfDevices();
            return months * monthRatio.getRatio() * maxNumberOfDevices * noDevicesRatio.getRatio();
        } else if (numberOfDevices > 0 && months == 0) {
            long numberOfLeftDays = 0;
            LocalDate endDate = subscription.getEndDate();
            LocalDate currentDate = LocalDate.now();
            numberOfLeftDays = ChronoUnit.DAYS.between(currentDate, endDate);
            return numberOfDevices * noDevicesRatio.getRatio() * numberOfLeftDays * (monthRatio.getRatio() / 30);
        }
        LocalDate endDate = subscription.getEndDate();
        LocalDate currentDate = LocalDate.now();
        long numberOfLeftDays = ChronoUnit.DAYS.between(currentDate, endDate);
        double oldTotal = numberOfDevices * noDevicesRatio.getRatio() * numberOfLeftDays * (monthRatio.getRatio() / 30);
        double newTotal = (numberOfDevices + subscription.getMaxNumberOfDevices()) * noDevicesRatio.getRatio() * months * monthRatio.getRatio();
        
        return (int) oldTotal + newTotal;
    }
    
    private TransactionEntity createNewTransaction(SubscribeRequestDTO request, BuildingEntity buildingEntity, double amount) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setBuilding(buildingEntity);
        transaction.setEnterprise(buildingEntity.getEnterprise());
        transaction.setTransactionType(request.type());
        transaction.setAmount(amount);
        transaction.setMonths(request.months());
        transaction.setNumberOfDevices(request.numberOfDevices());
        return transaction;
    }
    
    private SubscriptionEntity createNewSubscription(TransactionEntity transaction, BuildingEntity buildingEntity, SubscribeRequestDTO request) {
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setBuilding(buildingEntity);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(request.months()));
        subscription.setMaxNumberOfDevices(request.numberOfDevices());
        subscription.addTransaction(transaction);
        
        return subscription;
    }
    
    private SubscriptionEntity upgradeSubscription(TransactionEntity transaction, SubscribeRequestDTO request) {
        List<SubscriptionEntity> allValidSubscriptions = subscriptionRepository.findAllValidSubscriptions(LocalDate.now(), request.buildingId());
        if (allValidSubscriptions.isEmpty()) {
            throw new BusinessException(null, "You do not have any valid subscriptions");
        } else if (allValidSubscriptions.size() > 1) {
            log.warn("Building {} has more than one valid subscriptions", request.buildingId());
        }
        SubscriptionEntity subscription = allValidSubscriptions.getFirst();
        subscription.setMaxNumberOfDevices(subscription.getMaxNumberOfDevices() + request.numberOfDevices());
        subscription.setEndDate(subscription.getEndDate().plusMonths(request.months()));
        subscription.addTransaction(transaction);
        return subscription;
    }
    
    private double calculateTransactionAmountForNew(SubscribeRequestDTO request, CreditConvertRatioEntity monthRatio, CreditConvertRatioEntity noDevicesRatio) {
        return (monthRatio.getRatio() * request.months())
               * (noDevicesRatio.getRatio() * request.numberOfDevices());
    }
    
    @Override
    public CreditConvertRatioEntity getCreditConvertRatioDetail(UUID id) {
        return creditConvertRatioRepository.findById(id).orElseThrow();
    }
    
    public void updateCreditConvertRatio(CreditConvertRatioDTO creditConvertRatioDTO) {
        var creditConvertRatioEntity = getCreditConvertRatioDetail(creditConvertRatioDTO.id());
        creditConvertRatioEntity.setRatio(creditConvertRatioDTO.ratio());
        creditConvertRatioRepository.save(creditConvertRatioEntity);
    }
}
