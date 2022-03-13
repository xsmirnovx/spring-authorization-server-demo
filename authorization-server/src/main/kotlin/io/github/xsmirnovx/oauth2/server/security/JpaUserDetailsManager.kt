package io.github.xsmirnovx.oauth2.server.security

import io.github.xsmirnovx.oauth2.server.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service

//@Service
class JpaUserDetailsManager(private val userRepository: UserRepository) : UserDetailsManager {

    override fun loadUserByUsername(username: String?): UserDetails {
        return userRepository.findByUsername(username)
            .map { SecurityUser(it) }
            .orElseThrow { UsernameNotFoundException(username) }
    }

    override fun createUser(user: UserDetails?) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: UserDetails?) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(username: String?) {
        TODO("Not yet implemented")
    }

    override fun changePassword(oldPassword: String?, newPassword: String?) {
        TODO("Not yet implemented")
    }

    override fun userExists(username: String?): Boolean {
        TODO("Not yet implemented")
    }
}
