package es.unizar.webeng.hello.service

import org.springframework.stereotype.Service
import es.unizar.webeng.hello.repository.GreetingHistoryRepository
import es.unizar.webeng.hello.repository.UserRepository
import es.unizar.webeng.hello.delivery.DTO.StatisticsDTO

/**
 * Servicio encargado de calcular y proveer estadísticas de la aplicación.
 *
 * Combina información de los repositorios de usuarios y de historial
 * para generar métricas como número total de usuarios, total de saludos
 * y los nombres más populares.
 *
 * @property greetingHistoryRepository Repositorio de historial de saludos.
 * @property userRepository Repositorio de usuarios.
 */
@Service
class StatisticsService(
    private val greetingHistoryRepository: GreetingHistoryRepository,
    private val userRepository: UserRepository
) {

    /**
     * Obtiene las estadísticas actuales de la aplicación.
     *
     * @return Un objeto [StatisticsDTO] con:
     *  - total de usuarios registrados,
     *  - total de saludos realizados,
     *  - top 3 de nombres más populares junto a sus recuentos.
     */
    fun getStatistics(): StatisticsDTO {
        val totalUsers = userRepository.count()
        val totalGreetings = greetingHistoryRepository.count()

        val top3 = greetingHistoryRepository.countGreetingsGroupedByUser()
            .map { Pair(it[0] as String, (it[1] as Number).toLong()) }
            .take(3)

        return StatisticsDTO(
            totalUsers = totalUsers,
            totalGreetings = totalGreetings,
            top3Names = top3
        )
    }
}
