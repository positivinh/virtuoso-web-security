package io.positivinh.virtuoso.web.security.dummy

import io.positivinh.virtuoso.web.security.autoconfigure.WebSecurityAutoConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.WebApplicationContextRunner
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler

/**
 * Importing only [WebSecurityAutoConfiguration] (as `@WebMvcTest` slices do) must still enable method security,
 * otherwise `@PreAuthorize` would silently not apply.
 */
class MethodSecurityImportTest {

    @Test
    fun webSecurityAlone_enablesMethodSecurity() {

        WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(WebSecurityAutoConfiguration::class.java))
            .run { context ->
                Assertions.assertThat(context).hasNotFailed()
                Assertions.assertThat(context).hasSingleBean(MethodSecurityExpressionHandler::class.java)
                // registered by @EnableMethodSecurity(prePostEnabled = true)
                Assertions.assertThat(context.containsBean("preAuthorizeAuthorizationMethodInterceptor")).isTrue()
            }
    }
}
