package es.unizar.webeng.hello.delivery.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import es.unizar.webeng.hello.service.GreetingService

/**
 * Controlador MVC encargado de manejar las peticiones a la página principal.
 *
 * Renderiza una vista Thymeleaf con un mensaje de saludo,
 * que puede personalizarse con el nombre proporcionado por el usuario.
 *
 * @property greetingService Servicio encargado de generar los mensajes de saludo.
 * @property message Mensaje por defecto definido en las propiedades de la aplicación (`app.message`).
 */
@Controller
class HelloController(
    private val greetingService: GreetingService,
    @param:Value("\${app.message:Hello World}") 
    private val message: String
) {
    
    /**
     * Maneja las solicitudes GET a la raíz `/`.
     *
     * Si se pasa un parámetro `name`, genera un saludo personalizado con [GreetingService].
     * Si no, utiliza el mensaje por defecto.
     *
     * @param model Modelo de atributos para la vista.
     * @param name Nombre opcional del usuario (vacío por defecto).
     * @return El nombre de la vista `welcome`.
     */
    @GetMapping("/")
    fun welcome(
        model: Model,
        @RequestParam(defaultValue = "") name: String
    ): String {
        val greeting = if (name.isNotBlank()) "${greetingService.getGreeting(name)}" else message
        model.addAttribute("message", greeting)
        model.addAttribute("name", name)
        return "welcome"
    }
}

/**
 * Controlador REST empleado para exponer el saludo en formato JSON.
 *
 * Proporciona un endpoint accesible en `/api/hello`.
 *
 * @property greetingService Servicio encargado de generar los mensajes de saludo.
 */
@RestController
class HelloApiController(private val greetingService: GreetingService) {
    
    /**
     * Devuelve un saludo personalizado en formato JSON.
     *
     * @param name Nombre opcional del usuario (por defecto `"World"`).
     * @return Un mapa con el mensaje y un timestamp en formato ISO-8601.
     */
    @GetMapping("/api/hello", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun helloApi(@RequestParam(defaultValue = "World") name: String): Map<String, String> {
        val message = greetingService.getGreeting(name)
        return mapOf(
            "message" to message,
            "timestamp" to java.time.Instant.now().toString()
        )
    }
}
