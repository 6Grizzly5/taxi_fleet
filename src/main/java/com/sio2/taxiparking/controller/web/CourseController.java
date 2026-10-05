package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.service.ClientService;
import com.sio2.taxiparking.service.CourseService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final ClientService clientService;

    public CourseController(
            CourseService courseService,
            ClientService clientService
    ) {
        this.courseService = courseService;
        this.clientService = clientService;
    }

    @GetMapping
    public String liste(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateHeure") String sort,
            @RequestParam(required = false) String search,
            Model model
    ) {

        model.addAttribute(
                "courses",
                courseService.trouverTous(
                        page,
                        size,
                        sort,
                        search
                )
        );

        model.addAttribute(
                "search",
                search
        );

        return "courses/liste";
    }

    @GetMapping("/nouveau")
    public String nouveau(
            Model model
    ) {

        model.addAttribute(
                "course",
                new CourseRequest(
                        null,
                        "",
                        "",
                        null,
                        null,
                        null
                )
        );

        model.addAttribute(
                "clients",
                clientService.trouverTous(
                        0,
                        100,
                        "nom",
                        null
                )
        );

        return "courses/formulaire";
    }

    @PostMapping
    public String ajouter(
            @Valid @ModelAttribute("course")
            CourseRequest request,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "clients",
                    clientService.trouverTous(
                            0,
                            100,
                            "nom",
                            null
                    )
            );

            return "courses/formulaire";
        }

        courseService.ajouter(request);

        return "redirect:/courses";
    }

    @PostMapping("/{id}/assigner")
    public String assigner(
            @PathVariable Long id
    ) {

        courseService.attribuer(id);

        return "redirect:/courses";
    }

    @PostMapping("/{id}/demarrer")
    public String demarrer(
            @PathVariable Long id
    ) {

        courseService.demarrer(id);

        return "redirect:/courses";
    }

    @PostMapping("/{id}/terminer")
    public String terminer(
            @PathVariable Long id
    ) {

        courseService.terminer(id);

        return "redirect:/courses";
    }

    @PostMapping("/{id}/annuler")
    public String annuler(
            @PathVariable Long id
    ) {

        courseService.annuler(id);

        return "redirect:/courses";
    }
}