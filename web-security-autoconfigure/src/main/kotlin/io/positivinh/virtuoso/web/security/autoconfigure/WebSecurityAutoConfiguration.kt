package io.positivinh.virtuoso.web.security.autoconfigure

import io.positivinh.virtuoso.security.autoconfigure.configuration.MethodSecurityConfiguration
import io.positivinh.virtuoso.web.security.autoconfigure.configuration.AuthorizationHeadersConfigurationProperties
import io.positivinh.virtuoso.web.security.autoconfigure.configuration.CorsConfigurationProperties
import io.positivinh.virtuoso.web.security.autoconfigure.configuration.EndpointAuthorizationConfigurationProperties
import io.positivinh.virtuoso.web.security.autoconfigure.configuration.SpringSecurityConfiguration
import io.positivinh.virtuoso.web.security.autoconfigure.filter.VirtuosoHeaderAuthorizationFilter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@AutoConfiguration
@ConditionalOnClass(EnableWebSecurity::class)
// web security always comes with method security (virtuoso-security), also when this auto-configuration is imported
// on its own, e.g. in a @WebMvcTest slice; otherwise @PreAuthorize would silently not apply
@Import(
    SpringSecurityConfiguration::class,
    VirtuosoHeaderAuthorizationFilter::class,
    MethodSecurityConfiguration::class
)
@EnableConfigurationProperties(value = [EndpointAuthorizationConfigurationProperties::class, AuthorizationHeadersConfigurationProperties::class, CorsConfigurationProperties::class])
@PropertySource("classpath:web-security.properties")
class WebSecurityAutoConfiguration