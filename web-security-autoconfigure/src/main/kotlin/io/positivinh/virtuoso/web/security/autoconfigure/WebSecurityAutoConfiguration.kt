package io.positivinh.virtuoso.web.security.autoconfigure

import io.positivinh.virtuoso.web.security.autoconfigure.configuration.SpringSecurityConfiguration
import io.positivinh.virtuoso.web.security.autoconfigure.filter.VirtuosoHeaderAuthorizationFilter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@AutoConfiguration
@ConditionalOnClass(EnableWebSecurity::class)
@Import(SpringSecurityConfiguration::class, VirtuosoHeaderAuthorizationFilter::class)
@ConfigurationPropertiesScan
@PropertySource("classpath:web-security.properties")
class WebSecurityAutoConfiguration