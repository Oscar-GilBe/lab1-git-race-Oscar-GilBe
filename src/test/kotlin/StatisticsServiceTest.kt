package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.GreetingHistoryRepository
import es.unizar.webeng.hello.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class StatisticsServiceTest {

    private val historyRepository: GreetingHistoryRepository = mock(GreetingHistoryRepository::class.java)
    private val userRepository: UserRepository = mock(UserRepository::class.java)

    @Test
    fun `should return statistics with top 3 names`() {
        `when`(userRepository.count()).thenReturn(5L)
        `when`(historyRepository.count()).thenReturn(20L)

        `when`(historyRepository.countGreetingsGroupedByUser()).thenReturn(
            listOf(
                arrayOf("Alice", 10L),
                arrayOf("Bob", 5L),
                arrayOf("Carol", 3L),
                arrayOf("Dave", 2L)
            )
        )

        val service = StatisticsService(historyRepository, userRepository)
        val stats = service.getStatistics()

        assertThat(stats.totalUsers).isEqualTo(5L)
        assertThat(stats.totalGreetings).isEqualTo(20L)
        assertThat(stats.top3Names).containsExactly(
            "Alice" to 10L,
            "Bob" to 5L,
            "Carol" to 3L
        )
    }

    @Test
    fun `should handle empty greetings`() {
        `when`(userRepository.count()).thenReturn(0L)
        `when`(historyRepository.count()).thenReturn(0L)
        `when`(historyRepository.countGreetingsGroupedByUser()).thenReturn(emptyList())

        val service = StatisticsService(historyRepository, userRepository)
        val stats = service.getStatistics()

        assertThat(stats.totalUsers).isEqualTo(0L)
        assertThat(stats.totalGreetings).isEqualTo(0L)
        assertThat(stats.top3Names).isEmpty()
    }

    @Test
    fun `should truncate top names to 3`() {
        `when`(userRepository.count()).thenReturn(10L)
        `when`(historyRepository.count()).thenReturn(50L)

        `when`(historyRepository.countGreetingsGroupedByUser()).thenReturn(
            listOf(
                arrayOf("Alice", 15L),
                arrayOf("Bob", 14L),
                arrayOf("Carol", 13L),
                arrayOf("Dave", 12L),
                arrayOf("Eve", 11L)
            )
        )

        val service = StatisticsService(historyRepository, userRepository)
        val stats = service.getStatistics()

        assertThat(stats.totalUsers).isEqualTo(10L)
        assertThat(stats.totalGreetings).isEqualTo(50L)
        assertThat(stats.top3Names).hasSize(3)
        assertThat(stats.top3Names.map { it.first }).containsExactly("Alice", "Bob", "Carol")
    }
}
