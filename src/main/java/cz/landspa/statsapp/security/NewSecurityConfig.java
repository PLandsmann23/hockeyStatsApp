package cz.landspa.statsapp.security;

import cz.landspa.statsapp.util.CustomAuthenticationFailureHandler;
import cz.landspa.statsapp.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class NewSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private final CustomAuthenticationProvider customAuthenticationProvider;

    private final JwtUtil jwtUtil;


    public NewSecurityConfig(JwtRequestFilter jwtRequestFilter, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationProvider customAuthenticationProvider, JwtUtil jwtUtil) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // Ignoruje CSRF pro API
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/css/**", "/img/**", "/js/**", "/svg/**").permitAll()
                        .requestMatchers("/login", "/activate", "/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
                        .requestMatchers("/","/**").authenticated()
                        .anyRequest().permitAll()
                )
              /*  .securityContext(context -> context
                        .requireExplicitSave(false)
                ) */
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")

                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler((request, response, authentication) -> {
                            String token = jwtUtil.generateToken(authentication.getName());
                            Cookie[] cookies = request.getCookies();
                            if (cookies != null) {
                                for (Cookie cookie : cookies) {
                                    if (cookie.isHttpOnly()) {
                                        cookie.setMaxAge(0); // Nastavení maxAge na 0 vymaže cookie
                                        cookie.setPath("/"); // Ujistěte se, že nastavujete správnou cestu
                                        response.addCookie(cookie);
                                    }
                                }
                            }
                            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
                            jwtCookie.setHttpOnly(true);
                            jwtCookie.setSecure(false);
                            jwtCookie.setPath("/");
                            jwtCookie.setMaxAge(7 * 24 * 60 * 60);

                            response.addCookie(jwtCookie);
                            response.sendRedirect("/");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );



        return http.build();
    }



}
