package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.InscriptionRequest;
import com.sio2.taxiparking.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UtilisateurService utilisateurService;

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/inscription")
    public String inscriptionForm(Model model) {
        model.addAttribute("inscription", new InscriptionRequest("", "", ""));
        return "auth/inscription";
    }

    @PostMapping("/inscription")
    public String inscrire(@Valid InscriptionRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/inscription";
        }

        try {
            utilisateurService.inscrire(request);
            return "redirect:/login?inscription=success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/inscription";
        }
    }
}
