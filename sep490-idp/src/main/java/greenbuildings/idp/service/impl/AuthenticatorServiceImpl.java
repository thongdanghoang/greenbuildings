package greenbuildings.idp.service.impl;


import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.AttestationObjectConverter;
import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.AuthenticationData;
import com.webauthn4j.data.AuthenticationParameters;
import com.webauthn4j.data.AuthenticationRequest;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.RegistrationData;
import com.webauthn4j.data.RegistrationParameters;
import com.webauthn4j.data.RegistrationRequest;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.verifier.exception.VerificationException;
import greenbuildings.idp.dto.passkeys.CredentialsRegistration;
import greenbuildings.idp.dto.passkeys.CredentialsVerification;
import greenbuildings.idp.entity.UserAuthenticator;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.repository.UserAuthenticatorRepository;
import greenbuildings.idp.service.AuthenticatorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class AuthenticatorServiceImpl implements AuthenticatorService {
    
    private final UserAuthenticatorRepository repository;
    private final HttpServletRequest request;
    private final HttpSession httpSession;
    private final WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
    private final AttestationObjectConverter attestationConverter = new AttestationObjectConverter(new ObjectConverter());
    
    
    public void saveCredentials(CredentialsRegistration registration, UserEntity user) {
        try {
            RegistrationData registrationData = getRegistrationData(registration);
            
            verifyRegistrationData(registrationData);
            
            UserAuthenticator serializedAuthenticator = createSerializedAuthenticator(registration, user);
            repository.save(serializedAuthenticator);
        } catch (DataConversionException | VerificationException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected Error", e);
        }
    }
    
    @Override
    public UserEntity authenticate(CredentialsVerification verification) {
        UserAuthenticator userAuthenticator = repository.findByCredentialId(verification.id()).orElseThrow();
        AttestationObject attestation = attestationConverter.convert(userAuthenticator.getAttestationObject());
        CredentialRecordImpl authenticator = new CredentialRecordImpl(attestation, null, null, null);
        
        AuthenticationRequest authenticationRequest = getAuthenticationRequest(verification);
        AuthenticationParameters authenticationParameters =
                new AuthenticationParameters(buildServerPropertyFromSessionChallenge(), authenticator, null, true, true);
        
        AuthenticationData authenticationData = webAuthnManager.verify(authenticationRequest, authenticationParameters);
        UserEntity user = userAuthenticator.getUser();
        
        checkSignCount(userAuthenticator, authenticationData.getAuthenticatorData().getSignCount());
        return user;
    }
    
    public boolean deleteCredential(UserEntity user, String credentialId) {
        return repository.deleteByCredentialIdAndUser(credentialId, user) > 0;
    }
    
    private void checkSignCount(UserAuthenticator userAuthenticator, long signCount) {
        if (userAuthenticator.getSignCount() != signCount && signCount != 0) {
            log.warn("A cloned authenticator may exist!");
        }
    }
    
    private String getRpId() {
        return UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).build().getHost();
    }
    
    private Origin getOrigin() {
        var origin = UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).replacePath(null).toUriString();
        return new Origin(origin);
    }
    
    private AuthenticationRequest getAuthenticationRequest(CredentialsVerification verification) {
        byte[] clientDataJSON = Base64.getUrlDecoder().decode(verification.response().clientDataJSON());
        byte[] authenticatorData = Base64.getUrlDecoder().decode(verification.response().authenticatorData());
        byte[] signature = Base64.getUrlDecoder().decode(verification.response().signature());
        byte[] userHandle = Base64.getUrlDecoder().decode(verification.response().userHandle());
        
        return new AuthenticationRequest(verification.id().getBytes(), userHandle, authenticatorData, clientDataJSON, null, signature);
    }
    
    private RegistrationData getRegistrationData(CredentialsRegistration registration) throws DataConversionException {
        var registrationRequest = new RegistrationRequest(
                Base64.getUrlDecoder().decode(registration.credentials().response().attestationObject()),
                Base64.getUrlDecoder().decode(registration.credentials().response().clientDataJSON()));
        
        return webAuthnManager.parse(registrationRequest);
    }
    
    private ServerProperty buildServerPropertyFromSessionChallenge() {
        String challenge = httpSession.getAttribute("challenge").toString();
        return new ServerProperty(getOrigin(), getRpId(), new DefaultChallenge(challenge.getBytes()), null);
    }
    
    private void verifyRegistrationData(RegistrationData registrationData) throws VerificationException {
        var pubKeyCredParam = new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256);
        var registrationParameters =
                new RegistrationParameters(buildServerPropertyFromSessionChallenge(), List.of(pubKeyCredParam), false, true);
        
        webAuthnManager.verify(registrationData, registrationParameters);
    }
    
    private UserAuthenticator createSerializedAuthenticator(CredentialsRegistration registration, UserEntity user) {
        return UserAuthenticator.createSerializedAuthenticator(
                registration.credentials().id(),
                user,
                registration.name(),
                Base64.getUrlDecoder().decode(registration.credentials().response().attestationObject()),
                0);
    }
}
