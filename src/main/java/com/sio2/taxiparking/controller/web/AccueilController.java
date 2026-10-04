package com.sio2.taxiparking.controller.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AccueilController {
    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("title", "Gestion de flotte de taxis");
        return "accueil";
    }
}
