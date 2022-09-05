package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization
import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization.Companion.fromDomain
import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization.Companion.toDomain
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2TokenType
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class OAuth2AuthorizationServiceImpl(
    authorizationRepository: JpaAuthorizationRepository,
    registeredClientRepository: RegisteredClientRepository
) : OAuth2AuthorizationService {

    private val authorizationRepository: JpaAuthorizationRepository
    private val registeredClientRepository: RegisteredClientRepository

    override fun save(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")
        authorizationRepository.save(fromDomain(authorization))
    }

    override fun remove(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")
        authorizationRepository.deleteById(authorization.id)
    }

    override fun findById(id: String): OAuth2Authorization? {
        Assert.hasText(id, "id cannot be empty")
        val result = authorizationRepository.findById(id)

        return result
            .map { a: Authorization? -> toDomain(a!!) }
            .orElse(null)
    }

    override fun findByToken(token: String, tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")
        val result: Authorization?
        when {
            tokenType == null -> {
                result =
                    authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(
                        token
                    )!!.get()
            }
            OAuth2ParameterNames.STATE == tokenType.value -> {
                result = authorizationRepository.findByState(token)!!.get()
            }
            OAuth2ParameterNames.CODE == tokenType.value -> {
                result = authorizationRepository.findByAuthorizationCodeValue(token)!!.get()
            }
            OAuth2ParameterNames.ACCESS_TOKEN == tokenType.value -> {
                result = authorizationRepository.findByAccessTokenValue(token)!!.get()
            }
            OAuth2ParameterNames.REFRESH_TOKEN == tokenType.value -> {
                result = authorizationRepository.findByRefreshTokenValue(token)!!.get()
            }
            else -> result = null
        }

        return toDomain(result!!)
    }

    companion object {
        fun resolveAuthorizationGrantType(authorizationGrantType: String): AuthorizationGrantType {
            if (AuthorizationGrantType.AUTHORIZATION_CODE.value == authorizationGrantType) {
                return AuthorizationGrantType.AUTHORIZATION_CODE
            } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.value == authorizationGrantType) {
                return AuthorizationGrantType.CLIENT_CREDENTIALS
            } else if (AuthorizationGrantType.REFRESH_TOKEN.value == authorizationGrantType) {
                return AuthorizationGrantType.REFRESH_TOKEN
            }
            return AuthorizationGrantType(authorizationGrantType) // Custom authorization grant type
        }
    }

    init {
        Assert.notNull(authorizationRepository, "authorizationRepository cannot be null")
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null")
        this.authorizationRepository = authorizationRepository
        this.registeredClientRepository = registeredClientRepository
    }
}