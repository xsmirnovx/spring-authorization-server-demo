package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.AuthorizationConsent
import io.github.xsmirnovx.oauth2.server.adapters.database.entity.AuthorizationConsentId

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaAuthorizationConsentRepository : JpaRepository<AuthorizationConsent?, AuthorizationConsentId?> {

    fun findByRegisteredClientIdAndPrincipalName(
        registeredClientId: String?, principalName: String?): Optional<AuthorizationConsent?>

    fun deleteByRegisteredClientIdAndPrincipalName(
        registeredClientId: String?, principalName: String?)
}
