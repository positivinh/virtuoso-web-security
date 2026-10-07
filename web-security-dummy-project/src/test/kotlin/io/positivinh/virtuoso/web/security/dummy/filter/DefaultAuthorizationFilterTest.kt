package io.positivinh.virtuoso.web.security.dummy.filter

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@TestPropertySource(
    properties = [
        "debug=true",
        "dummy.authorization.filter.enabled=false",
        "virtuoso.web.security.authorization.headers.username=X-Dummy-Username",
        "virtuoso.web.security.authorization.headers.authorities=X-Dummy-Authorities"
    ]
)
@AutoConfigureMockMvc
class DefaultAuthorizationFilterTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Test
    fun autoconfiguredEndpointsAuthorizations() {

        mvc.perform(MockMvcRequestBuilders.get("/api/dummy"))
            .andExpect(MockMvcResultMatchers.status().isForbidden)

        mvc.perform(
            MockMvcRequestBuilders.get("/api/dummy")
                .header("X-Dummy-Username", "user")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mvc.perform(MockMvcRequestBuilders.post("/actuator/health"))
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun endpointSecuredByPermission() {

        mvc.perform(
            MockMvcRequestBuilders.get("/api/dummy/with-permission/ok")
                .header("X-Dummy-Username", "user")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mvc.perform(
            MockMvcRequestBuilders.get("/api/dummy/with-permission/nok")
                .header("X-Dummy-Username", "user")
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun blankAuthoritiesHeader_ignored() {

        mvc.perform(
            MockMvcRequestBuilders.get("/api/dummy")
                .header("X-Dummy-Username", "user")
                .header("X-Dummy-Authorities", " ; ")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun blankUsernameHeader_notAuthenticated() {

        mvc.perform(
            MockMvcRequestBuilders.get("/api/dummy")
                .header("X-Dummy-Username", " ")
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun headerFilterNotRegisteredAsServletFilter() {

        val registration = applicationContext.getBean(
            "virtuosoHeaderAuthorizationFilterRegistration",
            FilterRegistrationBean::class.java
        )

        Assertions.assertThat(registration.isEnabled).isFalse()
    }
}
