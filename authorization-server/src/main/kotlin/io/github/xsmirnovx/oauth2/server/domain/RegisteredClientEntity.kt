package io.github.xsmirnovx.oauth2.server.domain

import org.springframework.beans.BeanUtils
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class RegisteredClientEntity {

    @Id
    private val id: String? = null
    private val clientId: String? = null
    private val clientIdIssuedAt: Instant? = null
    private val clientSecret: String? = null
    private val clientSecretExpiresAt: Instant? = null
    private val clientName: String? = null
    private val clientAuthenticationMethods: Set<ClientAuthenticationMethod>? = null
    private val authorizationGrantTypes: Set<AuthorizationGrantType>? = null
    private val redirectUris: Set<String>? = null
    private val scopes: Set<String>? = null
    private val clientSettings: ClientSettings? = null
    private val tokenSettings: TokenSettings? = null

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
