package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.controller.GreetingHistoryController
import es.unizar.webeng.hello.service.GreetingService
import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Instant

@WebMvcTest(GreetingHistoryController::class)
@ActiveProfiles("test")
class GreetingHistoryControllerMVCTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var greetingService: GreetingService

    @Test
    fun `GET all history returns 200`() {
        val alice = User(1, "alice", "pw", Role.USER)
        val bob = User(2, "bob", "pw", Role.USER)
        val h1 = GreetingHistory(1, "Good Morning, Alice!", Instant.now(), alice)
        val h2 = GreetingHistory(2, "Good Morning, Bob!", Instant.now(), bob)

        `when`(greetingService.getAllHistory()).thenReturn(listOf(h1, h2))

        mockMvc.get("/api/history")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(2) }
                jsonPath("$[0].message") { value("Good Morning, Alice!") }
                jsonPath("$[1].message") { value("Good Morning, Bob!") }
            }
    }

    @Test
    fun `GET user history returns 200`() {
        val alice = User(1, "alice", "pw", Role.USER)
        val h1 = GreetingHistory(1, "Good Morning, Alice!", Instant.now(), alice)
        val h2 = GreetingHistory(2, "Good Evening, Alice!", Instant.now(), alice)

        `when`(greetingService.getHistoryForUser("alice")).thenReturn(listOf(h1, h2))

        mockMvc.get("/api/history/alice")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(2) }
                jsonPath("$[0].message") { value("Good Morning, Alice!") }
                jsonPath("$[1].message") { value("Good Evening, Alice!") }
            }
    }
}
