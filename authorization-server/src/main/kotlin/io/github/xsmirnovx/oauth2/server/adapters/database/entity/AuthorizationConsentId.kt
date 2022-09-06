package io.github.xsmirnovx.oauth2.server.adapters.database.entity

import java.io.Serializable
import java.util.*

class AuthorizationConsentId : Serializable {
    private val registeredClient: RegisteredClient? = null
    private val principalName: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AuthorizationConsentId
        return registeredClient?.id == that.registeredClient?.id
                && principalName == that.principalName
    }

    override fun hashCode(): Int {
        return Objects.hash(registeredClient?.id, principalName)
    }
}
