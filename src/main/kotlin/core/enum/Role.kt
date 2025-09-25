package es.unizar.webeng.hello.core.enum

/**
 * Enumeración que representa los roles posibles de un usuario en la aplicación.
 *
 * Los roles determinan los permisos y accesos a diferentes partes del sistema:
 * - [ADMIN]: usuario con privilegios administrativos, acceso total al sistema.
 * - [USER]: usuario estándar con permisos limitados.
 */
enum class Role {
    /** Rol de administrador con acceso completo al sistema. */
    ADMIN,

    /** Rol de usuario estándar con permisos restringidos. */
    USER
}
