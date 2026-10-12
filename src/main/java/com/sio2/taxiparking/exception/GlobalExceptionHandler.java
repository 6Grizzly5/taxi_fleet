package com.sio2.taxiparking.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object notFound(ResourceNotFoundException e, HttpServletRequest request) {
        if (isApi(request)) return ResponseEntity.status(404).body(Map.of("error", safeMessage(e, "Ressource introuvable")));
        return page(404, "Page introuvable", "Désolé, nous n’avons pas pu accéder à la ressource demandée.");
    }

    @ExceptionHandler(BusinessException.class)
    public Object business(BusinessException e, HttpServletRequest request) {
        if (isApi(request)) return ResponseEntity.badRequest().body(Map.of("error", safeMessage(e, "Opération impossible")));
        return page(400, "Opération impossible", safeMessage(e, "Votre demande n’a pas pu être traitée."));
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public Object invalidInput(Exception e, HttpServletRequest request) {
        String message = "Certaines informations sont invalides. Vérifiez les champs puis réessayez.";
        if (isApi(request)) return ResponseEntity.badRequest().body(Map.of("error", message));
        return page(400, "Vérifiez votre saisie", message);
    }

    @ExceptionHandler(Exception.class)
    public Object other(Exception e, HttpServletRequest request) {
        if (isApi(request)) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Erreur interne",
                    "detail", safeMessage(e, "Une erreur interne est survenue.")));
        }
        return page(500, "Une erreur est survenue", "Désolé, nous n’avons pas pu traiter votre demande. Réessayez dans quelques instants.");
    }

    private boolean isApi(HttpServletRequest request) {
        return request.getRequestURI() != null && request.getRequestURI().startsWith("/api/");
    }

    private String safeMessage(Exception e, String fallback) {
        return e.getMessage() == null || e.getMessage().isBlank() ? fallback : e.getMessage();
    }

    private ModelAndView page(int status, String title, String message) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", status);
        mav.addObject("errorTitle", title);
        mav.addObject("errorMessage", message);
        mav.setStatus(org.springframework.http.HttpStatusCode.valueOf(status));
        return mav;
    }
}
