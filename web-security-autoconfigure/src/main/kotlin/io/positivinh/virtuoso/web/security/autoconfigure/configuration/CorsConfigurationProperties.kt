package io.positivinh.virtuoso.web.security.autoconfigure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "virtuoso.web.security.cors")
data class CorsConfigurationProperties(

    val pattern: String = "/**",
    val allowedOrigins: List<String>? = listOf("*"),
    val allowedMethods: List<String>? = listOf("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"),
    val allowedHeaders: List<String>? = listOf("*"),
    val exposedHeaders: List<String>? = listOf("*"),
    val allowCredentials: Boolean = true,
)