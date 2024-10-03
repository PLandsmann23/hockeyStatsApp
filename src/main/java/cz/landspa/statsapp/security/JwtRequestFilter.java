package cz.landspa.statsapp.security;

import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.service.impl.UserDetailsServiceImpl;
import cz.landspa.statsapp.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);


    public JwtRequestFilter(UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {





       try {
            String jwt = extractJwtFromCookies(request);
            String username = null;

            if (jwt != null) {
                logger.debug("JWT token nalezen: {}", jwt);
                username = jwtUtil.extractUsername(jwt);
                logger.debug("Extrahováno uživatelské jméno z tokenu: {}", username);
            }



            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                logger.debug("Načteno UserDetails pro uživatele: {}", username);

                if (jwtUtil.validateToken(jwt, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    authToken.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));


                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new JwtException("Token není platný");

                }
            } else {
                logger.debug("JWT token nebyl nalezen nebo uživatel je již autentizován");
            }


            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException e) {
           // Logování chyby
           logger.error("Chyba při validaci JWT tokenu: {}", e.getMessage());
           Cookie cookie = new Cookie("JWT_TOKEN", null);
           cookie.setHttpOnly(true);
           cookie.setSecure(false); // Změň na true, pokud používáš HTTPS
           cookie.setPath("/"); // Ujisti se, že máš správnou cestu
           cookie.setMaxAge(0); // Nastavení na 0 znamená, že se cookie smaže
           response.addCookie(cookie);

           // Vymazání security contextu a přesměrování na přihlášení
           SecurityContextHolder.clearContext();
           response.sendRedirect("/login");
           return;
       }
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("JWT_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
