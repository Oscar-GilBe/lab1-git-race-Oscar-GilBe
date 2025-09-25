package es.unizar.webeng.hello.delivery.DTO

/**
 * Objeto de transferencia de datos (DTO) que representa las estadísticas de la aplicación.
 *
 * @property totalUsers Número total de usuarios registrados.
 * @property totalGreetings Número total de saludos realizados.
 * @property top3Names Lista con los tres nombres de usuario más populares
 * junto a la cantidad de saludos que han recibido.
 */
data class StatisticsDTO(
    val totalUsers: Long,
    val totalGreetings: Long,
    val top3Names: List<Pair<String, Long>>
)
