package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientCourseController {

	private final UtilisateurRepository ur;
	private final ClientRepository cr;
	private final CourseService cs;

	public ClientCourseController(UtilisateurRepository ur, ClientRepository cr, CourseService cs) {
		this.ur = ur;
		this.cr = cr;
		this.cs = cs;
	}

	@GetMapping("/client/course/nouveau")
	public String form(Model m) {
		m.addAttribute("request", new CourseRequest(null, "", "", 1, 1000.0));
		return "clients/course-form";
	}

	@PostMapping("/client/course")
	public String create(@Valid @ModelAttribute("request") CourseRequest r, BindingResult b, Authentication a) {
		if (b.hasErrors()) return "clients/course-form";
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Client c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		cs.ajouterPourClient(c.getId(), r);
		return "redirect:/client/dashboard";
	}
}
