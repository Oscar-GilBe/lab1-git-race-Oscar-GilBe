package es.unizar.webeng.hello.delivery.controller

import es.unizar.webeng.hello.delivery.DTO.GreetingHistoryDTO
import es.unizar.webeng.hello.service.GreetingService
import org.springframework.http.ResponseEntity
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/history")
class GreetingHistoryController(
    private val greetingService: GreetingService
) {

    @GetMapping
    fun getAllHistory(): ResponseEntity<List<GreetingHistoryDTO>> {
        val history = greetingService.getAllHistory().map {
            GreetingHistoryDTO.fromEntity(it)
        }
        return ResponseEntity.ok(history)
    }

    @GetMapping("/{username}")
    fun getUserHistory(@PathVariable username: String): ResponseEntity<List<GreetingHistoryDTO>> {
        val history = greetingService.getHistoryForUser(username).map {
            GreetingHistoryDTO.fromEntity(it)
        }
        return ResponseEntity.ok(history)
    }
}

@Controller
@RequestMapping("/history")
class GreetingHistoryPageController(
    private val greetingService: GreetingService
) {

    @GetMapping("/{username}")
    fun myHistory(@PathVariable username: String, model: Model, session: HttpSession): String {
        val sessionUser = session.getAttribute("username") as String?
        // Only allow watch one's own history, except admin
        val role = session.getAttribute("role")?.toString() ?: "GUEST"
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

    @GetMapping("/all")
    fun allUsersHistory(model: Model, session: HttpSession): String {
        val role = session.getAttribute("role")?.toString() ?: "GUEST"
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
