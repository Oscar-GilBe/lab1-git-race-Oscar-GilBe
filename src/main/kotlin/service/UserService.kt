package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.core.enum.Role
import es.unizar.webeng.hello.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Servicio encargado de gestionar la lógica de usuarios,
 * incluyendo creación, validación de contraseñas y operaciones CRUD.
 *
 * @property userRepository Repositorio para acceder a usuarios.
 * @property passwordEncoder Codificador de contraseñas para almacenar de forma segura.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param username Nombre de usuario único.
     * @param rawPassword Contraseña en texto plano (se encripta antes de guardarla).
     * @param role Rol asignado al usuario ([Role.ADMIN] o [Role.USER]).
     * @return El usuario creado.
     * @throws IllegalArgumentException Si el usuario ya existe.
     */
    fun createUser(username: String, rawPassword: String, role: Role): User {
        val existing = userRepository.findByUsername(username)
        if (existing != null) {
            throw IllegalArgumentException("User '$username' already exists")
        }

        val encryptedPassword = passwordEncoder.encode(rawPassword)

        val user = User(username = username, password = encryptedPassword, role = role)
        return userRepository.save(user)
    }

    /**
     * Valida la contraseña de un usuario.
     *
     * @param username Nombre de usuario.
     * @param rawPassword Contraseña en texto plano.
     * @return `true` si la contraseña coincide, `false` en caso contrario.
     */
    fun validatePassword(username: String, rawPassword: String): Boolean {
        val user = userRepository.findByUsername(username) ?: return false
        return passwordEncoder.matches(rawPassword, user.password)
    }

    /**
     * Recupera un usuario por su nombre.
     *
     * @param username Nombre de usuario.
     * @return El usuario encontrado o `null` si no existe.
     */
    fun getUser(username: String): User? {
        return userRepository.findByUsername(username)
    }

    /**
     * Recupera todos los usuarios registrados.
     *
     * @return Lista completa de usuarios.
     */
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    /**
     * Elimina un usuario por su nombre.
     *
     * @param username Nombre del usuario a eliminar.
     * @throws IllegalArgumentException Si el usuario no existe.
     */
    fun deleteUser(username: String) {
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")
        userRepository.delete(user)
    }
}
