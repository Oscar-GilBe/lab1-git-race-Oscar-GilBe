package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.controller.UserController
import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.service.UserService
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
@ActiveProfiles("test")
class UserControllerMVCTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `should create user`() {
        val user = User(id = 1L, username = "Alice", password = "pw", role = Role.USER)
        `when`(userService.createUser("Alice", "pw", Role.USER)).thenReturn(user)

        mockMvc.perform(
            post("/api/users")
                .param("username", "Alice")
                .param("password", "pw")
                .param("role", "USER")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username", equalTo("Alice")))
            .andExpect(jsonPath("$.role", equalTo("USER")))
    }

    @Test
    fun `should login successfully`() {
        `when`(userService.validatePassword("Bob", "pw")).thenReturn(true)

        mockMvc.perform(
            post("/api/users/login")
                .param("username", "Bob")
                .param("password", "pw")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Login successful"))
    }

    @Test
    fun `should fail login with invalid credentials`() {
        `when`(userService.validatePassword("Bob", "wrong")).thenReturn(false)

        mockMvc.perform(
            post("/api/users/login")
                .param("username", "Bob")
                .param("password", "wrong")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(content().string("Invalid username or password"))
    }

    @Test
    fun `should get user`() {
        val user = User(id = 2L, username = "Carol", password = "pw", role = Role.ADMIN)
        `when`(userService.getUser("Carol")).thenReturn(user)

        mockMvc.perform(get("/api/users/Carol"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username", equalTo("Carol")))
            .andExpect(jsonPath("$.role", equalTo("ADMIN")))
    }

    @Test
    fun `should return 404 if user not found`() {
        `when`(userService.getUser("Ghost")).thenReturn(null)

        mockMvc.perform(get("/api/users/Ghost"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should get all users`() {
        val list = listOf(
            User(id = 1L, username = "Alice", password = "pw", role = Role.USER),
            User(id = 2L, username = "Bob", password = "pw", role = Role.ADMIN)
        )
        `when`(userService.getAllUsers()).thenReturn(list)

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()", equalTo(2)))
            .andExpect(jsonPath("$[0].username", equalTo("Alice")))
            .andExpect(jsonPath("$[1].role", equalTo("ADMIN")))
    }

    @Test
    fun `should delete user`() {
        mockMvc.perform(delete("/api/users/Alice"))
            .andExpect(status().isNoContent)
    }
}
