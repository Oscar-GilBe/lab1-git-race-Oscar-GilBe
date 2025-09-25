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

/**
 * Controlador REST para la gestión de usuarios vía API.
 *
 * Permite crear, consultar, autenticar y eliminar usuarios.
 *
 * @property userService Servicio encargado de la lógica de negocio de usuarios.
 */
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    /**
     * Crea un nuevo usuario con credenciales y rol especificados.
     *
     * @param username Nombre del usuario.
     * @param password Contraseña sin encriptar.
     * @param role Rol asignado al usuario ([Role.USER] o [Role.ADMIN]).
     * @return El usuario creado como [UserDTO].
     */
    @PostMapping
    fun createUser(
        @RequestParam username: String,
        @RequestParam password: String,
        @RequestParam role: Role
    ): ResponseEntity<UserDTO> {
        val user = userService.createUser(username, password, role)
        return ResponseEntity.ok(UserDTO.fromEntity(user))
    }

    /**
     * Autentica un usuario mediante nombre y contraseña.
     *
     * @param username Nombre del usuario.
     * @param password Contraseña.
     * @return "Login successful" si las credenciales son correctas,
     *         401 en caso contrario.
     */
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

    /**
     * Obtiene los datos de un usuario concreto.
     *
     * @param username Nombre del usuario.
     * @return El usuario como [UserDTO] o 404 si no existe.
     */
    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String): ResponseEntity<UserDTO> {
        val user = userService.getUser(username)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserDTO.fromEntity(user))
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return Lista de [UserDTO].
     */
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAllUsers().map { UserDTO.fromEntity(it) }
        return ResponseEntity.ok(users)
    }

    /**
     * Elimina un usuario por su nombre.
     *
     * @param username Nombre del usuario a eliminar.
     * @return Respuesta vacía con código 204.
     */
    @DeleteMapping("/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Void> {
        userService.deleteUser(username)
        return ResponseEntity.noContent().build()
    }
}

/**
 * Controlador MVC encargado de la autenticación y navegación básica de usuarios.
 *
 * Permite login, registro, home y logout a través de vistas Thymeleaf.
 *
 * @property userService Servicio de usuarios para autenticación y gestión.
 * @property greetingService Servicio de saludos usado en la vista `home`.
 */
@Controller
class AuthPageController(
    private val userService: UserService,
    private val greetingService: GreetingService
) {

    /** Renderiza la página de login. */
    @GetMapping("/login")
    fun loginPage(): String = "login"

    /** Renderiza la página de registro. */
    @GetMapping("/register")
    fun registerPage(): String = "register"

    /**
     * Procesa el login desde formulario.
     *
     * @param username Nombre introducido.
     * @param password Contraseña introducida.
     * @param session Sesión HTTP donde se guarda usuario y rol si es válido.
     * @param model Modelo usado para mostrar errores.
     * @return Redirección a `/home` en caso de éxito, o `login` con error.
     */
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

    /**
     * Procesa el registro de un nuevo usuario desde formulario.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param role Rol del usuario.
     * @param session Sesión HTTP donde se guarda el nuevo usuario y rol.
     * @param model Modelo para errores.
     * @return Redirección a `/home` en caso de éxito, o `register` con error.
     */
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

    /**
     * Renderiza la página de inicio tras login.
     *
     * Añade al modelo el nombre de usuario, rol y un mensaje de saludo.
     *
     * @param session Sesión HTTP.
     * @param model Modelo de la vista.
     * @return La vista `home`.
     */
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

    /**
     * Cierra sesión e invalida la sesión HTTP.
     *
     * @param session Sesión HTTP actual.
     * @return Redirección a la página de login.
     */
    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/login"
    }
}
