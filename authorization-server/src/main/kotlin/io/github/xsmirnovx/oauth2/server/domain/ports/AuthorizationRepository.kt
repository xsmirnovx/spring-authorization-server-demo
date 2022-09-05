package io.github.xsmirnovx.oauth2.server.domain.ports

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization
import java.util.*

interface AuthorizationRepository {
    fun findByState(state: String?): Optional<Authorization?>?
    fun findByAuthorizationCodeValue(authorizationCode: String?): Optional<Authorization?>?
    fun findByAccessTokenValue(accessToken: String?): Optional<Authorization?>?
    fun findByRefreshTokenValue(refreshToken: String?): Optional<Authorization?>?
    fun findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(token: String?): Optional<Authorization?>?
}
