package io.positivinh.virtuoso.web.security.dummy.filter

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
}