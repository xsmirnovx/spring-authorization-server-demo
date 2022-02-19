package io.github.xsmirnovx.oauth2.auth.server.demo.configuration

import com.nimbusds.jose.jwk.*
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer
import org.springframework.security.oauth2.server.authorization.client.*
import org.springframework.security.oauth2.server.authorization.config.*
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.util.*
import java.util.stream.Collectors

@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfiguration (val passwordEncoder: PasswordEncoder) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        return http.formLogin(Customizer.withDefaults()).build()
    }

    @Bean
    fun tokenSettings(): TokenSettings {
        return TokenSettings.builder()
            .accessTokenTimeToLive(Duration.ofDays(1L))
            .build()
    }

    @Bean
    fun registeredClientRepository(jdbcTemplate: JdbcTemplate?): RegisteredClientRepository {

        //auth code flow + PKCE
        val authCodePkceClient = RegisteredClient
            .withId("0f8dfbff-83d5-4172-aa88-f3f90bf0f0e1")
            //.withId(UUID.randomUUID().toString())
            .clientId("demo-auth-code-pkce-client")
            //.clientSecret("{noop}123456")
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .clientSettings(ClientSettings.builder().requireProofKey(true).build())
            .redirectUri("https://oidcdebugger.com/debug")
            .tokenSettings(tokenSettings())
            .scope(OidcScopes.OPENID)
            .build();

        // auth code flow
        val authCodeClient = RegisteredClient
            .withId("0f8dfbff-83d5-4172-aa88-f3f90bf0f0e0") // in order not to have dups on startup
            .clientId("demo-auth-code-client")
            .clientSecret(passwordEncoder.encode("123456"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("https://oidcdebugger.com/debug")
            .scope(OidcScopes.OPENID)
            .tokenSettings(tokenSettings())
            .build()

//        val clientCredentialsClient = RegisteredClient
//            .withId("0f8dfbff-83d5-4172-aa88-f3f90bf0f0e1") // in order not to have dups on startup
//            .clientId("jira-plugin")
//            .clientSecret("{noop}123456")
//            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//            .tokenSettings(tokenSettings())
//            .build()

        return InMemoryRegisteredClientRepository(authCodeClient)
//            authCodePkceClient,
//            clientCredentialsClient
//        )
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector: JWKSelector, securityContext: SecurityContext? ->
            jwkSelector.select(
                jwkSet
            )
        }
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

            val authorities = context
                ?.takeIf { it.tokenType == OAuth2TokenType.ACCESS_TOKEN }
                ?.let {
                    val auth = it.getPrincipal<Authentication>()
                    auth.authorities.stream()
                        .map { obj: GrantedAuthority -> obj.authority }
                        .collect(Collectors.toSet())
                }

            context?.claims?.claim("user-authorities", authorities)
        }
    }

    companion object {
        private fun generateRsa(): RSAKey {
            val keyPair = generateRsaKey()
            val publicKey = keyPair.public as RSAPublicKey
            val privateKey = keyPair.private as RSAPrivateKey
            return RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build()
        }

        //@SneakyThrows(NoSuchAlgorithmException::class)
        private fun generateRsaKey(): KeyPair {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            return keyPairGenerator.generateKeyPair()
        }
    }
}