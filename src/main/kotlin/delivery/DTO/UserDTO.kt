package es.unizar.webeng.hello.delivery.DTO

import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.core.enum.Role

/**
 * DTO (Data Transfer Object) para representar información de un usuario.
 *
 * Se utiliza para transferir datos sin exponer la contraseña u otros detalles
 * sensibles de la entidad [User].
 *
 * @property id Identificador único del usuario.
 * @property username Nombre de usuario.
 * @property role Rol asignado al usuario ([Role.ADMIN] o [Role.USER]).
 */
data class UserDTO(
    val id: Long,
    val username: String,
    val role: Role
) {
    companion object {
        /**
         * Convierte una entidad [User] en un [UserDTO].
         *
         * **Importante:** La contraseña nunca se expone en el DTO.
         *
         * @param user Entidad de usuario a convertir.
         * @return una instancia de [UserDTO] con los campos públicos del usuario.
         */
        fun fromEntity(user: User): UserDTO {
            return UserDTO(
                id = user.id,
                username = user.username,
                role = user.role
                // no exponer la contraseña
            )
        }
    }
}
