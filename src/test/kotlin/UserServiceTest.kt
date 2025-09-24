package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.UserRepository
import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.core.enum.Role
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {

    private val userRepository: UserRepository = mock(UserRepository::class.java)
    private val passwordEncoder: PasswordEncoder = mock(PasswordEncoder::class.java)

    private val service = UserService(userRepository, passwordEncoder)

    @Test
    fun `should create a new user when username does not exist`() {
        `when`(userRepository.findByUsername("Alice")).thenReturn(null)
        `when`(passwordEncoder.encode("password123")).thenReturn("ENCODED")
        val savedUser = User(id = 1L, username = "Alice", password = "ENCODED", role = Role.USER)
        `when`(userRepository.save(any(User::class.java))).thenReturn(savedUser)

        val result = service.createUser("Alice", "password123", Role.USER)

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.username).isEqualTo("Alice")
        assertThat(result.password).isEqualTo("ENCODED")
        assertThat(result.role).isEqualTo(Role.USER)
        verify(userRepository).save(any(User::class.java))
    }

    @Test
    fun `should throw exception when creating duplicate user`() {
        val existingUser = User(id = 1L, username = "Alice", password = "pw", role = Role.USER)
        `when`(userRepository.findByUsername("Alice")).thenReturn(existingUser)

        assertThatThrownBy { service.createUser("Alice", "password123", Role.USER) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("User 'Alice' already exists")
    }

    @Test
    fun `should validate password correctly`() {
        val user = User(id = 1L, username = "Bob", password = "ENCODED", role = Role.USER)
        `when`(userRepository.findByUsername("Bob")).thenReturn(user)
        `when`(passwordEncoder.matches("secret", "ENCODED")).thenReturn(true)

        val result = service.validatePassword("Bob", "secret")

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if password is invalid`() {
        val user = User(id = 1L, username = "Bob", password = "ENCODED", role = Role.USER)
        `when`(userRepository.findByUsername("Bob")).thenReturn(user)
        `when`(passwordEncoder.matches("wrong", "ENCODED")).thenReturn(false)

        val result = service.validatePassword("Bob", "wrong")

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false if user does not exist when validating password`() {
        `when`(userRepository.findByUsername("Ghost")).thenReturn(null)

        val result = service.validatePassword("Ghost", "anything")

        assertThat(result).isFalse()
    }

    @Test
    fun `should get user by username`() {
        val user = User(id = 1L, username = "Carol", password = "pw", role = Role.ADMIN)
        `when`(userRepository.findByUsername("Carol")).thenReturn(user)

        val result = service.getUser("Carol")

        assertThat(result).isNotNull
        assertThat(result!!.username).isEqualTo("Carol")
        assertThat(result.role).isEqualTo(Role.ADMIN)
    }

    @Test
    fun `should return null if user not found`() {
        `when`(userRepository.findByUsername("Nobody")).thenReturn(null)

        val result = service.getUser("Nobody")

        assertThat(result).isNull()
    }

    @Test
    fun `should return all users`() {
        val users = listOf(
            User(id = 1L, username = "Alice", password = "pw", role = Role.USER),
            User(id = 2L, username = "Bob", password = "pw2", role = Role.ADMIN)
        )
        `when`(userRepository.findAll()).thenReturn(users)

        val result = service.getAllUsers()

        assertThat(result).hasSize(2)
        assertThat(result.map { it.username }).containsExactly("Alice", "Bob")
    }

    @Test
    fun `should delete existing user`() {
        val user = User(id = 1L, username = "Alice", password = "pw", role = Role.USER)
        `when`(userRepository.findByUsername("Alice")).thenReturn(user)

        service.deleteUser("Alice")

        verify(userRepository).delete(user)
    }

    @Test
    fun `should throw exception when deleting non-existing user`() {
        `when`(userRepository.findByUsername("Ghost")).thenReturn(null)

        assertThatThrownBy { service.deleteUser("Ghost") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("User not found")
    }
}
