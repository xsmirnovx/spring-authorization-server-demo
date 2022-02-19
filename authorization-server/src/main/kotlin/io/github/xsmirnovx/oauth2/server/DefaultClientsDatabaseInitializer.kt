package io.github.xsmirnovx.oauth2.server

import io.github.xsmirnovx.oauth2.server.domain.RegisteredClientEntity
import io.github.xsmirnovx.oauth2.server.repository.RegisteredClientJpaRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import org.springframework.stereotype.Component

@Component
class DefaultClientsDatabaseInitializer(
    val registeredClientJpaRepository: RegisteredClientJpaRepository,
    val passwordEncoder: PasswordEncoder,
    val tokenSettings: TokenSettings,

    ) : ApplicationListener<ApplicationReadyEvent?> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        registeredClientJpaRepository.saveAll(defaultClients)
    }

    private val defaultClients: Set<RegisteredClientEntity>
        get() {

            val authCodePkceClient = RegisteredClientEntity(
                clientId = "demo-auth-code-pkce-client",
                clientName = "demo-auth-code-pkce-client",
                clientSecret = passwordEncoder.encode("123456"),
                clientAuthenticationMethods = setOf(ClientAuthenticationMethod.NONE),
                authorizationGrantTypes = setOf(
                    AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.REFRESH_TOKEN
                ),
                clientSettings = ClientSettings.builder().requireProofKey(true).build(),
                redirectUris = setOf("https://oidcdebugger.com/debug"),
                tokenSettings = tokenSettings,
                scopes = setOf(OidcScopes.OPENID)
            )

//            val authCodeClient = RegisteredClientEntity(
//                clientId = "demo-auth-code-client",
//                clientName = "demo-auth-code-client",
//                clientSecret = passwordEncoder.encode("123456"),
//                clientAuthenticationMethods = listOf(ClientAuthenticationMethod.CLIENT_SECRET_POST),
//                authorizationGrantTypes = listOf(
//                    AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.REFRESH_TOKEN
//                ),
//                redirectUris = setOf("https://oidcdebugger.com/debug"),
//                scopes = setOf(OidcScopes.OPENID),
//                tokenSettings = tokenSettings
//            )
//
//            val clientCredentialsClient = RegisteredClientEntity(
//                clientId = "jira-plugin",
//                clientName = "jira-plugin",
//                clientSecret = "{noop}123456",
//                clientAuthenticationMethods = listOf(ClientAuthenticationMethod.CLIENT_SECRET_POST),
//                authorizationGrantTypes = listOf(AuthorizationGrantType.CLIENT_CREDENTIALS),
//                tokenSettings = tokenSettings
//            )

            return setOf(authCodePkceClient)
        }
}