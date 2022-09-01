package io.github.xsmirnovx.oauth2.server.adapters.api

import io.github.xsmirnovx.oauth2.server.adapters.database.RegisteredClientEntityRepository
import org.springframework.stereotype.Service

@Service
class RegisteredClientService(val registeredClientEntityRepository: RegisteredClientEntityRepository)
