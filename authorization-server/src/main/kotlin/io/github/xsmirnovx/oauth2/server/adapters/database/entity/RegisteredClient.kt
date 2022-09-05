package io.github.xsmirnovx.oauth2.server.adapters.database.entity

import io.github.xsmirnovx.oauth2.server.adapters.database.converter.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient as RegisteredClientDomain
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "oauth2_registered_client")
data class RegisteredClient(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    val id: String? = null,
    val clientId: String? = null,
    val clientIdIssuedAt: Instant? = Instant.now(),
    val clientSecret: String? = null,
    val clientSecretExpiresAt: Instant? = null,
    val clientName: String? = null,

    @Column(length = 1000)
    @Convert(converter = ClientAuthenticationMethodsConverter::class)
    val clientAuthenticationMethods: Set<ClientAuthenticationMethod>? = null,

    @Column(length = 1000)
    @Convert(converter = AuthorizationGrantTypesConverter::class)
    val authorizationGrantTypes: Set<AuthorizationGrantType>? = null,

    @Column(length = 1000)
    @Convert(converter = StringListToStringConverter::class)
    val redirectUris: Set<String>? = null,

    @Column(length = 1000)
    @Convert(converter = StringListToStringConverter::class)
    val scopes: Set<String>? = null,

    @Column(length = 2000)
    @Convert(converter = ClientSettingsConverter::class)
    val clientSettings: ClientSettings? = null,

    @Column(length = 2000)
    @Convert(converter = TokenSettingsConverter::class)
    var tokenSettings: TokenSettings? = null
) {

    companion object {

        fun fromDomain(domain: RegisteredClientDomain): RegisteredClient {
            return RegisteredClient(
                clientId = domain.clientId,
                clientIdIssuedAt = domain.clientIdIssuedAt.let { Instant.now() },
                clientSecret = domain.clientSecret,
                clientSecretExpiresAt = domain.clientSecretExpiresAt,
                clientName = domain.clientName,
                clientAuthenticationMethods = domain.clientAuthenticationMethods,
                authorizationGrantTypes = domain.authorizationGrantTypes,
                redirectUris = domain.redirectUris,
                scopes = domain.scopes,
                clientSettings = domain.clientSettings,
                tokenSettings = domain.tokenSettings
            )
        }

        fun toDomain(entity: RegisteredClient): RegisteredClientDomain {
            return RegisteredClientDomain.withId(entity.id)
                .clientId(entity.clientId)
                .clientIdIssuedAt(entity.clientIdIssuedAt)
                .clientSecret(entity.clientSecret)
                .clientSecretExpiresAt(entity.clientSecretExpiresAt)
                .clientName(entity.clientName)
                .clientAuthenticationMethods(appender(entity.clientAuthenticationMethods))
                .authorizationGrantTypes(appender(entity.authorizationGrantTypes))
                .redirectUris(appender(entity.redirectUris))
                .scopes(appender(entity.scopes))
                .clientSettings(entity.clientSettings)
                .tokenSettings(entity.tokenSettings)
                .build()
        }

        private fun <T> appender(values: Set<T?>?): (MutableSet<T?>) -> Unit {
            return {
                values?.forEach { value: T? -> it.add(value) }
            }
        }
    }
}
