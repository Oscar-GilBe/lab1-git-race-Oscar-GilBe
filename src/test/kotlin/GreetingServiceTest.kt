package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.GreetingHistoryRepository
import es.unizar.webeng.hello.repository.UserRepository
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
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

        val service = GreetingService(historyRepository, userRepository,fixedClock)

        val greeting = service.getGreeting("Carol")

        assertThat(greeting).isEqualTo("Good Evening, Carol!")
    }
}
