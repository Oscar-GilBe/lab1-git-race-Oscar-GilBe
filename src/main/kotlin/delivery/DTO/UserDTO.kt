package es.unizar.webeng.hello.delivery.DTO

import es.unizar.webeng.hello.repository.entity.User
import es.unizar.webeng.hello.core.enum.Role

data class UserDTO(
    val id: Long,
    val username: String,
    val role: Role
) {
    companion object {
        fun fromEntity(user: User): UserDTO {
            return UserDTO(
                id = user.id,
                username = user.username,
                role = user.role
                // do not expose password
            )
        }
    }
}
