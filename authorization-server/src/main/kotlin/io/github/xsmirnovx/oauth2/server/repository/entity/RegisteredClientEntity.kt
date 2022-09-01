package io.github.xsmirnovx.oauth2.server.repository.entity

import io.github.xsmirnovx.oauth2.server.converter.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
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
)

