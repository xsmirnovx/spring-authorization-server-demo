package io.github.xsmirnovx.oauth2.server.repository

import io.github.xsmirnovx.oauth2.server.domain.RegisteredClientEntity
import io.github.xsmirnovx.oauth2.server.domain.RegisteredClientEntity.Companion.toRegisteredClient
import io.github.xsmirnovx.oauth2.server.exeption.RegisteredClientNotFoundException
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component

@Component
class RegisteredClientRepositoryImpl(
    val registeredClientJpaRepository: RegisteredClientJpaRepository
) : RegisteredClientRepository {

    override fun save(registeredClient: RegisteredClient) {
        registeredClientJpaRepository
            .save(RegisteredClientEntity.fromRegisteredClient(registeredClient))
    }

    override fun findById(id: String): RegisteredClient {
        return registeredClientJpaRepository.findById(id)
            .map { toRegisteredClient(it) }
            .orElseThrow {
                RegisteredClientNotFoundException("Client with id [$id] not found")
            }
    }

    override fun findByClientId(clientId: String): RegisteredClient {
        return registeredClientJpaRepository.findByClientId(clientId)
            .map { toRegisteredClient(it) }
            .orElseThrow{
                RegisteredClientNotFoundException("Client with client id [$clientId] not found")
            }
    }
}
