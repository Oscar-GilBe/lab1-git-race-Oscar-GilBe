package es.unizar.webeng.hello

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.kotlin.whenever

import es.unizar.webeng.hello.service.GreetingService

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IntegrationTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @MockBean
    private lateinit var greetingService: GreetingService

    @Test
    fun `should return home page with modern title and client-side HTTP debug`() {
        val response = restTemplate.getForEntity("http://localhost:$port", String::class.java)
        
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("<title>Modern Web App</title>")
        assertThat(response.body).contains("Welcome to Modern Web App")
        assertThat(response.body).contains("Interactive HTTP Testing & Debug")
        assertThat(response.body).contains("Client-Side Educational Tool")
    }

    @Test
    fun `should return personalized morning greeting`() {
        whenever(greetingService.getGreeting("Developer")).thenReturn("Good Morning, Developer!")

        val response = restTemplate.getForEntity("http://localhost:$port?name=Developer", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Good Morning, Developer!")
    }

    @Test
    fun `should return personalized evening greeting`() {
        whenever(greetingService.getGreeting("Test")).thenReturn("Good Evening, Test!")

        val response = restTemplate.getForEntity("http://localhost:$port/?name=Test", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Good Evening, Test!")
    }

    @Test
    fun `should return API response with timestamp and afternoon greeting`() {
        whenever(greetingService.getGreeting("Test")).thenReturn("Good Afternoon, Test!")
        
        val response = restTemplate.getForEntity("http://localhost:$port/api/hello?name=Test", String::class.java)
        
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)
        assertThat(response.body).contains("Good Afternoon, Test!")
        assertThat(response.body).contains("timestamp")
    }

    @Test
    fun `should serve Bootstrap CSS correctly`() {
        val response = restTemplate.getForEntity("http://localhost:$port/webjars/bootstrap/5.3.3/css/bootstrap.min.css", String::class.java)
        
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("body")
        assertThat(response.headers.contentType).isEqualTo(MediaType.valueOf("text/css"))
    }

    @Test
    fun `should expose actuator health endpoint`() {
        val response = restTemplate.getForEntity("http://localhost:$port/actuator/health", String::class.java)
        
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("UP")
    }
    
    @Test
    fun `should display client-side HTTP debug interface`() {
        val response = restTemplate.getForEntity("http://localhost:$port?name=Student", String::class.java)
        
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Interactive HTTP Testing & Debug")
        assertThat(response.body).contains("Client-Side Educational Tool")
        assertThat(response.body).contains("Web Page Greeting")
        assertThat(response.body).contains("API Endpoint")
        assertThat(response.body).contains("Health Check")
        assertThat(response.body).contains("Learning Notes:")
    }
}
