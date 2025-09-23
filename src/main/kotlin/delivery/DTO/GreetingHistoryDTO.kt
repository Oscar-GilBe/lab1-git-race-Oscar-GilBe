package es.unizar.webeng.hello.delivery.DTO

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class GreetingHistoryDTO(
    val id: Long,
    val message: String,
    val timestamp: String,
    val username: String?
) {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            .withZone(ZoneId.of("Europe/Madrid"))
        
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
