package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.EmissionActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class EmissionActivityServiceImpl implements EmissionActivityService {

    private final EmissionActivityRepository emissionActivityRepository;


}
