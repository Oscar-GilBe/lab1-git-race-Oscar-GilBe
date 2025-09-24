package es.unizar.webeng.hello

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.hamcrest.Matchers.containsString
import org.springframework.http.HttpStatus

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = ["spring.profiles.include=ratelimiter"])
class RateLimitFilterTest(
    @Autowired val mockMvc: MockMvc
) {

    @Test
    fun `rate limiter allows up to 5 requests and blocks the 6th`() {
        repeat(5) { i ->
            mockMvc.get("/api/hello?name=Test$i")
                .andExpect {
                    status { isOk() } // 200 status code = OK
                    header { exists("X-Rate-Limit-Remaining") }
                }
        }

        mockMvc.get("/api/hello?name=Blocked")
            .andExpect {
                status { isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value()) } // 429 status code = Too Many Requests
                content { string(containsString("Too Many Requests")) }
                header { exists("Retry-After") }
            }
    }
}
