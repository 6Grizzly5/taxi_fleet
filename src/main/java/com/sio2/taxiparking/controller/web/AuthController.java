package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.*;
import com.sio2.taxiparking.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

	private final UtilisateurService s;

	public AuthController(UtilisateurService s) {
		this.s = s;
	}

	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}

	@GetMapping("/inscription")
	public String formClient(Model m) {
		m.addAttribute("request", new InscriptionRequest("", "", "", ""));
		return "auth/inscription";
	}

	@PostMapping("/inscription")
	public String client(@Valid @ModelAttribute("request") InscriptionRequest r, BindingResult b) {
		if (b.hasErrors()) return "auth/inscription";
		try {
			s.inscrireClient(r);
			return "redirect:/login?inscription=success";
		} catch (RuntimeException e) {
			b.reject("global", e.getMessage());
			return "auth/inscription";
		}
	}

	@GetMapping("/inscription/chauffeur")
	public String formDriver(Model m) {
		m.addAttribute("request", new ChauffeurInscriptionRequest(
				"", "", "", "", "", java.time.LocalDate.now(), "", "", "", 2020, 4));
		return "auth/inscription-chauffeur";
	}

	@PostMapping("/inscription/chauffeur")
	public String driver(@Valid @ModelAttribute("request") ChauffeurInscriptionRequest r, BindingResult b) {
		if (b.hasErrors()) return "auth/inscription-chauffeur";
		try {
			s.inscrireChauffeur(r);
			return "redirect:/login?inscription=chauffeur-success";
		} catch (RuntimeException e) {
			b.reject("global", e.getMessage());
			return "auth/inscription-chauffeur";
		}
	}
}
