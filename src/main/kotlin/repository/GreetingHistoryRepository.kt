package es.unizar.webeng.hello.repository

import es.unizar.webeng.hello.repository.entity.GreetingHistory
import es.unizar.webeng.hello.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface GreetingHistoryRepository : JpaRepository<GreetingHistory, Long> {
    fun findByUser(user: User): List<GreetingHistory>
}
