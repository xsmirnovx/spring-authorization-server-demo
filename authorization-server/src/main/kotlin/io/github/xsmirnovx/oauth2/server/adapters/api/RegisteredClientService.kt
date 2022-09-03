package io.github.xsmirnovx.oauth2.server.adapters.api

import io.github.xsmirnovx.oauth2.server.adapters.database.JpaRegisteredClientRepository
import org.springframework.stereotype.Service

@Service
class RegisteredClientService(val registeredClientEntityRepository: JpaRegisteredClientRepository)
