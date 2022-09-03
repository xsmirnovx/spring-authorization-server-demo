package io.github.xsmirnovx.oauth2.server.adapters.database.entity

import io.github.xsmirnovx.oauth2.server.adapters.database.converter.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
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
    val tokenSettings: TokenSettings? = null
)
