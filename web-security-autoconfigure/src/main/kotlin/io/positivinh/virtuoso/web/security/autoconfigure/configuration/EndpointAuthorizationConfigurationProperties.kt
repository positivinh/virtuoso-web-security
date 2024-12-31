package io.positivinh.virtuoso.web.security.autoconfigure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "virtuoso.web.security.endpoints.authorizations")
data class EndpointAuthorizationConfigurationProperties(
    val authenticated: MutableList<Endpoint> = mutableListOf(),
    val permitAll: MutableList<Endpoint> = mutableListOf(),
    val denyAll: MutableList<Endpoint> = mutableListOf(),
)

data class Endpoint (
    val name: String? = null,
    val pattern: String,
    val method: String? = null,
)