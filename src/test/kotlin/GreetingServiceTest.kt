package es.unizar.webeng.hello.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.*

class GreetingServiceTest {

    @Test
    fun `should return Good Morning at 9 AM`() {
        val fixedClock = Clock.fixed(
            LocalDateTime.of(2025, 1, 1, 9, 0).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        )
        val service = GreetingService(fixedClock)

        val greeting = service.getGreeting()

        assertThat(greeting).isEqualTo("Good Morning")
    }

    @Test
    fun `should return Good Afternoon at 17 PM`() {
        val fixedClock = Clock.fixed(
            LocalDateTime.of(2025, 1, 1, 17, 0).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        )
        val service = GreetingService(fixedClock)

        val greeting = service.getGreeting()

        assertThat(greeting).isEqualTo("Good Afternoon")
    }

    @Test
    fun `should return Good Evening at 22 PM`() {
        val fixedClock = Clock.fixed(
            LocalDateTime.of(2025, 1, 1, 22, 0).toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        )
        val service = GreetingService(fixedClock)

        val greeting = service.getGreeting()

        assertThat(greeting).isEqualTo("Good Evening")
    }
}
