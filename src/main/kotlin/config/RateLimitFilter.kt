package es.unizar.webeng.hello.config

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
class RateLimitFilter : OncePerRequestFilter() {

    // cache is a thread-safe map that associates a key (the client IP) with a Bucket4j Bucket
    private val cache = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(10))
        .build<String, Bucket>()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.startsWith("/api/")) {
            val ip = request.remoteAddr
            val bucket = cache.get(ip) { newBucket() } // Inactive buckets are deleted after 10 minutes

            val probe = bucket.tryConsumeAndReturnRemaining(1)
            if (probe.isConsumed) { // token can be consumed
                response.addHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
                filterChain.doFilter(request, response)
            } else {
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

    private fun newBucket(): Bucket {
        // every minute adds up to 5 tokens
        val limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))
        return Bucket.builder().addLimit(limit).build()
    }
}
