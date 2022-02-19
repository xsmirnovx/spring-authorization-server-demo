package io.github.xsmirnovx.oauth2.server.exeption

import java.lang.RuntimeException

class RegisteredClientNotFoundException(msg: String): RuntimeException(msg)