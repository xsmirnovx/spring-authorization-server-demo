package io.github.xsmirnox.oauth2.resourceserver;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
class RecordsResourceServerApplicationApplication

fun main(args: Array<String>) {
    runApplication<RecordsResourceServerApplicationApplication>(*args)
}
