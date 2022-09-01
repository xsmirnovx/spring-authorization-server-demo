package io.github.xsmirnovx.oauth2.server.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties
class RegisteredClientsProperties {
    val clients: Map<String, RegisteredClientProperties>? = null

    class RegisteredClientProperties {
        val clientId: String? = null
        private val secret: String? = null
        private val scopes: Set<String>? = null
        private val redirectUris: Set<String>? = null
        private val grants: Set<AuthorizationGrantType>? = null
        private val authenticationMethods: Set<ClientAuthenticationMethod>? = null

        fun toRegisteredClient(): RegisteredClient {
            return toRegisteredClient { it }
        }

        fun toRegisteredClient(secretEncoder: (String) -> String): RegisteredClient {
            return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(secretEncoder.invoke(secret.toString())) // fixme
                .scopes(appender(scopes))
                .redirectUris(appender(redirectUris))
                .authorizationGrantTypes(appender(grants))
                .clientAuthenticationMethods(appender(authenticationMethods))
                .build()
        }

        private fun <T> appender(values: Set<T?>?): (MutableSet<T?>) -> Unit {
            return {
                values?.forEach { value: T? -> it.add(value) }
            }
        }
    }
}