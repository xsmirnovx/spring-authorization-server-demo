package io.github.xsmirnovx.oauth2.server.controller

import io.github.xsmirnovx.oauth2.server.service.RegisteredClientService
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisteredClientController(val registeredClientService: RegisteredClientService)