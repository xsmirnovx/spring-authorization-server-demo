package io.github.xsmirnovx.oauth2.server.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    val id: String,
    val username: String,
    val password: String,
    val role: String,
    val enabled: Boolean
)
