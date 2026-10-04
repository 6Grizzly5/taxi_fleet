package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.ChauffeurRequest;
import com.sio2.taxiparking.service.ChauffeurService;
import com.sio2.taxiparking.service.VehiculeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chauffeurs")
public class ChauffeurController {

    private final ChauffeurService chauffeurService;
    private final VehiculeService vehiculeService;

    public ChauffeurController(
            ChauffeurService chauffeurService,
            VehiculeService vehiculeService
    ) {
        this.chauffeurService = chauffeurService;
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    public String liste(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sort,
            @RequestParam(required = false) String search,
            Model model
    ) {

        model.addAttribute(
                "chauffeurs",
                chauffeurService.trouverTous(
                        page,
                        size,
                        sort,
                        search
                )
        );

        model.addAttribute("search", search);

        return "chauffeurs/liste";
    }

    @GetMapping("/nouveau")
    public String formulaire(Model model) {

        model.addAttribute(
                "chauffeur",
                new ChauffeurRequest(
                        "",
                        "",
                        "",
                        null,
                        null,
                        null
                )
        );

        model.addAttribute(
                "vehicules",
                vehiculeService.trouverTous(
                        0,
                        100,
                        "immatriculation",
                        null
                )
        );

        return "chauffeurs/formulaire";
    }

    @PostMapping
    public String ajouter(
            @Valid @ModelAttribute("chauffeur")
            ChauffeurRequest request,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "vehicules",
                    vehiculeService.trouverTous(
                            0,
                            100,
                            "immatriculation",
                            null
                    )
            );

            return "chauffeurs/formulaire";
        }

        chauffeurService.ajouter(request);

        return "redirect:/chauffeurs";
    }

    @GetMapping("/{id}/modifier")
    public String modifierFormulaire(
            @PathVariable Long id,
            Model model
    ) {

        var chauffeur = chauffeurService.trouverParId(id);

        ChauffeurRequest request = new ChauffeurRequest(
                chauffeur.nom(),
                chauffeur.telephone(),
                chauffeur.numeroPermis(),
                chauffeur.expirationPermis(),
                chauffeur.dateEmbauche(),
                chauffeur.vehiculeId()
        );

        model.addAttribute("chauffeur", request);
        model.addAttribute("id", id);

        model.addAttribute(
                "vehicules",
                vehiculeService.trouverTous(
                        0,
                        100,
                        "immatriculation",
                        null
                )
        );

        return "chauffeurs/formulaire";
    }

    @PostMapping("/{id}")
    public String modifier(
            @PathVariable Long id,
            @Valid @ModelAttribute("chauffeur")
            ChauffeurRequest request,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute("id", id);

            model.addAttribute(
                    "vehicules",
                    vehiculeService.trouverTous(
                            0,
                            100,
                            "immatriculation",
                            null
                    )
            );

            return "chauffeurs/formulaire";
        }

        chauffeurService.modifier(id, request);

        return "redirect:/chauffeurs";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id) {

        chauffeurService.supprimer(id);

        return "redirect:/chauffeurs";
    }
}