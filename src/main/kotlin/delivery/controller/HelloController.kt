package es.unizar.webeng.hello.delivery.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import es.unizar.webeng.hello.service.GreetingService

@Controller
class HelloController(
    private val greetingService: GreetingService,
    @param:Value("\${app.message:Hello World}") 
    private val message: String
) {
    
    @GetMapping("/")
    fun welcome(
        model: Model,
        @RequestParam(defaultValue = "") name: String
    ): String {
        val greeting = if (name.isNotBlank()) "${greetingService.getGreeting(name)}, $name!" else message
        model.addAttribute("message", greeting)
        model.addAttribute("name", name)
        return "welcome"
    }
}

@RestController
class HelloApiController(private val greetingService: GreetingService) {
    
    @GetMapping("/api/hello", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun helloApi(@RequestParam(defaultValue = "World") name: String): Map<String, String> {
        val message = greetingService.getGreeting(name)
        return mapOf(
            "message" to message,
            "timestamp" to java.time.Instant.now().toString()
        )
    }
}
