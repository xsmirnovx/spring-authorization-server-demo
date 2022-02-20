package io.github.xsmirnovx.oauth2.server.service

import io.github.xsmirnovx.oauth2.server.repository.RegisteredClientJpaRepository
import org.springframework.stereotype.Service

@Service
class RegisteredClientService(val registeredClientJpaRepository: RegisteredClientJpaRepository)
