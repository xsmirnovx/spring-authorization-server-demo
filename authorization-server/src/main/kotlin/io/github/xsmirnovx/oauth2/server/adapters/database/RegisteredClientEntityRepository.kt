package io.github.xsmirnovx.oauth2.server.adapters.database

import io.github.xsmirnovx.oauth2.server.repository.entity.RegisteredClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RegisteredClientEntityRepository : JpaRepository<RegisteredClientEntity, String> {

    fun findByClientId(clientId: String): Optional<RegisteredClientEntity>
}