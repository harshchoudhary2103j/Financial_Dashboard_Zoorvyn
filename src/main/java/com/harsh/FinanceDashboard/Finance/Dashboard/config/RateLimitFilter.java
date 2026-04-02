package com.harsh.FinanceDashboard.Finance.Dashboard.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Each IP gets its own bucket
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(15)
                        .refillGreedy(15, Duration.ofMinutes(1)))
                .build();
    }

    private Bucket getBucketForIP(String ip) {
        return buckets.computeIfAbsent(ip, k -> createNewBucket());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = getBucketForIP(ip);

        if (bucket.tryConsume(1)) {
            // Add remaining requests info in response headers
            response.addHeader("X-Rate-Limit-Remaining",
                    String.valueOf(bucket.getAvailableTokens()));
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                    {
                        "data": null,
                        "error": {
                            "message": "Too many requests. Please try again after a minute.",
                            "status": "429 TOO_MANY_REQUESTS",
                            "subErrors": null
                        },
                        "timeStamp": "%s"
                    }
                    """.formatted(java.time.LocalDateTime.now()));
        }
    }
}
