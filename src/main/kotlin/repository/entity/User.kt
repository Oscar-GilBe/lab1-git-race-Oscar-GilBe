package es.unizar.webeng.hello.repository.entity

import es.unizar.webeng.hello.core.enum.Role
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role
)
