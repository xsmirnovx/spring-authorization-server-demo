package io.github.xsmirnovx.oauth2.server.repository

import io.github.xsmirnovx.oauth2.server.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {

    fun findByUsername(username: String?) : Optional<User>
}
