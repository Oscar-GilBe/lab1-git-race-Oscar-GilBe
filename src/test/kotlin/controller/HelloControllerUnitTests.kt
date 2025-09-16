package es.unizar.webeng.hello.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.ui.Model
import org.springframework.ui.ExtendedModelMap
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

import es.unizar.webeng.hello.service.GreetingService

class HelloControllerUnitTests {
    private lateinit var controller: HelloController
    private lateinit var model: Model
    private lateinit var greetingService: GreetingService
    
    @BeforeEach
    fun setup() {
        greetingService = mock()
        controller = HelloController(greetingService, "Test Message")
        model = ExtendedModelMap()
    }
    
    @Test
    fun `should return welcome view with default message`() {
        val view = controller.welcome(model, "")
        
        assertThat(view).isEqualTo("welcome")
        assertThat(model.getAttribute("message")).isEqualTo("Test Message")
        assertThat(model.getAttribute("name")).isEqualTo("")
    }
    
    @Test
    fun `should return welcome view with personalized message and morning greeting`() {
        whenever(greetingService.getGreeting()).thenReturn("Good Morning")

        val view = controller.welcome(model, "Developer")
        
        assertThat(view).isEqualTo("welcome")
        assertThat(model.getAttribute("message")).isEqualTo("Good Morning, Developer!")
        assertThat(model.getAttribute("name")).isEqualTo("Developer")
    }
    
    @Test
    fun `should return API response with timestamp and evening greeting`() {
        whenever(greetingService.getGreeting()).thenReturn("Good Evening")

        val apiController = HelloApiController(greetingService)
        val response = apiController.helloApi("Test")
        
        assertThat(response).containsKey("message")
        assertThat(response).containsKey("timestamp")
        assertThat(response["message"]).isEqualTo("Good Evening, Test!")
        assertThat(response["timestamp"]).isNotNull()
    }
}
