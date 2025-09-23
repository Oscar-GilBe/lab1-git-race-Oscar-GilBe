package es.unizar.webeng.hello.repository.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "greeting_history")
data class GreetingHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val message: String,

    val timestamp: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null
)
