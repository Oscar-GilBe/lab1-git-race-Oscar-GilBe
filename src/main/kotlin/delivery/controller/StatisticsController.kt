package es.unizar.webeng.hello.delivery.controller

import es.unizar.webeng.hello.service.StatisticsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.http.ResponseEntity
import jakarta.servlet.http.HttpSession

/**
 * Controlador REST que expone las estadísticas de la aplicación vía API.
 *
 * Proporciona los datos en formato JSON.
 *
 * @property statisticsService Servicio encargado de calcular las estadísticas.
 */
@RestController
class StatisticsApiController(private val statisticsService: StatisticsService) {

    /**
     * Devuelve las estadísticas actuales en formato JSON.
     * Solo accesible para administradores.
     *
     * @return Objeto con las métricas de la aplicación.
     */
    @GetMapping("/api/statistics")
    fun getStatistics(session: HttpSession): ResponseEntity<Any> {
        val role = session.getAttribute("role") as? String
        return if (role == "ADMIN") {
            ResponseEntity.ok(statisticsService.getStatistics())
        } else {
            ResponseEntity.status(403).body("Forbidden: Admins only")
        }
    }
}

/**
 * Controlador MVC que renderiza la página de estadísticas en la vista Thymeleaf.
 *
 * Inserta en el modelo los valores calculados por [StatisticsService] para ser
 * representados en la plantilla HTML.
 *
 * @property statisticsService Servicio encargado de calcular las estadísticas.
 */
@Controller
class StatisticsPageController(private val statisticsService: StatisticsService) {

    /**
     * Renderiza la página de estadísticas con los datos calculados.
     * Solo accesible para administradores.
     * 
     * @param model Objeto de modelo usado para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf a renderizar.
     */
    @GetMapping("/statistics")
    fun statisticsPage(model: Model, session: HttpSession): String {
        val role = session.getAttribute("role")?.toString() ?: "USER"
        val username = session.getAttribute("username") as? String ?: "Anonymus"

        model.addAttribute("username", username)
        model.addAttribute("role", role)

        if (role != "ADMIN") {
            model.addAttribute("error", "No tienes permisos para ver las estadísticas")
            return "home"
        }

        val stats = statisticsService.getStatistics()
        model.addAttribute("totalUsers", stats.totalUsers)
        model.addAttribute("totalGreetings", stats.totalGreetings)
        model.addAttribute("top3Names", stats.top3Names)
        return "statistics"
    }
}
