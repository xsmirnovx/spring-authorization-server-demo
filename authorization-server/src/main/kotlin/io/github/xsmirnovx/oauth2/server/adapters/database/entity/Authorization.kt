package io.github.xsmirnovx.oauth2.server.adapters.database.entity

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.xsmirnovx.oauth2.server.adapters.database.OAuth2AuthorizationServiceImpl
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.util.StringUtils
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "oauth2_authorization")
data class Authorization(

    @Id
    @Column
    val id: String? = null,

    @ManyToOne
    @JoinColumn(name = "registered_client_id")
    val registeredClient: RegisteredClient? = null,

    val principalName: String? = null,
    val authorizationGrantType: String? = null,

    @Column(length = 4000)
    val attributes: String? = null,

    @Column(length = 500)
    val state: String? = null,

    @Column(length = 4000)
    val authorizationCodeValue: String? = null,
    val authorizationCodeIssuedAt: Instant? = null,
    val authorizationCodeExpiresAt: Instant? = null,
    val authorizationCodeMetadata: String? = null,

    @Column(length = 4000)
    val accessTokenValue: String? = null,
    val accessTokenIssuedAt: Instant? = null,
    val accessTokenExpiresAt: Instant? = null,

    @Column(length = 2000)
    val accessTokenMetadata: String? = null,
    val accessTokenType: String? = null,

    @Column(length = 1000)
    val accessTokenScopes: String? = null,

    @Column(length = 4000)
    val refreshTokenValue: String? = null,
    val refreshTokenIssuedAt: Instant? = null,
    val refreshTokenExpiresAt: Instant? = null,

    @Column(length = 2000)
    val refreshTokenMetadata: String? = null,

    @Column(length = 4000)
    val oidcIdTokenValue: String? = null,
    val oidcIdTokenIssuedAt: Instant? = null,
    val oidcIdTokenExpiresAt: Instant? = null,

    @Column(length = 2000)
    val oidcIdTokenMetadata: String? = null,

    @Column(length = 2000)
    val oidcIdTokenClaims: String? = null
) {

    companion object {

        private val objectMapper: ObjectMapper = ObjectMapper()
        init {
            val classLoader = Authorization::class.java.classLoader
            val securityModules = SecurityJackson2Modules.getModules(classLoader)
            objectMapper.registerModules(securityModules);
            objectMapper.registerModule(OAuth2AuthorizationServerJackson2Module())
        }

        fun fromDomain(authorization: OAuth2Authorization): Authorization {

            val authorizationCode = authorization.getToken(OAuth2AuthorizationCode::class.java)
            val accessToken = authorization.getToken(OAuth2AccessToken::class.java)
            val refreshToken = authorization.getToken(OAuth2RefreshToken::class.java)
            val oidcIdToken = authorization.getToken(OidcIdToken::class.java)

            return Authorization(
                id = authorization.id,
                registeredClient = RegisteredClient(id = authorization.registeredClientId),
                principalName = authorization.principalName,
                authorizationGrantType = authorization.authorizationGrantType.value,
                attributes = writeMap(authorization.attributes),
                state = authorization.getAttribute(OAuth2ParameterNames.STATE),

                authorizationCodeValue = authorizationCode?.token?.tokenValue,
                authorizationCodeIssuedAt = authorizationCode?.token?.issuedAt,
                authorizationCodeExpiresAt = authorizationCode?.token?.expiresAt,
                authorizationCodeMetadata = authorizationCode?.metadata?.let { writeMap(it) },

                accessTokenValue = accessToken?.token?.tokenValue,
                accessTokenIssuedAt = accessToken?.token?.issuedAt,
                accessTokenExpiresAt = accessToken?.token?.expiresAt,
                accessTokenMetadata = accessToken?.metadata?.let { writeMap(it) },

                refreshTokenValue = refreshToken?.token?.tokenValue,
                refreshTokenIssuedAt = refreshToken?.token?.issuedAt,
                refreshTokenExpiresAt = refreshToken?.token?.expiresAt,
                refreshTokenMetadata = refreshToken?.metadata?.let { writeMap(it) },

                oidcIdTokenValue = oidcIdToken?.token?.tokenValue,
                oidcIdTokenIssuedAt = oidcIdToken?.token?.issuedAt,
                oidcIdTokenExpiresAt = oidcIdToken?.token?.expiresAt,
                oidcIdTokenMetadata = refreshToken?.metadata?.let { writeMap(it) },

                accessTokenScopes = accessToken?.token?.scopes?.let {
                    StringUtils.collectionToDelimitedString(accessToken.token.scopes, ",")
                },

                oidcIdTokenClaims = oidcIdToken?.claims?.let { writeMap(it) }
            )
        }

        // FIXME:
        fun toDomain(entity: Authorization): OAuth2Authorization? {

            val builder = OAuth2Authorization.withRegisteredClient(RegisteredClient.toDomain(entity.registeredClient!!))
                .id(entity.id)
                .principalName(entity.principalName)
                .authorizationGrantType(
                    entity.authorizationGrantType?.let {
                        OAuth2AuthorizationServiceImpl.resolveAuthorizationGrantType(
                            it
                        )
                    }
                )
                .attributes { attributes: MutableMap<String?, Any?> ->
                    attributes.putAll(
                        parseMap(entity.attributes)!!
                    )
                }
            if (entity.state != null) {
                builder.attribute(OAuth2ParameterNames.STATE, entity.state)
            }
            if (entity.authorizationCodeValue != null) {
                val authorizationCode = OAuth2AuthorizationCode(
                    entity.authorizationCodeValue,
                    entity.authorizationCodeIssuedAt,
                    entity.authorizationCodeExpiresAt
                )
                builder.token(authorizationCode,
                    { metadata: MutableMap<String?, Any?> ->
                        metadata.putAll(
                            parseMap(entity.authorizationCodeMetadata)!!
                        )
                    })
            }
            if (entity.accessTokenValue != null) {
                val accessToken = OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    entity.accessTokenValue,
                    entity.accessTokenIssuedAt,
                    entity.accessTokenExpiresAt,
                    StringUtils.commaDelimitedListToSet(entity.accessTokenScopes)
                )
                builder.token(accessToken,
                    { metadata: MutableMap<String?, Any?> ->
                        metadata.putAll(
                            parseMap(entity.accessTokenMetadata)!!
                        )
                    })
            }
            if (entity.refreshTokenValue != null) {
                val refreshToken = OAuth2RefreshToken(
                    entity.refreshTokenValue,
                    entity.refreshTokenIssuedAt,
                    entity.refreshTokenExpiresAt
                )
                builder.token(refreshToken,
                    { metadata: MutableMap<String?, Any?> ->
                        metadata.putAll(
                            parseMap(entity.refreshTokenMetadata)!!
                        )
                    })
            }
            if (entity.oidcIdTokenValue != null) {
                val idToken = OidcIdToken(
                    entity.oidcIdTokenValue,
                    entity.oidcIdTokenIssuedAt,
                    entity.oidcIdTokenExpiresAt,
                    parseMap(entity.oidcIdTokenClaims)
                )
                builder.token(idToken,
                    { metadata: MutableMap<String?, Any?> ->
                        metadata.putAll(
                            parseMap(entity.oidcIdTokenMetadata)!!
                        )
                    })
            }
            return builder.build()
        }

        private fun parseMap(data: String?): Map<String?, Any?>? {
            return try {
                objectMapper.readValue(data, object : TypeReference<Map<String?, Any?>?>() {})
            } catch (ex: java.lang.Exception) {
                throw IllegalArgumentException(ex.message, ex)
            }
        }

        private fun writeMap(metadata: MutableMap<String, Any>?): String? {
            return try {
                this.objectMapper.writeValueAsString(metadata)
            } catch (ex: Exception) {
                throw IllegalArgumentException(ex.message, ex)
            }
        }
    }
}
