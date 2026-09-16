package comman.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public RoleHierarchy roleHierarchy() {
                return RoleHierarchyImpl.withDefaultRolePrefix()
                                .role("SUPER_ADMIN").implies("ADMIN")
                                .build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.setContentType("application/json");
                                                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                                        response.getWriter().write(
                                                                        "{\"success\":false,\"status\":401,\"error\":\"UNAUTHORIZED\",\"message\":\"Unauthorized: "
                                                                                        + authException.getMessage()
                                                                                        + "\",\"path\":\""
                                                                                        + request.getRequestURI()
                                                                                        + "\"}");
                                                })
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        response.setContentType("application/json");
                                                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                                        response.getWriter().write(
                                                                        "{\"success\":false,\"status\":403,\"error\":\"FORBIDDEN\",\"message\":\"Forbidden: Access denied.\",\"path\":\""
                                                                                        + request.getRequestURI()
                                                                                        + "\"}");
                                                }))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/auth/**", "/api/auth/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/categories",
                                                                "/api/categories/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/admin/categories",
                                                                "/api/admin/categories/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/admin/products",
                                                                "/api/admin/products/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/products/list",
                                                                "/api/admin/products/list")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/categories/list",
                                                                "/api/admin/categories/list")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/admin/dashboard",
                                                                "/api/admin/dashboard/**", "/api/dashboard", "/api/dashboard/**")
                                                .permitAll()
                                                .requestMatchers("/api/admin/**", "/api/v1/admin/**",
                                                                "/api/products/addorUpdate", "/api/products/delete/**",
                                                                "/api/categories/addorUpdate", "/api/categories/delete/**")
                                                .hasAnyRole("ADMIN", "SUPER_ADMIN")
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(List.of("*"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(
                                Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
                config.setExposedHeaders(List.of("Authorization"));
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
