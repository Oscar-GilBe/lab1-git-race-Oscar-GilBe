package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.GreetingHistoryRepository
import es.unizar.webeng.hello.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId

@Service
class GreetingService(
    private val historyRepository: GreetingHistoryRepository,
    private val userRepository: UserRepository,
    private val clock: Clock = Clock.system(ZoneId.of("Europe/Madrid"))) {

    fun getGreeting(name: String): String {
        val hour = LocalTime.now(clock).hour
        val greeting = when (hour) {
            in 7..15 -> "Good Morning"
            in 16..20 -> "Good Afternoon"
            else -> "Good Evening"
        }
        val message = "$greeting, $name!"

        val user = userRepository.findByUsername(name)
        historyRepository.save(GreetingHistory(message = message, user = user))

        return message
    }

    fun getHistoryForUser(username: String): List<GreetingHistory> {
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")
        return historyRepository.findByUser(user)
    }

    fun getAllHistory(): List<GreetingHistory> {
        return historyRepository.findAll()
    }
}
