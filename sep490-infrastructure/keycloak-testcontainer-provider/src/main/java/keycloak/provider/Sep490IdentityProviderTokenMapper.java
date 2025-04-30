package keycloak.provider;

import com.google.auto.service.AutoService;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.ProtocolMapper;
import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAccessTokenMapper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.AccessToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AutoService(ProtocolMapper.class)
public class Sep490IdentityProviderTokenMapper extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper {
    private static final String ID = "green-buildings-mapper";
    private static final String LABEL = "Testcontainer mapper";
    
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String PERMISSIONS_CLAIM = "permissions";
    
    private final List<ProviderConfigProperty> configProperties = new ArrayList<>();
    
    private void registerClaim(AccessToken token, String label, Object claim) {
        token.getOtherClaims().put(label, claim);
    }
    
    @Override
    public AccessToken transformAccessToken(AccessToken token,
                                            ProtocolMapperModel mappingModel,
                                            KeycloakSession session,
                                            UserSessionModel userSession,
                                            ClientSessionContext clientSessionCtx) {
        setClaim(token, mappingModel, userSession, session, clientSessionCtx);
        var email = token.getOtherClaims().get("email").toString();
        registerClaim(token, PERMISSIONS_CLAIM, Collections.emptyList()); // TODO: put mock permissions here
        if (email.startsWith("system.admin")) {
            registerClaim(token, AUTHORITIES_CLAIM, List.of("ROLE_SYSTEM_ADMIN"));
            registerClaim(token, PERMISSIONS_CLAIM, List.of());
            return token;
        }
        if (email.startsWith("core")) {
            registerClaim(token, AUTHORITIES_CLAIM, List.of("ROLE_BASIC_USER"));
            registerClaim(token, PERMISSIONS_CLAIM, List.of());
            return token;
        }
        if (email.startsWith("enterprise.owner")) {
            registerClaim(token, AUTHORITIES_CLAIM, List.of("ROLE_ENTERPRISE_OWNER"));
            registerClaim(token, PERMISSIONS_CLAIM, List.of("ENTERPRISE_OWNER:664748fa-1312-4456-a88c-1ef187ec9510"));
            return token;
        }
        if (email.startsWith("tenant")) {
            registerClaim(token, AUTHORITIES_CLAIM, List.of("ROLE_TENANT"));
            registerClaim(token, PERMISSIONS_CLAIM, "TENANT:5ad9bee9-1281-46d7-984f-8ab33ee8d12a");
            return token;
        }
        registerClaim(token, PERMISSIONS_CLAIM, UUID.randomUUID().toString());
        return token;
    }
    
    /*
     * MAPPER BOILERPLATE
     */
    @Override
    public String getDisplayCategory() {
        return LABEL;
    }
    
    @Override
    public String getDisplayType() {
        return getDisplayCategory();
    }
    
    @Override
    public String getId() {
        return ID;
    }
    
    @Override
    public String getHelpText() {
        return getDisplayCategory();
    }
    
    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }
    
    @Override
    public int getPriority() {
        return 1000;
    }
    
}
