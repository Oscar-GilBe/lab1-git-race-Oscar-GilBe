package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.controller.GreetingHistoryPageController
import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.service.GreetingService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Instant

@WebMvcTest(GreetingHistoryPageController::class)
@ActiveProfiles("test")
class GreetingHistoryPageControllerMVCTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var greetingService: GreetingService

    @Test
    fun `redirects to login if session user missing`() {
        mockMvc.get("/history")
            .andExpect {
                status { is3xxRedirection() }
                redirectedUrl("/login")
            }
    }

    @Test
    fun `shows error if non-admin accesses other user's history`() {
        mockMvc.get("/history/bob") {
            sessionAttr("username", "alice")
            sessionAttr("role", "USER")
        }.andExpect {
            status { isOk() }
            content { string(containsString("No tienes permiso para ver este historial")) }
        }
    }

    @Test
    fun `admin can see all users history`() {
        val alice = User(1, "alice", "pw", Role.USER)
        val bob = User(2, "bob", "pw", Role.ADMIN)
        val h1 = GreetingHistory(1, "Good Morning, Alice!", Instant.now(), alice)
        val h2 = GreetingHistory(2, "Good Morning, Bob!", Instant.now(), bob)

        `when`(greetingService.getAllHistory()).thenReturn(listOf(h1, h2))

        mockMvc.get("/history/all") {
            sessionAttr("username", "admin")
            sessionAttr("role", "ADMIN")
        }.andExpect {
            status { isOk() }
            content { string(containsString("Good Morning, Alice!")) }
            content { string(containsString("Good Morning, Bob!")) }
        }
    }

    @Test
    fun `user can see own history`() {
        val alice = User(1, "alice", "pw", Role.USER)
        val h1 = GreetingHistory(1, "Good Morning, Alice!", Instant.now(), alice)
        val h2 = GreetingHistory(2, "Good Evening, Alice!", Instant.now(), alice)

        `when`(greetingService.getHistoryForUser("alice")).thenReturn(listOf(h1, h2))

        mockMvc.get("/history/alice") {
            sessionAttr("username", "alice")
            sessionAttr("role", "USER")
        }.andExpect {
            status { isOk() }
            content { string(containsString("Good Morning, Alice!")) }
            content { string(containsString("Good Evening, Alice!")) }
        }
    }
}
