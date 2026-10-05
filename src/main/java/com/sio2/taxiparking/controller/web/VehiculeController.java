package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.VehiculeRequest;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import com.sio2.taxiparking.service.VehiculeService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehicules")
public class VehiculeController {

    private final VehiculeService service;

    public VehiculeController(
            VehiculeService service
    ) {
        this.service = service;
    }

    @GetMapping
    public String liste(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "immatriculation") String sort,
            @RequestParam(required = false) String search,
            Model model
    ) {

        model.addAttribute(
                "vehicules",
                service.trouverTous(
                        page,
                        size,
                        sort,
                        search
                )
        );

        model.addAttribute("search", search);

        return "vehicules/liste";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {

        model.addAttribute(
                "vehicule",
                new VehiculeRequest(
                        "",
                        "",
                        "",
                        null,
                        null,
                        0.0
                )
        );

        return "vehicules/formulaire";
    }

    @PostMapping
    public String ajouter(
            @Valid @ModelAttribute("vehicule")
            VehiculeRequest request,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return "vehicules/formulaire";
        }

        service.ajouter(request);

        return "redirect:/vehicules";
    }

    @GetMapping("/{id}/modifier")
    public String modifierFormulaire(
            @PathVariable Long id,
            Model model
    ) {

        VehiculeResponse v =
                service.trouverParId(id);

        model.addAttribute(
                "vehicule",
                new VehiculeRequest(
                        v.immatriculation(),
                        v.marque(),
                        v.modele(),
                        v.annee(),
                        v.capacite(),
                        v.kilometrage()
                )
        );

        model.addAttribute("id", id);

        return "vehicules/formulaire";
    }

    @PostMapping("/{id}")
    public String modifier(
            @PathVariable Long id,
            @Valid @ModelAttribute("vehicule")
            VehiculeRequest request,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute("id", id);

            return "vehicules/formulaire";
        }

        service.modifier(id, request);

        return "redirect:/vehicules";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(
            @PathVariable Long id
    ) {

        service.supprimer(id);

        return "redirect:/vehicules";
    }
}