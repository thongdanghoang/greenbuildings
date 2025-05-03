package greenbuildings.enterprise.services;


import java.util.UUID;

public interface WalletService {
    double getBalance(UUID enterpriseId);
}
