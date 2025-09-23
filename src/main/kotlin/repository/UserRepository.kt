package es.unizar.webeng.hello.repository

import es.unizar.webeng.hello.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
