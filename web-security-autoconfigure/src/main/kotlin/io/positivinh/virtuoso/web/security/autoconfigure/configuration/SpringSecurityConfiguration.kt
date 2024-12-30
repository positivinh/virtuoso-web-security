package io.positivinh.virtuoso.web.security.autoconfigure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Spring security configuration
 *
 * See [Reference documentation](https://docs.spring.io/spring-security/reference/servlet/configuration/kotlin.html)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SpringSecurityConfiguration {

    /**
     * Security filter chain configuration
     *
     * See [Reference documentation](https://docs.spring.io/spring-security/reference/servlet/configuration/kotlin.html)
     * See [Actuator security configuration](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.security)
     *
     */
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        endpointAuthorizationConfigurationProperties: EndpointAuthorizationConfigurationProperties
    ): SecurityFilterChain {

        http {

            csrf { disable() }

            authorizeHttpRequests {

                endpointAuthorizationConfigurationProperties.permitAll.forEach {
                    it.method?.let { method -> authorize(HttpMethod.valueOf(method), it.pattern, permitAll) }
                        ?: authorize(it.pattern, permitAll)
                }
                endpointAuthorizationConfigurationProperties.authenticated.forEach {
                    it.method?.let { method -> authorize(HttpMethod.valueOf(method), it.pattern, authenticated) }
                        ?: authorize(it.pattern, authenticated)
                }
                endpointAuthorizationConfigurationProperties.denyAll.forEach {
                    it.method?.let { method -> authorize(HttpMethod.valueOf(method), it.pattern, denyAll) }
                        ?: authorize(it.pattern, denyAll)
                }

                authorize(anyRequest, authenticated)
            }
//            addFilterBefore<UsernamePasswordAuthenticationFilter>(authorizationTokenFilter)
        }

        return http.build()
    }

    /**
     * Cors configuration
     *
     * See [Reference documentation](https://docs.spring.io/spring-security/reference/reactive/integrations/cors.html)
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {

        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = listOf("*")
        corsConfiguration.allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)

        return source
    }
}
