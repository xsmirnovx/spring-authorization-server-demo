package io.github.xsmirnovx.oauth2.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
class SpringAuthorizationServerDemoApplication

fun main(args: Array<String>) {
	runApplication<SpringAuthorizationServerDemoApplication>(*args)
}
