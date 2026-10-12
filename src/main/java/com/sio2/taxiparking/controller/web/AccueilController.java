package com.sio2.taxiparking.controller.web;

import org.springframework.security.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

	@GetMapping({"/", "/accueil"})
	public String home(Authentication a) {
		if (a == null) return "redirect:/login";
		if (a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/admin/dashboard";
		}
		if (a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_CLIENT"))) {
			return "redirect:/client/dashboard";
		}
		return "redirect:/chauffeur/dashboard";
	}
}
