package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.delivery.DTO.StatisticsDTO
import es.unizar.webeng.hello.delivery.controller.StatisticsApiController
import es.unizar.webeng.hello.delivery.controller.StatisticsPageController
import es.unizar.webeng.hello.service.StatisticsService
import jakarta.servlet.http.HttpSession
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.ui.Model

class StatisticsControllerUnitTests {

    private lateinit var statisticsService: StatisticsService
    private lateinit var apiController: StatisticsApiController
    private lateinit var pageController: StatisticsPageController
    private val session: HttpSession = mock()
    private val model: Model = mock()

    @BeforeEach
    fun setup() {
        statisticsService = mock()
        apiController = StatisticsApiController(statisticsService)
        pageController = StatisticsPageController(statisticsService)
    }

    @Test
    fun `API returns 403 if not admin`() {
        `when`(session.getAttribute("role")).thenReturn("USER")

        val response = apiController.getStatistics(session)

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(response.body).isEqualTo("Forbidden: Admins only")
    }

    @Test
    fun `API returns stats if admin`() {
        `when`(session.getAttribute("role")).thenReturn("ADMIN")

        val stats = StatisticsDTO(3, 7, listOf("Alice" to 5L, "Bob" to 2L))
        `when`(statisticsService.getStatistics()).thenReturn(stats)

        val response = apiController.getStatistics(session)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(stats)
    }

    @Test
    fun `Page returns home with error if not admin`() {
        `when`(session.getAttribute("role")).thenReturn("USER")
        `when`(session.getAttribute("username")).thenReturn("bob")

        val result = pageController.statisticsPage(model, session)

        assertThat(result).isEqualTo("home")
    }

    @Test
    fun `Page returns statistics if admin`() {
        `when`(session.getAttribute("role")).thenReturn("ADMIN")
        `when`(session.getAttribute("username")).thenReturn("admin")

        val stats = StatisticsDTO(2, 4, listOf("Alice" to 3L, "Bob" to 1L))
        `when`(statisticsService.getStatistics()).thenReturn(stats)

        val result = pageController.statisticsPage(model, session)

        assertThat(result).isEqualTo("statistics")
    }
}
