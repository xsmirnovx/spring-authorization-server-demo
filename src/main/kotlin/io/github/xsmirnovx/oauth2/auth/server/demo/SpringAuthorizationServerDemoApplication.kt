package io.github.xsmirnovx.oauth2.auth.server.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringAuthorizationServerDemoApplication

fun main(args: Array<String>) {
	runApplication<SpringAuthorizationServerDemoApplication>(*args)
}
