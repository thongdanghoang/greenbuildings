package commons.springfw.impl.audit;

import commons.springfw.impl.securities.UserContextData;
import commons.springfw.impl.utils.SecurityUtils;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    
    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityUtils.getUserContextData()
                            .map(UserContextData::getEmail)
                            .or(() -> Optional.of("Unknown"));
    }
}
