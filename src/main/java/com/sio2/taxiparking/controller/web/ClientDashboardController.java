package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientDashboardController {

	private final UtilisateurRepository ur;
	private final ClientRepository cr;
	private final CourseService cs;

	public ClientDashboardController(UtilisateurRepository ur, ClientRepository cr, CourseService cs) {
		this.ur = ur;
		this.cr = cr;
		this.cs = cs;
	}

	@GetMapping("/client/dashboard")
	public String dashboard(Authentication a, Model m) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		Client c = cr.findByUtilisateurId(u.getId()).orElseThrow();
		m.addAttribute("client", c);
		m.addAttribute("courses", cs.pourClient(c.getId(), 0, 20).getContent());
		return "clients/dashboard";
	}
}
