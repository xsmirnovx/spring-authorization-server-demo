package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.RegisteredClient
import io.github.xsmirnovx.oauth2.server.configuration.RegisteredClientsProperties
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
    val registeredClientEntityRepository: JpaRegisteredClientRepository,
    val passwordEncoder: PasswordEncoder,
    val tokenSettings: TokenSettings,
    val registeredClientsProperties: RegisteredClientsProperties) : ApplicationListener<ApplicationReadyEvent?> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {

        defaultClients.forEach {
            it.clientId?.let {
                clientId -> registeredClientEntityRepository
                                .findByClientId(clientId)
                                .ifPresentOrElse( {}, { registeredClientEntityRepository.save(it) })
            }
        }
//        registeredClientsProperties.clients?.values?.stream()
//            ?.map { it.toRegisteredClient(passwordEncoder::encode) }
//           /// ?.map { it.tokenSettings = tokenSettings }
//            ?.map { it.toEntity() }
//            ?.forEach {
//                it
//                    .clientId?.let { clientId ->
//                        registeredClientEntityRepository
//                            .findByClientId(clientId)
//                            .ifPresentOrElse({}, { registeredClientEntityRepository.save(it) })
//                    }
//            }
    }

    private val defaultClients: Set<RegisteredClient>
        get() {

            val authCodePkceClient = RegisteredClient(
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

            val authCodeClient = RegisteredClient(
                clientId = "demo-auth-code-client",
                clientName = "demo-auth-code-client",
                clientSecret = passwordEncoder.encode("123456"),
                clientAuthenticationMethods = setOf(ClientAuthenticationMethod.CLIENT_SECRET_POST),
                authorizationGrantTypes = setOf(
                    AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.REFRESH_TOKEN
                ),
                redirectUris = setOf(
                    "https://oidcdebugger.com/debug", "http://react-app:5001/oauth_callback"),
                scopes = setOf(OidcScopes.OPENID),
                tokenSettings = tokenSettings,
                clientSettings = ClientSettings.builder().build()
            )

            val clientCredentialsClient = RegisteredClient(
                clientId = "demo-client-credentials-client",
                clientName = "demo-client-credentials-client",
                clientSecret = "{noop}123456",
                clientAuthenticationMethods = setOf(ClientAuthenticationMethod.CLIENT_SECRET_POST),
                authorizationGrantTypes = setOf(AuthorizationGrantType.CLIENT_CREDENTIALS),
                tokenSettings = tokenSettings,
                clientSettings = ClientSettings.builder().build()
            )

            return setOf(authCodeClient, authCodePkceClient, clientCredentialsClient)
        }
}
