package io.github.xsmirnovx.oauth2.server.service;

import org.springframework.security.oauth2.core.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService

class JpaOAuth2AuthorizationService : OAuth2AuthorizationService {

    override fun save(authorization: OAuth2Authorization?) {
        TODO("Not yet implemented")
    }

    override fun remove(authorization: OAuth2Authorization?) {
        TODO("Not yet implemented")
    }

    override fun findById(id: String?): OAuth2Authorization? {
        TODO("Not yet implemented")
    }

    override fun findByToken(token: String?, tokenType: OAuth2TokenType?): OAuth2Authorization? {
        TODO("Not yet implemented")
    }
}
