package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.adapters.database.entity.RegisteredClient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaRegisteredClientRepository : JpaRepository<RegisteredClient, String> {

    fun findByClientId(clientId: String): Optional<RegisteredClient>
}
