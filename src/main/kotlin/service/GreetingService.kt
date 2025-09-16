package es.unizar.webeng.hello.service

import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalTime

@Service
class GreetingService(private val clock: Clock = Clock.systemDefaultZone()) {

    fun getGreeting(): String {
        val hour = LocalTime.now(clock).hour
        return when (hour) {
            in 7..15 -> "Good Morning"
            in 16..20 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}
