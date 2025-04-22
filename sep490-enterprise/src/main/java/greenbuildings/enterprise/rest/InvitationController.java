package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationController {
    
    private final InvitationService invitationService;
    
    
}
