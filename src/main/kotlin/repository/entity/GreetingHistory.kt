package es.unizar.webeng.hello.repository.entity

import jakarta.persistence.*
import java.time.Instant

/**
 * Entidad JPA que representa un saludo almacenado en el historial.
 *
 * Cada registro contiene el mensaje, un instante temporal
 * y una referencia opcional al usuario que lo generó.
 *
 * Se mapea a la tabla `greeting_history`.
 *
 * @property id Identificador único autogenerado.
 * @property message Texto del saludo registrado.
 * @property timestamp Marca de tiempo del saludo (por defecto el instante actual).
 * @property user Usuario que realizó el saludo (puede ser `null` si no está asociado).
 */
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
