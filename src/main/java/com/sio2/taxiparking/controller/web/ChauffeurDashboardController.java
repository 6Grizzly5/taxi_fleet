package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChauffeurDashboardController {

	private final UtilisateurRepository ur;
	private final ChauffeurRepository cr;
	private final CourseService cs;

	public ChauffeurDashboardController(
			UtilisateurRepository ur,
			ChauffeurRepository cr,
			CourseService cs) {
		this.ur = ur;
		this.cr = cr;
		this.cs = cs;
	}

	@GetMapping("/chauffeur/dashboard")
	public String dash(Authentication a, Model m) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Chauffeur c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		m.addAttribute("chauffeur", c);
		m.addAttribute("courses", cs.pourChauffeur(c.getId(), 0, 50).getContent());
		return "chauffeurs/dashboard";
	}

	@PostMapping("/chauffeur/course/{id}/demarrer")
	public String start(@PathVariable Long id, Authentication a) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Chauffeur c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		cs.demarrer(id, c.getId());
		return "redirect:/chauffeur/dashboard";
	}

	@PostMapping("/chauffeur/course/{id}/terminer")
	public String finish(@PathVariable Long id, Authentication a) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Chauffeur c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		cs.terminer(id, c.getId());
		return "redirect:/chauffeur/dashboard";
	}
}
