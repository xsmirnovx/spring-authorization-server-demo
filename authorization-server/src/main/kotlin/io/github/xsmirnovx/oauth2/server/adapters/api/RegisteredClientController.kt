package io.github.xsmirnovx.oauth2.server.adapters.api

import org.springframework.web.bind.annotation.RestController

@RestController
class RegisteredClientController(val registeredClientService: RegisteredClientService)
