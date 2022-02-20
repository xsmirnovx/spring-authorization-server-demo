package io.github.xsmirnovx.oauth2.server.exception

import java.lang.RuntimeException

class RegisteredClientNotFoundException(msg: String): RuntimeException(msg)