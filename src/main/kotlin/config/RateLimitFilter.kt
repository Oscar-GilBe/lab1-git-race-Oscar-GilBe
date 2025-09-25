package es.unizar.webeng.hello.config

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Filtro de limitación de peticiones basado en la librería [Bucket4j].
 *
 * Este filtro se aplica únicamente en entornos distintos a `test` gracias al perfil
 * `@Profile("!test", "ratelimiter")`. Se encarga de limitar la tasa de peticiones
 * a los endpoints que comienzan con `/api/`, aplicando un límite de **5 peticiones
 * por minuto por dirección IP**.
 *
 * Para almacenar el estado de los "buckets" (cubos de tokens) se utiliza un
 * caché en memoria proporcionado por [Caffeine], que elimina entradas tras 10
 * minutos de inactividad.
 *
 * - Si una petición está dentro del límite, se añade la cabecera
 *   `X-Rate-Limit-Remaining` con los tokens restantes y se deja continuar la
 *   ejecución de la cadena de filtros.
 * - Si se supera el límite, se devuelve un error `429 Too Many Requests`
 *   incluyendo en la respuesta el campo `Retry-After` en segundos.
 */
@Component
@Profile("!test", "ratelimiter")
class RateLimitFilter : OncePerRequestFilter() {

    /**
     * Caché que asocia direcciones IP con un [Bucket].
     * Cada entrada expira tras 10 minutos de inactividad.
     */
    private val cache = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(10))
        .build<String, Bucket>()

    /**
     * Lógica principal del filtro de rate limiting.
     *
     * @param request Petición HTTP entrante.
     * @param response Respuesta HTTP saliente.
     * @param filterChain Cadena de filtros de Spring.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.startsWith("/api/")) {
            val ip = request.remoteAddr
            val bucket = cache.get(ip) { newBucket() } // crea un bucket si no existe

            val probe = bucket.tryConsumeAndReturnRemaining(1)
            if (probe.isConsumed) {
                // Se consume un token y se continúa la ejecución normal
                response.addHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
                filterChain.doFilter(request, response)
            } else {
                // Se ha superado el límite de peticiones
                val waitNanos = probe.nanosToWaitForRefill
                val waitSeconds = TimeUnit.NANOSECONDS.toSeconds(waitNanos) + 1
                response.setHeader("Retry-After", waitSeconds.toString())
                response.status = 429 // 429 status code = "Too Many Requests"
                response.contentType = "application/json"
                response.writer.write("""{"error":"Too Many Requests","retry_after":$waitSeconds}""")
                return
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    /**
     * Crea un nuevo [Bucket] con una capacidad de **5 tokens por minuto**.
     *
     * Cada minuto se recargan 5 tokens de forma inmediata (estrategia greedy).
     *
     * @return un nuevo bucket configurado con la política de rate limiting.
     */
    private fun newBucket(): Bucket {
        val limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))
        return Bucket.builder().addLimit(limit).build()
    }
}
