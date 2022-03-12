package io.github.xsmirnovx.oauth2.server.configuration

import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class WebAuthorizationConfiguration {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests {
                    authorizeRequests -> authorizeRequests.anyRequest().authenticated()
            }
            .formLogin(Customizer.withDefaults())

        return http.build()
    }
}
