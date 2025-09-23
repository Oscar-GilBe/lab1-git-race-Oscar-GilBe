package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun createUser(username: String, rawPassword: String, role: Role): User {
        val existing = userRepository.findByUsername(username)
        if (existing != null) {
            throw IllegalArgumentException("User '$username' already exists")
        }

        val encryptedPassword = passwordEncoder.encode(rawPassword)

        val user = User(username = username, password = encryptedPassword, role = role)
        return userRepository.save(user)
    }

    fun validatePassword(username: String, rawPassword: String): Boolean {
        val user = userRepository.findByUsername(username) ?: return false
        return passwordEncoder.matches(rawPassword, user.password)
    }

    fun getUser(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun deleteUser(username: String) {
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")
        userRepository.delete(user)
    }
}
