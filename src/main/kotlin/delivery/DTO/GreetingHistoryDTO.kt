package es.unizar.webeng.hello.delivery.DTO

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import java.time.Instant

data class GreetingHistoryDTO(
    val id: Long,
    val message: String,
    val timestamp: Instant,
    val username: String?
) {
    companion object {
        fun fromEntity(history: GreetingHistory): GreetingHistoryDTO {
            return GreetingHistoryDTO(
                id = history.id,
                message = history.message,
                timestamp = history.timestamp,
                username = history.user?.username
            )
        }
    }
}
