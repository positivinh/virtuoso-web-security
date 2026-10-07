package io.positivinh.virtuoso.web.security.autoconfigure.configuration

import com.crabshue.commons.kotlin.logging.getLogger
import io.positivinh.virtuoso.web.security.autoconfigure.filter.VirtuosoHeaderAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Spring security configuration
 *
 * See [Reference documentation](https://docs.spring.io/spring-security/reference/servlet/configuration/kotlin.html)
 */
@Configuration
@EnableWebSecurity
class SpringSecurityConfiguration {

    private val log = getLogger()

    /**
     * Security filter chain configuration
     *
     * See [Reference documentation](https://docs.spring.io/spring-security/reference/servlet/configuration/kotlin.html)
     * See [Actuator security configuration](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.security)
     *
     */
    @ConditionalOnWebApplication
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        endpointAuthorizationConfigurationProperties: EndpointAuthorizationConfigurationProperties,
        @Autowired(required = false) @Qualifier("appAuthorizationFilter") authorizationFilter: OncePerRequestFilter?,
        virtuosoHeaderAuthorizationFilter: VirtuosoHeaderAuthorizationFilter
    ): SecurityFilterChain {

        http {

            // csrf
            csrf { disable() }

            // endpoint authorizations
            // first matching rule wins: deny rules come first so a broader permit-all cannot override them
            authorizeHttpRequests {

                endpointAuthorizationConfigurationProperties.denyAll.forEach {
                    it.method?.let { method -> authorize(HttpMethod.valueOf(method), it.pattern, denyAll) }
                        ?: authorize(it.pattern, denyAll)
                }
                endpointAuthorizationConfigurationProperties.permitAll.forEach {
                    it.method?.let { method -> authorize(HttpMethod.valueOf(method), it.pattern, permitAll) }
                        ?: authorize(it.pattern, permitAll)
                }
                endpointAuthorizationConfigurationProperties.authenticated.forEach {
                    it.method?.let { method -> authorize(HttpMethod.valueOf(method), it.pattern, authenticated) }
                        ?: authorize(it.pattern, authenticated)
                }

                authorize(anyRequest, authenticated)
            }

            // authentication filter: must populate the security context before authorization is checked
            if (authorizationFilter != null) {
                addFilterBefore<AuthorizationFilter>(authorizationFilter)
                log.info("Registering Authorization Filter [{}]", authorizationFilter)
            } else {
                addFilterBefore<AuthorizationFilter>(virtuosoHeaderAuthorizationFilter)
                log.info("Registering Virtuoso header authorization filter [{}]", virtuosoHeaderAuthorizationFilter)

            }


        }

        return http.build()
    }

    /**
     * The header filter is a bean, so Spring Boot would also register it in the servlet container,
     * outside the security filter chain. It must only run inside the chain.
     */
    @ConditionalOnWebApplication
    @Bean
    fun virtuosoHeaderAuthorizationFilterRegistration(
        virtuosoHeaderAuthorizationFilter: VirtuosoHeaderAuthorizationFilter
    ): FilterRegistrationBean<VirtuosoHeaderAuthorizationFilter> {

        return FilterRegistrationBean(virtuosoHeaderAuthorizationFilter)
            .apply { isEnabled = false }
    }

    /**
     * Cors configuration
     *
     * See [Reference documentation](https://docs.spring.io/spring-security/reference/reactive/integrations/cors.html)
     */
    @ConditionalOnWebApplication
    @Bean
    fun corsConfigurationSource(corsConfigurationProperties: CorsConfigurationProperties): CorsConfigurationSource {

        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = corsConfigurationProperties.allowedOrigins
        corsConfiguration.allowedMethods = corsConfigurationProperties.allowedMethods
        corsConfiguration.allowedHeaders = corsConfigurationProperties.allowedHeaders
        corsConfiguration.exposedHeaders = corsConfigurationProperties.exposedHeaders
        corsConfiguration.allowCredentials = corsConfigurationProperties.allowCredentials

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration(corsConfigurationProperties.pattern, corsConfiguration)

        return source
    }
}
