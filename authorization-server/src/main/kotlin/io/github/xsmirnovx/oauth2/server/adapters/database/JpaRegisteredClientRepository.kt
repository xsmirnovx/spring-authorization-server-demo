package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.domain.RegisteredClient.fromEntity
import io.github.xsmirnovx.oauth2.server.domain.RegisteredClient.toEntity
import io.github.xsmirnovx.oauth2.server.exception.RegisteredClientNotFoundException
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component

@Component
class JpaRegisteredClientRepository(
    val registeredClientEntityRepository: RegisteredClientEntityRepository
) : RegisteredClientRepository {

    override fun save(registeredClient: RegisteredClient) {
        registeredClientEntityRepository
            .save(registeredClient.toEntity())
    }

    override fun findById(id: String): RegisteredClient {
        return registeredClientEntityRepository.findById(id)
            .map { fromEntity(it) }
            .orElseThrow {
                RegisteredClientNotFoundException("Client with id not found: [$id]")
            }
    }

    override fun findByClientId(clientId: String): RegisteredClient {
        return registeredClientEntityRepository.findByClientId(clientId)
            .map { fromEntity(it) }
            .orElseThrow {
                RegisteredClientNotFoundException("Client with client id not found: [$clientId]")
            }
    }
}
