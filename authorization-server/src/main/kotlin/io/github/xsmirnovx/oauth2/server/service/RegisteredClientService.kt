package io.github.xsmirnovx.oauth2.server.service

import io.github.xsmirnovx.oauth2.server.repository.RegisteredClientEntityRepository
import org.springframework.stereotype.Service

@Service
class RegisteredClientService(val registeredClientEntityRepository: RegisteredClientEntityRepository)
