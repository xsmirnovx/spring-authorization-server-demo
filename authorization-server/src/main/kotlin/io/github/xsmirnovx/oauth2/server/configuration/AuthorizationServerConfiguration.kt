package io.github.xsmirnovx.oauth2.server.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.server.authorization.*
import org.springframework.security.oauth2.server.authorization.config.*
import org.springframework.security.web.SecurityFilterChain
import java.time.Duration
import java.util.stream.Collectors

@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        return http.formLogin(Customizer.withDefaults()).build()
    }

    @Bean
    fun tokenSettings(): TokenSettings {
        return TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1L)).build()
    }

    @Bean
    fun providerSettings(): ProviderSettings {
        return ProviderSettings.builder()
            .issuer("http://auth-server:9001")
            .build()
    }

    @Bean
    fun jwtCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context: JwtEncodingContext? ->

            context
                ?.takeIf {
                    it.tokenType == OAuth2TokenType.ACCESS_TOKEN
                }
                ?.getPrincipal<Authentication>()?.authorities?.stream()?.map { it.authority }
                ?.collect(Collectors.toSet())
                ?.also {
                    context.claims?.claim("user-authorities", it)
                }
        }
    }
}
