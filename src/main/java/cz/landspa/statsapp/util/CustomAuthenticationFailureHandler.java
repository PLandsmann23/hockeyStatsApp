package cz.landspa.statsapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {


    private final ObjectMapper objectMapper;

    public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errorMessage;
        if (exception.getMessage().contains("Username")) {
            errorMessage = "un";
        }else if(exception.getMessage().contains("not enabled")) {
            errorMessage = "ne";
        } else {
            errorMessage = "p";
        }

        // Přesměrování zpět na přihlašovací stránku s chybovou zprávou
        response.sendRedirect(request.getContextPath() + "/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }


}
