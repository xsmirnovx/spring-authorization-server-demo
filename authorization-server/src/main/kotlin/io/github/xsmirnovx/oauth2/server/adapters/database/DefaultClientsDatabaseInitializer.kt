package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.RegisteredClient as RegisteredClientEntity
import io.github.xsmirnovx.oauth2.server.configuration.RegisteredClientsProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import org.springframework.stereotype.Component

@Component
class DefaultClientsDatabaseInitializer(
    val registeredClientEntityRepository: JpaRegisteredClientRepository,
    val passwordEncoder: PasswordEncoder,
    val tokenSettings: TokenSettings,
    val registeredClientsProperties: RegisteredClientsProperties)
        : ApplicationListener<ApplicationReadyEvent?> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        registeredClientsProperties.clients?.values?.stream()
            ?.map { it.toRegisteredClient(passwordEncoder::encode) }
            ?.map { RegisteredClientEntity.fromDomain(it) }
            ?.map { it.copy(tokenSettings = tokenSettings) }
            ?.forEach { saveRegisteredClient(it) }
    }

    private fun saveRegisteredClient(entity: RegisteredClientEntity) {
        entity.clientId?.let {
            registeredClientEntityRepository
                .findByClientId(it)
                .ifPresentOrElse({}, { registeredClientEntityRepository.save(entity) })
        }
    }
}
