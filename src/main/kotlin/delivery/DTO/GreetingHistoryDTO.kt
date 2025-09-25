package es.unizar.webeng.hello.delivery.DTO

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * DTO (Data Transfer Object) para representar un historial de saludos.
 *
 * Este objeto se utiliza para transferir datos de la entidad [GreetingHistory]
 * a las capas superiores (controladores o vistas), evitando exponer la entidad
 * directamente.
 *
 * @property id Identificador único del historial.
 * @property message Mensaje de saludo almacenado.
 * @property timestamp Fecha y hora en formato `dd-MM-yyyy HH:mm:ss`, localizada en la zona horaria de Madrid.
 * @property username Nombre de usuario asociado al saludo, o `null` si no existe.
 */
data class GreetingHistoryDTO(
    val id: Long,
    val message: String,
    val timestamp: String,
    val username: String?
) {
    companion object {

        /**
         * Formateador de fechas con el patrón `dd-MM-yyyy HH:mm:ss`
         * configurado en la zona horaria `Europe/Madrid`.
         */
        private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            .withZone(ZoneId.of("Europe/Madrid"))
        
        /**
         * Convierte una entidad [GreetingHistory] en un [GreetingHistoryDTO].
         *
         * @param history Entidad de historial a convertir.
         * @return una instancia de [GreetingHistoryDTO] con los datos formateados.
         */
        fun fromEntity(history: GreetingHistory): GreetingHistoryDTO {
            return GreetingHistoryDTO(
                id = history.id,
                message = history.message,
                timestamp = formatter.format(history.timestamp),
                username = history.user?.username
            )
        }
    }
}
