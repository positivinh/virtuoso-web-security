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
@EnableConfigurationProperties(value = [EndpointAuthorizationConfigurationProperties::class])
class EndpointAuthorizationConfigurationPropertiesTest {

    @Autowired
    private lateinit var properties: EndpointAuthorizationConfigurationProperties

    @Test
    fun parseWebSecurityConfigurationProperties() {

        Assertions.assertThat(properties).isNotNull

        Assertions.assertThat(properties.authenticated).isNotEmpty()
        Assertions.assertThat(properties.authenticated).contains(Endpoint(pattern = "/api/**"))

        Assertions.assertThat(properties.permitAll).isNotEmpty()
        Assertions.assertThat(properties.permitAll)
            .contains(Endpoint(name = "actuator", pattern = "/actuator/**", method = "GET"))

        Assertions.assertThat(properties.denyAll).isEmpty()
    }

}