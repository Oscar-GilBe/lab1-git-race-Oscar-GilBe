package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.controller.AuthPageController
import es.unizar.webeng.hello.service.GreetingService
import es.unizar.webeng.hello.service.UserService
import es.unizar.webeng.hello.repository.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.ui.ExtendedModelMap
import jakarta.servlet.http.HttpSession
import org.mockito.kotlin.mock

class AuthPageControllerUnitTests {

    private lateinit var controller: AuthPageController
    private lateinit var userService: UserService
    private lateinit var greetingService: GreetingService
    private lateinit var session: HttpSession
    private lateinit var model: ExtendedModelMap

    @BeforeEach
    fun setup() {
        userService = mock()
        greetingService = mock()
        session = mock()
        model = ExtendedModelMap()
        controller = AuthPageController(userService, greetingService)
    }

    @Test
    fun `login page returns login view`() {
        val view = controller.loginPage()
        assertThat(view).isEqualTo("login")
    }

    @Test
    fun `register page returns register view`() {
        val view = controller.registerPage()
        assertThat(view).isEqualTo("register")
    }

    @Test
    fun `successful login sets session and redirects to home`() {
        whenever(userService.validatePassword("Alice", "pw")).thenReturn(true)

        val view = controller.loginUser("Alice", "pw", session, model)
        assertThat(view).isEqualTo("redirect:/home")
    }

    @Test
    fun `failed login returns login with error`() {
        whenever(userService.validatePassword("Alice", "wrong")).thenReturn(false)

        val view = controller.loginUser("Alice", "wrong", session, model)
        assertThat(view).isEqualTo("login")
        assertThat(model.getAttribute("error")).isEqualTo("Invalid username or password")
    }

    @Test
    fun `successful register sets session and redirects to home`() {
        val mockedUser: User = mock()
        whenever(userService.createUser("Bob", "pw", Role.USER)).thenReturn(
            mockedUser
        )

        val view = controller.registerUser("Bob", "pw", Role.USER, session, model)
        assertThat(view).isEqualTo("redirect:/home")
    }

    @Test
    fun `register fails with existing user`() {
        whenever(userService.createUser("Bob", "pw", Role.USER))
            .thenThrow(IllegalArgumentException("User 'Bob' already exists"))

        val view = controller.registerUser("Bob", "pw", Role.USER, session, model)
        assertThat(view).isEqualTo("register")
        assertThat(model.getAttribute("error")).isEqualTo("User 'Bob' already exists")
    }

    @Test
    fun `home page with username returns greeting message`() {
        whenever(session.getAttribute("username")).thenReturn("Alice")
        whenever(session.getAttribute("role")).thenReturn(Role.USER)
        whenever(greetingService.getGreeting("Alice")).thenReturn("Good Evening, Alice!")

        val view = controller.home(session, model)
        assertThat(view).isEqualTo("home")
        assertThat(model.getAttribute("username")).isEqualTo("Alice")
        assertThat(model.getAttribute("role")).isEqualTo("USER")
        assertThat(model.getAttribute("message")).isEqualTo("Good Evening, Alice!")
    }

    @Test
    fun `logout invalidates session and redirects`() {
        val view = controller.logout(session)
        assertThat(view).isEqualTo("redirect:/login")
    }
}
