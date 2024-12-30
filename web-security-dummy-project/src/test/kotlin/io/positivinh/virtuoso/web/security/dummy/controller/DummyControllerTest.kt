package io.positivinh.virtuoso.web.security.dummy.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class DummyControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun autoconfiguredEndpointsAuthorizations() {

        mvc.perform(MockMvcRequestBuilders.get("/api/dummy"))
            .andExpect(MockMvcResultMatchers.status().isForbidden)

        mvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mvc.perform(MockMvcRequestBuilders.post("/actuator/health"))
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}