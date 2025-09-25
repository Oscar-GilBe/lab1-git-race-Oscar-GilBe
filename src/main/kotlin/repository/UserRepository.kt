package es.unizar.webeng.hello.repository

import es.unizar.webeng.hello.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio JPA para gestionar la entidad [User].
 *
 * Proporciona operaciones CRUD básicas heredadas de [JpaRepository],
 * así como consultas adicionales para búsqueda por nombre de usuario.
 */
interface UserRepository : JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre del usuario.
     * @return El usuario correspondiente o `null` si no existe.
     */
    fun findByUsername(username: String): User?
}
