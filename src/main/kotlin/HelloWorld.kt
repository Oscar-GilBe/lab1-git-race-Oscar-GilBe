package es.unizar.webeng.hello

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

/**
 * Clase principal de la aplicación Spring Boot.
 *
 * Marca el punto de entrada del sistema, habilitando
 * el escaneo de configuración y arrancando el contexto de Spring.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
class Application

/**
 * Función principal que arranca la aplicación.
 *
 * @param args Argumentos de línea de comandos.
 */
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
