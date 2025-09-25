package es.unizar.webeng.hello.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Configuración de seguridad de Spring.
 *
 * Define los beans necesarios para la gestión de contraseñas,
 * incluyendo el codificador basado en el algoritmo [BCryptPasswordEncoder].
 */
@Configuration
class SecurityConfig {
    /**
     * Provee un bean de tipo [PasswordEncoder] que implementa
     * el algoritmo BCrypt para el almacenamiento seguro de contraseñas.
     *
     * @return una instancia de [BCryptPasswordEncoder].
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
