package es.unizar.webeng.hello.repository

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository

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
}
