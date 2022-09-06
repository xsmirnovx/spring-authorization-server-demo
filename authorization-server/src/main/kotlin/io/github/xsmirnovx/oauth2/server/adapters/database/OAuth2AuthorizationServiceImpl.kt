package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization
import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization.Companion.fromDomain
import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization.Companion.toDomain
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2TokenType
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class OAuth2AuthorizationServiceImpl(val authorizationRepository: JpaAuthorizationRepository)
    : OAuth2AuthorizationService {

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
        return authorizationRepository.findById(id).get().let(Authorization::toDomain)
    }

    override fun findByToken(token: String, tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")
        val result = when {
            tokenType == null -> {
                authorizationRepository
                    .findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(token)!!.get()
            }
            OAuth2ParameterNames.STATE == tokenType.value -> {
                authorizationRepository.findByState(token)!!.get()
            }
            OAuth2ParameterNames.CODE == tokenType.value -> {
                 authorizationRepository.findByAuthorizationCodeValue(token)!!.get()
            }
            OAuth2ParameterNames.ACCESS_TOKEN == tokenType.value -> {
                authorizationRepository.findByAccessTokenValue(token)!!.get()
            }
            OAuth2ParameterNames.REFRESH_TOKEN == tokenType.value -> {
                authorizationRepository.findByRefreshTokenValue(token)!!.get()
            }
            else -> null
        }

        return toDomain(result!!)
    }

    companion object {
        fun resolveAuthorizationGrantType(authorizationGrantType: String): AuthorizationGrantType {
            return when {
                AuthorizationGrantType.AUTHORIZATION_CODE.value == authorizationGrantType -> {
                    AuthorizationGrantType.AUTHORIZATION_CODE
                }
                AuthorizationGrantType.CLIENT_CREDENTIALS.value == authorizationGrantType -> {
                    AuthorizationGrantType.CLIENT_CREDENTIALS
                }
                AuthorizationGrantType.REFRESH_TOKEN.value == authorizationGrantType -> {
                    AuthorizationGrantType.REFRESH_TOKEN
                }
                // Custom authorization grant type
                else -> AuthorizationGrantType(authorizationGrantType)
            }
        }
    }
}
