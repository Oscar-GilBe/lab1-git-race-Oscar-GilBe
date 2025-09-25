package es.unizar.webeng.hello.repository.entity

import es.unizar.webeng.hello.core.enum.Role
import jakarta.persistence.*

/**
 * Entidad JPA que representa a un usuario de la aplicación.
 *
 * Cada usuario tiene un nombre único, contraseña encriptada
 * y un rol asignado (ADMIN o USER).
 *
 * Se mapea a la tabla `users`.
 *
 * @property id Identificador único autogenerado.
 * @property username Nombre único del usuario.
 * @property password Contraseña encriptada del usuario.
 * @property role Rol del usuario en el sistema ([Role.ADMIN], [Role.USER]).
 */
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
