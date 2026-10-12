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
	private final CourseRepository cor;
	private final CourseService cs;

	public ChauffeurDashboardController(
			UtilisateurRepository ur,
			ChauffeurRepository cr,
			CourseRepository cor,
			CourseService cs) {
		this.ur = ur;
		this.cr = cr;
		this.cor = cor;
		this.cs = cs;
	}

	@GetMapping("/chauffeur/dashboard")
	public String dash(Authentication a, Model m) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Chauffeur c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		m.addAttribute("chauffeur", c);
		m.addAttribute("pending", cor.findByStatut(
				com.sio2.taxiparking.enumeration.StatutCourse.EN_ATTENTE,
				org.springframework.data.domain.PageRequest.of(0, 50))
				.getContent().stream().map(com.sio2.taxiparking.mapper.CourseMapper::toResponse).toList());
		m.addAttribute("courses", cs.pourChauffeur(c.getId(), 0, 50).getContent());
		return "chauffeurs/dashboard";
	}

	@PostMapping("/chauffeur/course/{id}/accepter")
	public String accept(@PathVariable Long id, Authentication a) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Chauffeur c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		cs.accepter(id, c.getId());
		return "redirect:/chauffeur/dashboard";
	}

	@PostMapping("/chauffeur/course/{id}/refuser")
	public String reject(@PathVariable Long id, Authentication a) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Chauffeur c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		cs.refuser(id, c.getId());
		return "redirect:/chauffeur/dashboard";
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
