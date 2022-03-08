package io.github.xsmirnovx.oauth2.server.domain

import io.github.xsmirnovx.oauth2.server.converter.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.beans.BeanUtils
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import java.time.Instant
import javax.persistence.*

@Entity(name = "oauth2_registered_client")
@EntityListeners(value = [AuditingEntityListener::class])
data class RegisteredClientEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    val id: String? = null,

    val clientId: String? = null,

    @Column(columnDefinition = "TIMESTAMP")
    val clientIdIssuedAt: Instant? = Instant.now(),

    val clientSecret: String? = null,

    val clientSecretExpiresAt: Instant? = null,

    val clientName: String? = null,

    @Convert(converter = ClientAuthenticationMethodsConverter::class)
    val clientAuthenticationMethods: Set<ClientAuthenticationMethod>? = null,

    @Convert(converter = AuthorizationGrantTypesConverter::class)
    val authorizationGrantTypes: Set<AuthorizationGrantType> = emptySet(),

    @Convert(converter = StringListToStringConverter::class)
    val redirectUris: Set<String>? = null,

    @Convert(converter = StringListToStringConverter::class)
    val scopes: Set<String>? = null,

    @Convert(converter = ClientSettingsConverter::class)
    val clientSettings: ClientSettings = ClientSettings.builder().build(),

    @Convert(converter = TokenSettingsConverter::class)
    val tokenSettings: TokenSettings = TokenSettings.builder().build()
) {

    companion object {

        private fun <T> appender(values: Set<T?>?): (MutableSet<T?>) -> Unit {
            return {
                values?.forEach { value: T? -> it.add(value) }
            }
        }

        fun fromRegisteredClient(registeredClient: RegisteredClient?): RegisteredClientEntity {
            val registeredClientEntity = RegisteredClientEntity()
            BeanUtils.copyProperties(registeredClient!!, registeredClientEntity)
            return registeredClientEntity
        }

        fun toRegisteredClient(entity: RegisteredClientEntity): RegisteredClient {

            return RegisteredClient.withId(entity.id)
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
    }
}

