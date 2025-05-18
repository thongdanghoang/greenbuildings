package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.enums.PaymentStatus;
import greenbuildings.enterprise.dtos.NumberOfTransactionByType;
import greenbuildings.enterprise.dtos.PaymentRevenueByQuarter;
import greenbuildings.enterprise.dtos.RevenueReportDTO;
import greenbuildings.enterprise.enums.TransactionType;
import greenbuildings.enterprise.repositories.PaymentRepository;
import greenbuildings.enterprise.repositories.TransactionRepository;
import greenbuildings.enterprise.services.RevenueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Throwable.class)
public class RevenueServiceImpl implements RevenueService {
    
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    
    @Override
    public RevenueReportDTO generateReport() {
        long numberOfPaidPayment = paymentRepository.countByStatus(PaymentStatus.PAID);
        BigDecimal totalRevenue = paymentRepository.totalOfPaidPayments();
        long numberOfTransactions = transactionRepository.count();
        Map<String, PaymentRevenueByQuarter> revenueByQuarter = getRevenueByQuarter();
        
        Map<TransactionType, NumberOfTransactionByType> numberOfTransactionByTypeMap = getNOTransactionByType();
        
        return new RevenueReportDTO(numberOfPaidPayment,
                                    totalRevenue,
                                    numberOfTransactions,
                                    revenueByQuarter,
                                    numberOfTransactionByTypeMap);
    }
    
    private Map<TransactionType, NumberOfTransactionByType> getNOTransactionByType() {
        Map<TransactionType, NumberOfTransactionByType> rs = new LinkedHashMap<>();
        rs.put(TransactionType.NEW_PURCHASE, new NumberOfTransactionByType(TransactionType.NEW_PURCHASE, 0));
        rs.put(TransactionType.UPGRADE, new NumberOfTransactionByType(TransactionType.UPGRADE, 0));
        
        transactionRepository.countByTransactionType().forEach(t -> {
            rs.put(t.getType(), t);
        });
        return rs;
    }
    
    private Map<String, PaymentRevenueByQuarter> getRevenueByQuarter() {
        Map<String, PaymentRevenueByQuarter> rs = new LinkedHashMap<>();
        rs.put("Q1", new PaymentRevenueByQuarter(1, 0));
        rs.put("Q2", new PaymentRevenueByQuarter(2, 0));
        rs.put("Q3", new PaymentRevenueByQuarter(3, 0));
        rs.put("Q4", new PaymentRevenueByQuarter(4, 0));
        
        paymentRepository.findRevenueByQuarter().forEach(p -> {
            String key = "Q" + p.getQuarter();
            rs.put(key, p);
        });
        
        return rs;
    }
    
}
