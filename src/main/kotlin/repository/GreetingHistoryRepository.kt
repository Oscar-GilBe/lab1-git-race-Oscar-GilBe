package es.unizar.webeng.hello.repository

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repositorio JPA para gestionar la entidad [GreetingHistory].
 *
 * Proporciona métodos CRUD básicos heredados de [JpaRepository],
 * además de consultas específicas relacionadas con usuarios.
 */
interface GreetingHistoryRepository : JpaRepository<GreetingHistory, Long> {

    /**
     * Busca todos los registros de historial asociados a un usuario específico.
     *
     * @param user Usuario del cual obtener los saludos.
     * @return Lista de saludos realizados por el usuario.
     */
    fun findByUser(user: User): List<GreetingHistory>

    /**
     * Obtiene el número de saludos realizados agrupados por nombre de usuario que lo realizó.
     *
     * @return Una lista de arrays con dos posiciones:
     *  - [0] = nombre de usuario (String)
     *  - [1] = número de saludos (Long)
     */
    @Query(
        "SELECT gh.user.username, COUNT(gh) " +
        "FROM GreetingHistory gh " +
        "GROUP BY gh.user.username " +
        "ORDER BY COUNT(gh) DESC"
    )
    fun countGreetingsGroupedByUser(): List<Array<Any>>
}
