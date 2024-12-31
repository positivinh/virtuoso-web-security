package io.positivinh.virtuoso.web.security.autoconfigure.configuration

import io.positivinh.virtuoso.web.security.autoconfigure.WebSecurityAutoConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(
    classes = [WebSecurityAutoConfiguration::class],
    initializers = [ConfigDataApplicationContextInitializer::class]
)
@EnableConfigurationProperties(value = [CorsConfigurationProperties::class])
class CorsConfigurationPropertiesTest {

    @Autowired
    private lateinit var corsConfigurationProperties: CorsConfigurationProperties

    @Test
    fun parseCorsConfiguration() {

        Assertions.assertThat(corsConfigurationProperties.allowedOrigins).isEqualTo(listOf("*"))
        Assertions.assertThat(corsConfigurationProperties.allowedMethods).isEqualTo(listOf("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"))
        Assertions.assertThat(corsConfigurationProperties.allowedHeaders).isEqualTo(listOf("*"))
        Assertions.assertThat(corsConfigurationProperties.exposedHeaders).isEqualTo(listOf("*"))
        Assertions.assertThat(corsConfigurationProperties.allowCredentials).isTrue()
    }
}