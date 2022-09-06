package io.github.xsmirnovx.oauth2.server.adapters.database.entity

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent
import org.springframework.util.StringUtils
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "oauth2_authorization_consent")
@IdClass(AuthorizationConsentId::class)
data class AuthorizationConsent(

    @Id
    @ManyToOne
    @JoinColumn(name = "registered_client_id")
    val registeredClient: RegisteredClient? = null,

    @Id
    val principalName: String? = null,

    @Column(length = 1000)
    val authorities: String? = null
) {
    companion object {

        fun toDomain(entity: AuthorizationConsent?) : OAuth2AuthorizationConsent? {
            val builder = OAuth2AuthorizationConsent.withId(
                entity?.registeredClient?.id!!, entity.principalName!!
            )
            entity.authorities?.let {
                for (authority in StringUtils.commaDelimitedListToSet(it)) {
                    builder.authority(SimpleGrantedAuthority(authority))
                }
            }
            return builder.build()
        }

        fun fromDomain(domain: OAuth2AuthorizationConsent): AuthorizationConsent {
            return AuthorizationConsent(
                registeredClient = RegisteredClient(id = domain.registeredClientId),
                principalName = domain.principalName,
                authorities = StringUtils.collectionToCommaDelimitedString(domain.authorities)
            )
        }
    }
}

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
