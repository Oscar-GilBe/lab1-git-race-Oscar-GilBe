package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.GreetingHistoryRepository
import es.unizar.webeng.hello.repository.UserRepository
import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.core.enum.Role
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.*

class GreetingServiceTest {

    private val historyRepository: GreetingHistoryRepository = mock(GreetingHistoryRepository::class.java)
    private val userRepository: UserRepository = mock(UserRepository::class.java)

    @Test
    fun `should return Good Morning at 9 AM`() {
        val fixedClock = Clock.fixed(
            LocalDateTime.of(2025, 1, 1, 9, 0).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        )
        `when`(userRepository.findByUsername("Alice")).thenReturn(null)

        val service = GreetingService(historyRepository, userRepository, fixedClock)

        val greeting = service.getGreeting("Alice")

        assertThat(greeting).isEqualTo("Good Morning, Alice!")
    }

    @Test
    fun `should return Good Afternoon at 17 PM`() {
        val fixedClock = Clock.fixed(
            LocalDateTime.of(2025, 1, 1, 17, 0).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        )
        `when`(userRepository.findByUsername("Bob")).thenReturn(null)

        val service = GreetingService(historyRepository, userRepository, fixedClock)

        val greeting = service.getGreeting("Bob")

        assertThat(greeting).isEqualTo("Good Afternoon, Bob!")
    }

    @Test
    fun `should return Good Evening at 22 PM`() {
        val fixedClock = Clock.fixed(
            LocalDateTime.of(2025, 1, 1, 22, 0).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        )
        `when`(userRepository.findByUsername("Carol")).thenReturn(null)

        val service = GreetingService(historyRepository, userRepository, fixedClock)

        val greeting = service.getGreeting("Carol")

        assertThat(greeting).isEqualTo("Good Evening, Carol!")
    }

    @Test
    fun `should return history for existing user`() {
        val user = User(id = 1L, username = "Alice", password = "secret", role = Role.USER)
        val historyEntries = listOf(
            GreetingHistory(id = 1L, message = "Good Morning, Alice!", user = user),
            GreetingHistory(id = 2L, message = "Good Afternoon, Alice!", user = user)
        )

        `when`(userRepository.findByUsername("Alice")).thenReturn(user)
        `when`(historyRepository.findByUser(user)).thenReturn(historyEntries)

        val service = GreetingService(historyRepository, userRepository, Clock.systemUTC())
        val result = service.getHistoryForUser("Alice")

        assertThat(result).hasSize(2)
        assertThat(result[0].message).isEqualTo("Good Morning, Alice!")
        assertThat(result[1].message).isEqualTo("Good Afternoon, Alice!")
    }

    @Test
    fun `should throw exception when user not found`() {
        `when`(userRepository.findByUsername("Ghost")).thenReturn(null)

        val service = GreetingService(historyRepository, userRepository, Clock.systemUTC())

        assertThatThrownBy { service.getHistoryForUser("Ghost") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("User not found")
    }

    @Test
    fun `should return all history`() {
        val user1 = User(id = 1L, username = "Alice", password = "secret", role = Role.USER)
        val user2 = User(id = 2L, username = "Bob", password = "secret2", role = Role.USER)

        val historyEntries = listOf(
            GreetingHistory(id = 1L, message = "Good Morning, Alice!", user = user1),
            GreetingHistory(id = 2L, message = "Good Afternoon, Bob!", user = user2)
        )

        `when`(historyRepository.findAll()).thenReturn(historyEntries)

        val service = GreetingService(historyRepository, userRepository, Clock.systemUTC())
        val result = service.getAllHistory()

        assertThat(result).hasSize(2)
        assertThat(result.map { it.message }).containsExactly("Good Morning, Alice!", "Good Afternoon, Bob!")
    }
}
