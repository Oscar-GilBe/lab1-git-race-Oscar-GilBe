package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.delivery.controller.StatisticsApiController
import es.unizar.webeng.hello.delivery.controller.StatisticsPageController
import es.unizar.webeng.hello.delivery.DTO.StatisticsDTO
import es.unizar.webeng.hello.service.StatisticsService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [StatisticsApiController::class, StatisticsPageController::class])
@ActiveProfiles("test")
class StatisticsControllerMVCTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var statisticsService: StatisticsService

    @Test
    fun `API returns 403 if user not admin`() {
        mockMvc.get("/api/statistics") {
            sessionAttr("role", "USER")
        }.andExpect {
            status { isForbidden() }
            content { string("Forbidden: Admins only") }
        }
    }

    @Test
    fun `API returns stats if admin`() {
        val stats = StatisticsDTO(
            totalUsers = 10L,
            totalGreetings = 50L,
            top3Names = listOf("Alice" to 20L, "Bob" to 15L, "Carol" to 10L)
        )
        `when`(statisticsService.getStatistics()).thenReturn(stats)

        mockMvc.get("/api/statistics") {
            sessionAttr("role", "ADMIN")
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.totalUsers") { value(10) }
            jsonPath("$.totalGreetings") { value(50) }
            jsonPath("$.top3Names.length()") { value(3) }
        }
    }

    @Test
    fun `MVC shows error if non-admin accesses statistics`() {
        mockMvc.get("/statistics") {
            sessionAttr("username", "alice")
            sessionAttr("role", "USER")
        }.andExpect {
            status { isOk() }
            content { string(containsString("No tienes permisos para ver las estadísticas")) }
        }
    }

    @Test
    fun `MVC shows statistics if admin`() {
        val stats = StatisticsDTO(
            totalUsers = 5L,
            totalGreetings = 12L,
            top3Names = listOf("Alice" to 6L, "Bob" to 4L, "Carol" to 2L)
        )
        `when`(statisticsService.getStatistics()).thenReturn(stats)

        mockMvc.get("/statistics") {
            sessionAttr("username", "admin")
            sessionAttr("role", "ADMIN")
        }.andExpect {
            status { isOk() }
            content { string(containsString("5")) }
            content { string(containsString("12")) }
            content { string(containsString("Alice")) }
            content { string(containsString("Bob")) }
            content { string(containsString("Carol")) }
        }
    }
}
