package io.github.xsmirnovx.oauth2.server.domain

import io.github.xsmirnovx.oauth2.server.converter.AuthorizationGrantTypesConverter
import io.github.xsmirnovx.oauth2.server.converter.ClientAuthenticationMethodsConverter
import io.github.xsmirnovx.oauth2.server.converter.StringListToStringConverter
import org.hibernate.annotations.GenericGenerator
import org.springframework.beans.BeanUtils
import org.springframework.data.annotation.CreatedDate
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

    @CreatedDate
    val clientIdIssuedAt: Instant? = Instant.now(),

    val clientSecret: String? = null,

    val clientSecretExpiresAt: Instant? = null,

    val clientName: String? = null,

    @Column(name = "client_authentication_methods")
    @Convert(converter = ClientAuthenticationMethodsConverter::class)
    val clientAuthenticationMethods: Set<ClientAuthenticationMethod>? = null,

    @Column(name = "authorization_grant_types")
    @Convert(converter = AuthorizationGrantTypesConverter::class)
    val authorizationGrantTypes: Set<AuthorizationGrantType> = emptySet(),

    @Column(name = "redirect_uris")
    @Convert(converter = StringListToStringConverter::class)
    val redirectUris: Set<String>? = null,

    @Column(name = "scopes")
    @Convert(converter = StringListToStringConverter::class)
    val scopes: Set<String>? = null,

    val clientSettings: ClientSettings? = null,

    val tokenSettings: TokenSettings? = null
) {

    companion object {
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
                .clientAuthenticationMethods { entity.clientAuthenticationMethods }
                .authorizationGrantTypes { entity.authorizationGrantTypes }
                .redirectUris { entity.redirectUris }
                .scopes { entity.scopes}
                .clientSettings(entity.clientSettings)
                .tokenSettings(entity.tokenSettings)
                .build()
        }
    }
}