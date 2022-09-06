package io.github.xsmirnovx.oauth2.server.adapters.database

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.xsmirnovx.oauth2.server.adapters.database.entity.RegisteredClient as RegisteredClientEntity
import io.github.xsmirnovx.oauth2.server.exception.RegisteredClientNotFoundException
import org.springframework.security.jackson2.SecurityJackson2Modules.*
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.stereotype.Component

@Component
class RegisteredClientRepositoryImpl(
    val registeredClientRepository: JpaRegisteredClientRepository, objectMapper: ObjectMapper
) : RegisteredClientRepository {

    init {
        val classLoader = JpaRegisteredClientRepository::class.java.classLoader
        val securityModules = getModules(classLoader)
        objectMapper.registerModules(securityModules)
        objectMapper.registerModule(OAuth2AuthorizationServerJackson2Module())
    }

    override fun save(registeredClient: RegisteredClient) {
        registeredClientRepository.save(RegisteredClientEntity.fromDomain(registeredClient))
    }

    override fun findById(id: String): RegisteredClient? {
        return registeredClientRepository.findById(id)
            .map { RegisteredClientEntity.toDomain(it) }
            .orElseThrow {
                RegisteredClientNotFoundException("Client with id not found: [$id]")
            }
    }

    override fun findByClientId(clientId: String): RegisteredClient? {
        return registeredClientRepository.findByClientId(clientId)
            .map { RegisteredClientEntity.toDomain(it) }
            .orElseThrow {
                RegisteredClientNotFoundException("Client with client id not found: [$clientId]")
            }
    }
}
