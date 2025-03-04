package io.positivinh.virtuoso.web.security.autoconfigure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "virtuoso.web.security.authorization.headers")
data class AuthorizationHeadersConfigurationProperties(

    val username: String = "X-Virtuoso-Username",

    val authorities: String = "X-Virtuoso-Authorities"
)
