package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.GreetingHistoryRepository
import es.unizar.webeng.hello.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId

/**
 * Servicio encargado de gestionar la lógica de saludos
 * y el historial de mensajes asociados a los usuarios.
 *
 * Proporciona métodos para generar saludos personalizados
 * según la hora del día y para recuperar historiales.
 *
 * @property historyRepository Repositorio para gestionar [GreetingHistory].
 * @property userRepository Repositorio para acceder a usuarios.
 * @property clock Reloj configurable para obtener la hora actual (por defecto, zona Europe/Madrid).
 */
@Service
class GreetingService(
    private val historyRepository: GreetingHistoryRepository,
    private val userRepository: UserRepository,
    private val clock: Clock = Clock.system(ZoneId.of("Europe/Madrid"))) {

    /**
     * Genera un saludo para un usuario en función de la hora actual.
     *
     * Además, registra el saludo en la base de datos,
     * asociándolo al usuario si existe.
     *
     * @param name Nombre del usuario a saludar.
     * @return Saludo generado en formato de cadena.
     */
    fun getGreeting(name: String): String {
        val hour = LocalTime.now(clock).hour
        val greeting = when (hour) {
            in 7..15 -> "Good Morning"
            in 16..20 -> "Good Afternoon"
            else -> "Good Evening"
        }
        val message = "$greeting, $name!"

        val user = userRepository.findByUsername(name)
        historyRepository.save(GreetingHistory(message = message, user = user))

        return message
    }

    /**
     * Obtiene el historial de saludos de un usuario específico.
     *
     * @param username Nombre de usuario.
     * @return Lista de saludos asociados al usuario.
     * @throws IllegalArgumentException Si el usuario no existe.
     */
    fun getHistoryForUser(username: String): List<GreetingHistory> {
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")
        return historyRepository.findByUser(user)
    }

    /**
     * Recupera todos los registros de saludos en el sistema.
     *
     * @return Lista completa de saludos almacenados.
     */
    fun getAllHistory(): List<GreetingHistory> {
        return historyRepository.findAll()
    }
}
