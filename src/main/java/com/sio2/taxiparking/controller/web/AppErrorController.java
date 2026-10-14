package com.sio2.taxiparking.controller.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        Object code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = 500;
        if (code != null) {
            try { status = Integer.parseInt(code.toString()); } catch (NumberFormatException ignored) {}
        }
        String title;
        String message;
        switch (status) {
            case 400 -> { title = "Demande invalide"; message = "Certaines informations envoyées sont invalides. Vérifiez votre saisie puis réessayez."; }
            case 401 -> { title = "Connexion requise"; message = "Connectez-vous pour accéder à cette page."; }
            case 403 -> { title = "Accès refusé"; message = "Désolé, vous n’avez pas l’autorisation d’accéder à cette page."; }
            case 404 -> { title = "Page introuvable"; message = "Désolé, nous n’avons pas pu accéder à cette page. Le lien est peut-être incorrect ou la page a été déplacée."; }
            case 503 -> { title = "Service temporairement indisponible"; message = "Le service est momentanément indisponible. Réessayez dans quelques instants."; }
            default -> { title = "Une erreur est survenue"; message = "Désolé, nous n’avons pas pu traiter votre demande. Réessayez dans quelques instants."; }
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", status);
        mav.addObject("errorTitle", title);
        mav.addObject("errorMessage", message);
        mav.setStatus(HttpStatusCode.valueOf(status >= 400 && status <= 599 ? status : 500));
        return mav;
    }

    @GetMapping("/erreur/acces-refuse")
    public ModelAndView accessDenied() {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 403);
        mav.addObject("errorTitle", "Accès refusé");
        mav.addObject("errorMessage", "Désolé, vous n’avez pas l’autorisation d’accéder à cette page.");
        mav.setStatus(HttpStatus.FORBIDDEN);
        return mav;
    }
}
