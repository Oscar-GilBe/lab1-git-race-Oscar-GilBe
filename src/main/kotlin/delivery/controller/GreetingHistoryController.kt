package es.unizar.webeng.hello.delivery.controller

import es.unizar.webeng.hello.delivery.DTO.GreetingHistoryDTO
import es.unizar.webeng.hello.service.GreetingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/history")
class GreetingHistoryController(
    private val greetingService: GreetingService
) {

    @GetMapping
    fun getAllHistory(): ResponseEntity<List<GreetingHistoryDTO>> {
        val history = greetingService.getAllHistory().map {
            GreetingHistoryDTO.fromEntity(it)
        }
        return ResponseEntity.ok(history)
    }

    @GetMapping("/{username}")
    fun getUserHistory(@PathVariable username: String): ResponseEntity<List<GreetingHistoryDTO>> {
        val history = greetingService.getHistoryForUser(username).map {
            GreetingHistoryDTO.fromEntity(it)
        }
        return ResponseEntity.ok(history)
    }
}
