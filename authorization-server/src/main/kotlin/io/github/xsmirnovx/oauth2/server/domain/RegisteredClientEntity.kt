package io.github.xsmirnovx.oauth2.server.domain

import org.springframework.beans.BeanUtils
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import java.time.Instant
import javax.persistence.*

@Entity
data class RegisteredClientEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String? = null,
    val clientId: String? = null,
    val clientIdIssuedAt: Instant? = null,
    val clientSecret: String? = null,
    val clientSecretExpiresAt: Instant? = null,
    val clientName: String? = null,

    @ElementCollection(targetClass = ClientAuthenticationMethod::class)
    val clientAuthenticationMethods: Set<ClientAuthenticationMethod> = emptySet(),

    @ElementCollection(targetClass = AuthorizationGrantType::class)
    val authorizationGrantTypes: Set<AuthorizationGrantType> = emptySet(),

    @ElementCollection(targetClass = String::class)
    val redirectUris: Set<String>? = null,

    @ElementCollection(targetClass = String::class)
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
