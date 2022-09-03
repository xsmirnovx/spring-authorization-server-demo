package io.github.xsmirnovx.oauth2.server.domain

import org.springframework.beans.BeanUtils
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import io.github.xsmirnovx.oauth2.server.adapters.database.entity.RegisteredClient as RegisteredClientEntity

object RegisteredClientExtension {

    fun RegisteredClient.toEntity(): RegisteredClientEntity {
        val registeredClientEntity = RegisteredClientEntity()
        BeanUtils.copyProperties(this, registeredClientEntity)
        return registeredClientEntity
    }

    fun fromEntity(entity: RegisteredClientEntity): RegisteredClient {
        return RegisteredClient.withId(entity.id)
            .clientId(entity.clientId)
            .clientIdIssuedAt(entity.clientIdIssuedAt)
            .clientSecret(entity.clientSecret)
            .clientSecretExpiresAt(entity.clientSecretExpiresAt)
            .clientName(entity.clientName)
            .clientAuthenticationMethods(appender(entity.clientAuthenticationMethods))
            .authorizationGrantTypes(appender(entity.authorizationGrantTypes))
            .redirectUris(appender(entity.redirectUris))
            .scopes(appender(entity.scopes))
            .clientSettings(entity.clientSettings)
            .tokenSettings(entity.tokenSettings)
            .build()
    }

    private fun <T> appender(values: Set<T?>?): (MutableSet<T?>) -> Unit {
        return {
            values?.forEach { value: T? -> it.add(value) }
        }
    }
}
