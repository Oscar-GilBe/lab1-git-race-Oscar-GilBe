package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.controller.AuthPageController
import es.unizar.webeng.hello.service.GreetingService
import es.unizar.webeng.hello.service.UserService
import es.unizar.webeng.hello.repository.entity.User
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(AuthPageController::class)
class AuthPageControllerMVCTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var greetingService: GreetingService

    @Test
    fun `login page should return login view`() {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk)
            .andExpect(view().name("login"))
    }

    @Test
    fun `register page should return register view`() {
        mockMvc.perform(get("/register"))
            .andExpect(status().isOk)
            .andExpect(view().name("register"))
    }

    @Test
    fun `successful login redirects to home`() {
        `when`(userService.validatePassword("Alice", "pw")).thenReturn(true)
        `when`(userService.getUser("Alice")).thenReturn(null)

        mockMvc.perform(post("/login")
            .param("username", "Alice")
            .param("password", "pw"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/home"))
    }

    @Test
    fun `failed login returns login view with error`() {
        `when`(userService.validatePassword("Alice", "wrong")).thenReturn(false)

        mockMvc.perform(post("/login")
            .param("username", "Alice")
            .param("password", "wrong"))
            .andExpect(status().isOk)
            .andExpect(view().name("login"))
            .andExpect(model().attribute("error", equalTo("Invalid username or password")))
    }

    @Test
    fun `successful register redirects to home`() {
        val mockedUser: User = mock()
        `when`(userService.createUser("Bob", "pw", Role.USER)).thenReturn(
            mockedUser
        )

        mockMvc.perform(post("/register")
            .param("username", "Bob")
            .param("password", "pw")
            .param("role", "USER"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/home"))
    }

    @Test
    fun `register with existing user returns register view with error`() {
        `when`(userService.createUser("Bob", "pw", Role.USER))
            .thenThrow(IllegalArgumentException("User 'Bob' already exists"))

        mockMvc.perform(post("/register")
            .param("username", "Bob")
            .param("password", "pw")
            .param("role", "USER"))
            .andExpect(status().isOk)
            .andExpect(view().name("register"))
            .andExpect(model().attribute("error", equalTo("User 'Bob' already exists")))
    }

    @Test
    fun `home page shows greeting message`() {
        `when`(greetingService.getGreeting("Alice")).thenReturn("Good Morning, Alice!")

        mockMvc.perform(get("/home")
            .sessionAttr("username", "Alice")
            .sessionAttr("role", Role.USER))
            .andExpect(status().isOk)
            .andExpect(view().name("home"))
            .andExpect(model().attribute("username", equalTo("Alice")))
            .andExpect(model().attribute("role", equalTo("USER")))
            .andExpect(model().attribute("message", equalTo("Good Morning, Alice!")))
    }

    @Test
    fun `logout invalidates session and redirects to login`() {
        mockMvc.perform(get("/logout"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/login"))
    }
}
