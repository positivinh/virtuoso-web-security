package io.positivinh.virtuoso.web.security.dummy.filter

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * A deny-all rule wins over a broader permit-all rule.
 */
@SpringBootTest
@TestPropertySource(
    properties = [
        "dummy.authorization.filter.enabled=false",
        "virtuoso.web.security.endpoints.authorizations.permit-all[0].pattern=/api/dummy/**",
        "virtuoso.web.security.endpoints.authorizations.deny-all[0].pattern=/api/dummy/with-permission/**",
    ]
)
@AutoConfigureMockMvc
class DenyAllPrecedenceTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun permitAll() {

        mvc.perform(MockMvcRequestBuilders.get("/api/dummy"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun denyAllOverridesBroaderPermitAll() {

        mvc.perform(
            MockMvcRequestBuilders.get("/api/dummy/with-permission/ok")
                .header("X-Virtuoso-Username", "user")
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}
