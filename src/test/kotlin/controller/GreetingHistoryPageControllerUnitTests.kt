package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.controller.GreetingHistoryPageController
import es.unizar.webeng.hello.delivery.DTO.GreetingHistoryDTO
import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.service.GreetingService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import jakarta.servlet.http.HttpSession
import org.springframework.ui.Model
import java.time.Instant

class GreetingHistoryPageControllerUnitTests {

    private lateinit var greetingService: GreetingService
    private lateinit var controller: GreetingHistoryPageController
    private val model: Model = mock()
    private val session: HttpSession = mock()

    @BeforeEach
    fun setup() {
        greetingService = mock()
        controller = GreetingHistoryPageController(greetingService)
    }

    @Test
    fun `redirects to login if session username null`() {
        whenever(session.getAttribute("username")).thenReturn(null)

        val result = controller.redirectToMyHistory(session)
        assertThat(result).isEqualTo("redirect:/login")
    }

    @Test
    fun `redirects to own history if session username exists`() {
        whenever(session.getAttribute("username")).thenReturn("alice")

        val result = controller.redirectToMyHistory(session)
        assertThat(result).isEqualTo("redirect:/history/alice")
    }

    @Test
    fun `admin can access all users history`() {
        val alice = User(1, "alice", "pw", Role.USER)
        val bob = User(2, "bob", "pw", Role.ADMIN)
        val h1 = GreetingHistory(1, "Good Morning, Alice!", Instant.now(), alice)
        val h2 = GreetingHistory(2, "Good Morning, Bob!", Instant.now(), bob)

        whenever(session.getAttribute("role")).thenReturn("ADMIN")
        whenever(session.getAttribute("username")).thenReturn("admin")
        whenever(greetingService.getAllHistory()).thenReturn(listOf(h1, h2))

        val result = controller.allUsersHistory(model, session)
        assertThat(result).isEqualTo("history")
    }

    @Test
    fun `non-admin cannot access other user's history`() {
        whenever(session.getAttribute("username")).thenReturn("alice")
        whenever(session.getAttribute("role")).thenReturn("USER")

        val result = controller.myHistory("bob", model, session)
        assertThat(result).isEqualTo("home")
    }

    @Test
    fun `user can see their own history`() {
        val alice = User(1, "alice", "pw", Role.USER)
        val h1 = GreetingHistory(1, "Good Morning, Alice!", Instant.now(), alice)
        val h2 = GreetingHistory(2, "Good Evening, Alice!", Instant.now(), alice)

        whenever(session.getAttribute("username")).thenReturn("alice")
        whenever(session.getAttribute("role")).thenReturn("USER")
        whenever(greetingService.getHistoryForUser("alice")).thenReturn(listOf(h1, h2))

        val result = controller.myHistory("alice", model, session)
        assertThat(result).isEqualTo("history")
    }
}
