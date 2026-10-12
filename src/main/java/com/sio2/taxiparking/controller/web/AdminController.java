package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.enumeration.StatutCourse;
import com.sio2.taxiparking.enumeration.StatutPlace;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.ChauffeurService;
import com.sio2.taxiparking.service.ParkingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UtilisateurRepository ur;
    private final ClientRepository cr;
    private final ChauffeurRepository chr;
    private final VehiculeRepository vr;
    private final CourseRepository cor;
    private final PlaceRepository pr;
    private final ReservationRepository rr;
    private final ChauffeurService cs;
    private final ParkingService parkingService;

    public AdminController(UtilisateurRepository ur, ClientRepository cr,
            ChauffeurRepository chr, VehiculeRepository vr, CourseRepository cor,
            PlaceRepository pr, ReservationRepository rr, ChauffeurService cs,
            ParkingService parkingService) {
        this.ur = ur;
        this.cr = cr;
        this.chr = chr;
        this.vr = vr;
        this.cor = cor;
        this.pr = pr;
        this.rr = rr;
        this.cs = cs;
        this.parkingService = parkingService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m) {
        m.addAttribute("users", ur.count());
        m.addAttribute("clients", cr.count());
        m.addAttribute("chauffeurs", chr.count());
        m.addAttribute("vehicules", vr.count());
        m.addAttribute("courses", cor.count());
        m.addAttribute("pendingCourses", cor.countByStatut(StatutCourse.EN_ATTENTE));
        m.addAttribute("finished", cor.countByStatut(StatutCourse.TERMINEE));
        m.addAttribute("places", pr.count());
        m.addAttribute("freePlaces", pr.countByStatut(StatutPlace.LIBRE));
        m.addAttribute("reservations", rr.count());
        m.addAttribute("drivers", chr.findAll());
        m.addAttribute("recentCourses", cor.findAll(PageRequest.of(0, 20, Sort.by("dateHeure").descending())).getContent());
        return "admin/dashboard";
    }

    @GetMapping("/parking")
    public String parking(Model m,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        m.addAttribute("placesList", pr.findAll());
        m.addAttribute("reservationsPage", parkingService.reservations(Math.max(0, page), Math.min(100, Math.max(1, size))));
        return "admin/parking";
    }

    @PostMapping("/chauffeur/{id}/valider")
    public String validate(@PathVariable Long id) {
        cs.valider(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/chauffeur/{id}/suspendre")
    public String suspend(@PathVariable Long id) {
        cs.suspendre(id);
        return "redirect:/admin/dashboard";
    }
}
