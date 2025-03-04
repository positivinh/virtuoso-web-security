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
@EnableConfigurationProperties(value = [AuthorizationHeadersConfigurationProperties::class])
class AuthorizationHeadersConfigurationPropertiesTest {

    @Autowired
    private lateinit var authorizationHeadersConfigurationProperties: AuthorizationHeadersConfigurationProperties

    @Test
    fun parseAuthorizationHeadersConfigurationProperties() {

        Assertions.assertThat(authorizationHeadersConfigurationProperties.username).isEqualTo("X-Virtuoso-Username")
        Assertions.assertThat(authorizationHeadersConfigurationProperties.authorities)
            .isEqualTo("X-Virtuoso-Authorities")
    }
}