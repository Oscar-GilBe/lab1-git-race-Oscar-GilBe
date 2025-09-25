package es.unizar.webeng.hello.delivery.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.DTO.UserDTO
import es.unizar.webeng.hello.service.UserService
import es.unizar.webeng.hello.service.GreetingService
import org.springframework.stereotype.Controller
import jakarta.servlet.http.HttpSession
import org.springframework.ui.Model
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(
        @RequestParam username: String,
        @RequestParam password: String,
        @RequestParam role: Role
    ): ResponseEntity<UserDTO> {
        val user = userService.createUser(username, password, role)
        return ResponseEntity.ok(UserDTO.fromEntity(user))
    }

    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): ResponseEntity<String> {
        return if (userService.validatePassword(username, password)) {
            ResponseEntity.ok("Login successful")
        } else {
            ResponseEntity.status(401).body("Invalid username or password")
        }
    }

    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String): ResponseEntity<UserDTO> {
        val user = userService.getUser(username)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserDTO.fromEntity(user))
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAllUsers().map { UserDTO.fromEntity(it) }
        return ResponseEntity.ok(users)
    }

    @DeleteMapping("/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Void> {
        userService.deleteUser(username)
        return ResponseEntity.noContent().build()
    }
}

@Controller
class AuthPageController(
    private val userService: UserService,
    private val greetingService: GreetingService
) {

    @GetMapping("/login")
    fun loginPage(): String = "login"

    @GetMapping("/register")
    fun registerPage(): String = "register"

    @PostMapping("/login")
    fun loginUser(
        @RequestParam username: String,
        @RequestParam password: String,
        session: HttpSession,
        model: Model
    ): String {
        return if (userService.validatePassword(username, password)) {
            session.setAttribute("username", username)
            session.setAttribute("role", userService.getUser(username)?.role)
            "redirect:/home"
        } else {
            model.addAttribute("error", "Invalid username or password")
            "login"
        }
    }

    @PostMapping("/register")
    fun registerUser(
        @RequestParam username: String,
        @RequestParam password: String,
        @RequestParam role: Role,
        session: HttpSession,
        model: Model
    ): String {
        return try {
            userService.createUser(username, password, role)
            session.setAttribute("username", username)
            session.setAttribute("role", role)
            "redirect:/home"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("error", e.message)
            "register"
        }
    }

    @GetMapping("/home")
    fun home(
        session: HttpSession,
        model: Model
    ): String {
        val username = session.getAttribute("username") as String?
        val roleAttr = session.getAttribute("role")?.toString() ?: "USER"

        val safeUsername = username ?: "anonymous"

        val message = if (username != null) {
            "${greetingService.getGreeting(username)}"
        } else {
            "Welcome to Modern Web App!"
        }

        model.addAttribute("username", safeUsername)
        model.addAttribute("role", roleAttr)
        model.addAttribute("message", message)

        return "home"
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/login"
    }
}
