package io.positivinh.virtuoso.web.security.dummy.filter

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ComponentScan(
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [AuthorizationFilter::class]
    )]
)
@TestPropertySource(
    properties = [
        "debug=true"
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
                .header("X-Virtuoso-Username", "user")
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
                .header("X-Virtuoso-Username", "user")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mvc.perform(
            MockMvcRequestBuilders.get("/api/dummy/with-permission/nok")
                .header("X-Virtuoso-Username", "user")
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}