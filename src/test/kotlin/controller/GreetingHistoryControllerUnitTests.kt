package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.DTO.GreetingHistoryDTO
import es.unizar.webeng.hello.delivery.controller.GreetingHistoryController
import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.service.GreetingService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.time.Instant

class GreetingHistoryControllerUnitTests {

    private lateinit var greetingService: GreetingService
    private lateinit var controller: GreetingHistoryController

    @BeforeEach
    fun setup() {
        greetingService = mock()
        controller = GreetingHistoryController(greetingService)
    }

    @Test
    fun `get all history returns list`() {
        val user1 = User(id = 1, username = "alice", password = "pw", role = Role.USER)
        val user2 = User(id = 2, username = "bob", password = "pw2", role = Role.ADMIN)

        val entity1 = GreetingHistory(id = 1, message = "Good Morning, Alice!", timestamp = Instant.now(), user = user1)
        val entity2 = GreetingHistory(id = 2, message = "Good Morning, Bob!", timestamp = Instant.now(), user = user2)

        `when`(greetingService.getAllHistory()).thenReturn(listOf(entity1, entity2))

        val response = controller.getAllHistory()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).hasSize(2)
        assertThat(response.body!![0]).isInstanceOf(GreetingHistoryDTO::class.java)
        assertThat(response.body!![1]).isInstanceOf(GreetingHistoryDTO::class.java)
        assertThat(response.body!!.map { it.username }).containsExactlyInAnyOrder("alice", "bob")
    }

    @Test
    fun `get user history returns list`() {
        val user = User(id = 2, username = "bob", password = "pw2", role = Role.ADMIN)
        val entity = GreetingHistory(id = 2, message = "Good Morning, Bob!", timestamp = Instant.now(), user = user)
        `when`(greetingService.getHistoryForUser("bob")).thenReturn(listOf(entity))

        val response = controller.getUserHistory("bob")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).hasSize(1)
        assertThat(response.body!![0].username).isEqualTo("bob")
    }
}
