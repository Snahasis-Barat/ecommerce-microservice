package com.example.api_gateway.config;

import com.example.api_gateway.Exception.UserNotAuthenticatedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private String secretkey = "my-very-secret-key-for-jwt-signing@123456";

    @Override
    public int getOrder() {
        return -1;
    }

//    public JwtAuthenticationFilter() {
//
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private SecretKey getKey() {
        byte[] keyBytes = secretkey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Bypass authentication for public endpoints like login/register
        if (request.getPath().toString().contains("/users/login") ||
                request.getPath().toString().contains("/users/register") || request.getPath().toString().contains("/users/resetPassword") || request.getPath().toString().contains("/users/verifyUser") || request.getPath().toString().contains("/products/getProducts") || request.getPath().toString().contains("/products/addProducts")|| request.getPath().toString().contains("/products/getProductById/{id}")) {
            return chain.filter(exchange);
        }

        // Check for Authorization Header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            String body = "{\"error\": \"" + "User not authenticated" + "\"}";

            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(body.getBytes()))
            );
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Optionally forward user info
            ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("x-user-id", claims.getSubject()) // optional
                    .build();

            exchange = exchange.mutate().request(modifiedRequest).build();

        } catch (UserNotAuthenticatedException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            String body = "{\"error\": \"" + e.getMessage() + "\"}";

            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(body.getBytes()))
            );
        }

        return chain.filter(exchange);
    }

//    @Override
//    public int getOrder() {
//        return -1; // Ensures this filter runs before routes are processed
//    }


}
