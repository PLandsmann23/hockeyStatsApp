package cz.landspa.statsapp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
public class SiteErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        // Získání statusového kódu
        Integer statusCode = status != null ? Integer.valueOf(status.toString()) : HttpStatus.INTERNAL_SERVER_ERROR.value();

        // Přidání atributů do modelu
        model.addAttribute("timestamp", LocalDateTime.now()); // Aktuální čas
        model.addAttribute("path", path != null ? path.toString() : "Unknown");
        model.addAttribute("status", statusCode);
        model.addAttribute("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        model.addAttribute("message", errorMessage != null ? errorMessage.toString() : "Unexpected error");


            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error";
            }


        return "error/error";
    }
}
