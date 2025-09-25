package es.unizar.webeng.hello.delivery.controller

import es.unizar.webeng.hello.delivery.DTO.GreetingHistoryDTO
import es.unizar.webeng.hello.service.GreetingService
import org.springframework.http.ResponseEntity
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * Controlador REST empleado para exponer el historial de saludos vía API.
 *
 * Proporciona endpoints para obtener el historial completo
 * o el historial filtrado por usuario.
 *
 * @property greetingService Servicio encargado de acceder al historial de saludos.
 */
@RestController
@RequestMapping("/api/history")
class GreetingHistoryController(
    private val greetingService: GreetingService
) {

    /**
     * Obtiene el historial completo de saludos.
     *
     * @return Una lista de [GreetingHistoryDTO] con todos los saludos.
     */
    @GetMapping
    fun getAllHistory(): ResponseEntity<List<GreetingHistoryDTO>> {
        val history = greetingService.getAllHistory().map {
            GreetingHistoryDTO.fromEntity(it)
        }
        return ResponseEntity.ok(history)
    }

    /**
     * Obtiene el historial de saludos de un usuario específico.
     *
     * @param username Nombre del usuario.
     * @return Una lista de [GreetingHistoryDTO] correspondiente al usuario.
     */
    @GetMapping("/{username}")
    fun getUserHistory(@PathVariable username: String): ResponseEntity<List<GreetingHistoryDTO>> {
        val history = greetingService.getHistoryForUser(username).map {
            GreetingHistoryDTO.fromEntity(it)
        }
        return ResponseEntity.ok(history)
    }
}

/**
 * Controlador MVC para mostrar el historial de saludos en vistas Thymeleaf.
 *
 * Incluye validaciones de permisos: solo los administradores pueden
 * ver historiales completos, y cada usuario puede ver únicamente el suyo,
 * salvo que tenga rol ADMIN.
 *
 * @property greetingService Servicio encargado de acceder al historial de saludos.
 */
@Controller
@RequestMapping("/history")
class GreetingHistoryPageController(
    private val greetingService: GreetingService
) {

    /**
     * Muestra el historial de saludos de un usuario.
     * Verifica permisos: un usuario solo puede ver su propio historial,
     * salvo que tenga rol ADMIN.
     *
     * @param username Usuario del historial solicitado.
     * @param model Modelo de atributos para la vista.
     * @param session Sesión HTTP con datos de usuario y rol.
     * @return Vista `history` si se permite el acceso, o `home` con error.
     */
    @GetMapping("/{username}")
    fun myHistory(@PathVariable username: String, model: Model, session: HttpSession): String {
        val sessionUser = session.getAttribute("username") as String?
        // Only allow watch one's own history, except admin
        val role = session.getAttribute("role")?.toString() ?: "USER"
        if (sessionUser != username && role != "ADMIN") {
            model.addAttribute("error", "No tienes permiso para ver este historial")
            model.addAttribute("username", sessionUser ?: "anonymous")
            model.addAttribute("role", role)
            return "home"
        }

        val history = greetingService.getHistoryForUser(username).map { GreetingHistoryDTO.fromEntity(it) }
        model.addAttribute("history", history)
        model.addAttribute("username", sessionUser ?: "anonymous")
        model.addAttribute("role", role)
        model.addAttribute("viewTitle", username)
        return "history"
    }

    /**
     * Muestra el historial de saludos de todos los usuarios.
     * Solo accesible para usuarios con rol ADMIN.
     *
     * @param model Modelo de atributos para la vista.
     * @param session Sesión HTTP con rol del usuario.
     * @return Vista `history` con todos los registros, o `home` con error.
     */
    @GetMapping("/all")
    fun allUsersHistory(model: Model, session: HttpSession): String {
        val role = session.getAttribute("role")?.toString() ?: "USER"
        val sessionUser = session.getAttribute("username") as String?
        
        if (role != "ADMIN") {
            model.addAttribute("error", "No tienes permiso para ver todos los historiales")
            model.addAttribute("username", sessionUser ?: "anonymous")
            model.addAttribute("role", role)
            return "home"
        }

        val history = greetingService.getAllHistory().map { GreetingHistoryDTO.fromEntity(it) }
        model.addAttribute("history", history)
        model.addAttribute("username", sessionUser ?: "anonymous")
        model.addAttribute("role", role)
        model.addAttribute("viewTitle", "All Users")
        return "history"
    }

    /**
     * Redirige al historial del usuario en sesión, si existe.
     * Si no hay usuario en sesión, redirige a la página de login.
     *
     * @param session Sesión HTTP actual.
     * @return Redirección a `/history/{username}` o a `/login`.
     */
    @GetMapping
    fun redirectToMyHistory(session: HttpSession): String {
        val username = session.getAttribute("username") as String?
        return if (username != null) {
            "redirect:/history/$username"
        } else {
            "redirect:/login"
        }
    }
}
