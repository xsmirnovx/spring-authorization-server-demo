package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.Authorization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaAuthorizationRepository : JpaRepository<Authorization?, String?> {

    fun findByState(state: String?): Optional<Authorization?>?
    fun findByAuthorizationCodeValue(authorizationCode: String?): Optional<Authorization?>?
    fun findByAccessTokenValue(accessToken: String?): Optional<Authorization?>?
    fun findByRefreshTokenValue(refreshToken: String?): Optional<Authorization?>?

    @Query(
        "select a from Authorization a where a.state = :token" +
                " or a.authorizationCodeValue = :token" +
                " or a.accessTokenValue = :token" +
                " or a.refreshTokenValue = :token"
    )
    fun findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(@Param("token") token: String?): Optional<Authorization?>?
}
