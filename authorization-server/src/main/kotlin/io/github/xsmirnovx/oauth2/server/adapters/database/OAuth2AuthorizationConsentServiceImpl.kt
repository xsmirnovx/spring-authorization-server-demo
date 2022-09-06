package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.AuthorizationConsent
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class OAuth2AuthorizationConsentServiceImpl(
    val authorizationConsentRepository: JpaAuthorizationConsentRepository,
) : OAuth2AuthorizationConsentService {

    override fun save(authorizationConsent: OAuth2AuthorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null")
        authorizationConsentRepository.save(AuthorizationConsent.fromDomain(authorizationConsent))
    }

    override fun remove(authorizationConsent: OAuth2AuthorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null")
        authorizationConsentRepository
            .deleteByRegisteredClientIdAndPrincipalName(
                authorizationConsent.registeredClientId,
                authorizationConsent.principalName
            )
    }

    override fun findById(registeredClientId: String, principalName: String): OAuth2AuthorizationConsent? {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty")
        Assert.hasText(principalName, "principalName cannot be empty")
        return authorizationConsentRepository
            .findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
            .map(AuthorizationConsent::toDomain)
            .orElse(null)
    }
}
