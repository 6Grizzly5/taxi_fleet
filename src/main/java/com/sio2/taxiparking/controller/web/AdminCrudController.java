package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.*;
import com.sio2.taxiparking.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminCrudController {

	private final ClientService clients;
	private final VehiculeService vehicules;
	private final ChauffeurService chauffeurs;

	public AdminCrudController(ClientService c, VehiculeService v, ChauffeurService d) {
		clients = c;
		vehicules = v;
		chauffeurs = d;
	}

	@GetMapping("/clients")
	public String cl(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			Model m) {
		m.addAttribute("page", clients.trouverTous(page, size, "nom", search));
		m.addAttribute("search", search);
		return "clients/liste";
	}

	@GetMapping("/vehicules")
	public String ve(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			Model m) {
		m.addAttribute("page", vehicules.trouverTous(page, size, "immatriculation", search));
		m.addAttribute("search", search);
		return "vehicules/liste";
	}

	@GetMapping("/chauffeurs")
	public String ch(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			Model m) {
		m.addAttribute("page", chauffeurs.trouverTous(page, size, "nom", search));
		m.addAttribute("search", search);
		return "chauffeurs/liste";
	}
}
