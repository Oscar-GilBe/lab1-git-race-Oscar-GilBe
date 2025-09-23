package es.unizar.webeng.hello.delivery.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.DTO.UserDTO
import es.unizar.webeng.hello.service.UserService
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
