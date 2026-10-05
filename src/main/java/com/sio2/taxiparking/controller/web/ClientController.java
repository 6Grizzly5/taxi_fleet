package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.dto.request.ClientRequest;
import com.sio2.taxiparking.dto.response.ClientResponse;
import com.sio2.taxiparking.service.ClientService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
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
                "clients",
                service.trouverTous(
                        page,
                        size,
                        sort,
                        search
                )
        );

        model.addAttribute("search", search);

        return "clients/liste";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {

        model.addAttribute(
                "client",
                new ClientRequest(
                        "",
                        "",
                        ""
                )
        );

        return "clients/formulaire";
    }

    @PostMapping
    public String ajouter(
            @Valid @ModelAttribute("client")
            ClientRequest request,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {
            return "clients/formulaire";
        }

        service.ajouter(request);

        return "redirect:/clients";
    }

    @GetMapping("/{id}/modifier")
    public String modifierFormulaire(
            @PathVariable Long id,
            Model model
    ) {

        ClientResponse client =
                service.trouverParId(id);

        model.addAttribute(
                "client",
                new ClientRequest(
                        client.nom(),
                        client.telephone(),
                        client.email()
                )
        );

        model.addAttribute("id", id);

        return "clients/formulaire";
    }

    @PostMapping("/{id}")
    public String modifier(
            @PathVariable Long id,
            @Valid @ModelAttribute("client")
            ClientRequest request,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute("id", id);

            return "clients/formulaire";
        }

        service.modifier(id, request);

        return "redirect:/clients";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(
            @PathVariable Long id
    ) {

        service.supprimer(id);

        return "redirect:/clients";
    }
}