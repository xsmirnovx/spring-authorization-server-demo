package io.github.xsmirnovx.oauth2.server.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties
data class RegisteredClientsProperties(var clients: Map<String, RegisteredClientProperties>? = null) {

    data class RegisteredClientProperties(
        var clientId: String? = null,
        var secret: String? = null,
        var scopes: Set<String>? = null,
        var redirectUris: Set<String>? = null,
        var grants: Set<AuthorizationGrantType>? = null,
        var authenticationMethods: Set<ClientAuthenticationMethod>? = null,
        var clientSettings: ClientSettingsProperties? = null
    ) {

        fun toRegisteredClient(): RegisteredClient {
            return toRegisteredClient { it }
        }

        fun toRegisteredClient(secretEncoder: (String) -> String): RegisteredClient {
            return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(secretEncoder(secret!!))
                .scopes(appender(scopes))
                .redirectUris(appender(redirectUris))
                .authorizationGrantTypes(appender(grants))
                .clientAuthenticationMethods(appender(authenticationMethods))
                .clientSettings(clientSettings?.let {
                    ClientSettings.builder()
                        .requireProofKey(it.pkce)
                        .requireAuthorizationConsent(it.consent)
                        .build()
                })
                .build()
        }

        private fun <T> appender(values: Set<T?>?): (MutableSet<T?>) -> Unit {
            return {
                values?.forEach { value: T? -> it.add(value) }
            }
        }
    }

    data class ClientSettingsProperties(
        var pkce: Boolean = false,
        var consent: Boolean = false
    )
}
