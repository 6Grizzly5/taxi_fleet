package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

	private final CourseService s;

	public CourseController(CourseService s) {
		this.s = s;
	}

	@GetMapping
	public String list(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			Model m) {
		m.addAttribute("page", s.trouverTous(page, size, "dateHeure", search));
		return "courses/liste";
	}

	@PostMapping("/{id}/assigner")
	public String assign(@PathVariable Long id) {
		s.attribuer(id);
		return "redirect:/courses";
	}

	@PostMapping("/{id}/annuler")
	public String cancel(@PathVariable Long id) {
		s.annuler(id);
		return "redirect:/courses";
	}
}
