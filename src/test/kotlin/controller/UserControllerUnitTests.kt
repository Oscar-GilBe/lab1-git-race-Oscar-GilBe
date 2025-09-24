package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.delivery.controller.UserController
import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus

class UserControllerUnitTests {

    private lateinit var controller: UserController
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        userService = mock()
        controller = UserController(userService)
    }

    @Test
    fun `should create user`() {
        val user = User(id = 1L, username = "Alice", password = "pw", role = Role.USER)
        `when`(userService.createUser("Alice", "pw", Role.USER)).thenReturn(user)

        val response = controller.createUser("Alice", "pw", Role.USER)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.username).isEqualTo("Alice")
        assertThat(response.body!!.role).isEqualTo(Role.USER)
    }

    @Test
    fun `should login successfully`() {
        `when`(userService.validatePassword("Bob", "pw")).thenReturn(true)

        val response = controller.login("Bob", "pw")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo("Login successful")
    }

    @Test
    fun `should return 401 on invalid login`() {
        `when`(userService.validatePassword("Bob", "wrong")).thenReturn(false)

        val response = controller.login("Bob", "wrong")

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(response.body).isEqualTo("Invalid username or password")
    }

    @Test
    fun `should get user`() {
        val user = User(id = 2L, username = "Carol", password = "pw", role = Role.ADMIN)
        `when`(userService.getUser("Carol")).thenReturn(user)

        val response = controller.getUser("Carol")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.username).isEqualTo("Carol")
        assertThat(response.body!!.role).isEqualTo(Role.ADMIN)
    }

    @Test
    fun `should return 404 if user not found`() {
        `when`(userService.getUser("Ghost")).thenReturn(null)

        val response = controller.getUser("Ghost")

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `should get all users`() {
        val list = listOf(
            User(id = 1L, username = "Alice", password = "pw", role = Role.USER),
            User(id = 2L, username = "Bob", password = "pw", role = Role.ADMIN)
        )
        `when`(userService.getAllUsers()).thenReturn(list)

        val response = controller.getAllUsers()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).hasSize(2)
        assertThat(response.body!![0].username).isEqualTo("Alice")
        assertThat(response.body!![1].role).isEqualTo(Role.ADMIN)
    }

    @Test
    fun `should delete user`() {
        val response = controller.deleteUser("Alice")

        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }
}
