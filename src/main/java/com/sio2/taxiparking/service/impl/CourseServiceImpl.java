package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.enumeration.*;
import com.sio2.taxiparking.exception.*;
import com.sio2.taxiparking.mapper.CourseMapper;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.CourseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courses;
    private final ClientRepository clients;
    private final ChauffeurRepository chauffeurs;
    @Value("${taxi.scoring.course-count-weight:1000.0}") private double courseCountWeight;
    @Value("${taxi.scoring.kilometrage-weight:1.0}") private double kilometrageWeight;

    public CourseServiceImpl(CourseRepository courses, ClientRepository clients, ChauffeurRepository chauffeurs) {
        this.courses = courses; this.clients = clients; this.chauffeurs = chauffeurs;
    }

    private Course create(Client client, CourseRequest request) {
        Course course = new Course();
        course.setClient(client);
        course.setDepart(request.depart().trim());
        course.setDestination(request.destination().trim());
        course.setNombrePersonnes(request.nombrePersonnes());
        course.setTarif(request.tarif());
        course.setDistance(request.distance() == null ? 0.0 : request.distance());
        course.setDateHeure(request.dateHeure() == null ? LocalDateTime.now() : request.dateHeure());
        course.setStatut(StatutCourse.EN_ATTENTE);
        return course;
    }

    @Override @Transactional
    public CourseResponse ajouter(CourseRequest request) {
        if (request.clientId() == null) throw new BusinessException("Le client est obligatoire");
        return ajouterPourClient(request.clientId(), request);
    }

    @Override @Transactional
    public CourseResponse ajouterPourClient(Long clientId, CourseRequest request) {
        Client client = clients.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        Course course = courses.save(create(client, request));
        // L'attribution automatique est déclenchée immédiatement après la création.
        // Si aucun chauffeur n'est éligible, la course reste EN_ATTENTE et pourra
        // être attribuée ultérieurement par l'administrateur.
        return attribuerAutomatiquementSiPossible(course);
    }

    @Override @Transactional(readOnly = true)
    public Page<CourseResponse> trouverTous(int page, int size, String sort, String search) {
        int safePage = Math.max(0, page), safeSize = Math.min(100, Math.max(1, size));
        String allowedSort = switch (sort == null ? "dateHeure" : sort) {
            case "id", "depart", "destination", "tarif", "distance", "dateHeure", "statut" -> sort;
            default -> "dateHeure";
        };
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(allowedSort).descending());
        String term = search == null ? "" : search.trim();
        return courses.findByDepartContainingIgnoreCaseOrDestinationContainingIgnoreCase(term, term, pageable).map(CourseMapper::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public Page<CourseResponse> pourClient(Long id, int page, int size) {
        return courses.findByClientId(id, PageRequest.of(Math.max(0,page), Math.min(100,Math.max(1,size)), Sort.by("dateHeure").descending())).map(CourseMapper::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public Page<CourseResponse> pourChauffeur(Long id, int page, int size) {
        return courses.findByChauffeurId(id, PageRequest.of(Math.max(0,page), Math.min(100,Math.max(1,size)), Sort.by("dateHeure").descending())).map(CourseMapper::toResponse);
    }

    @Override @Transactional
    public CourseResponse attribuer(Long id) {
        Course course = courses.findByIdForUpdate(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
        if (course.getStatut() != StatutCourse.EN_ATTENTE) throw new BusinessException("Seule une course en attente peut être attribuée");
        CourseResponse response = attribuerAutomatiquementSiPossible(course);
        if (response.statut() == StatutCourse.EN_ATTENTE) {
            throw new BusinessException("Aucun chauffeur éligible : vérifiez disponibilité, permis, véhicule et capacité");
        }
        return response;
    }

    /**
     * Choisit et affecte automatiquement le meilleur chauffeur éligible.
     * En cas d'indisponibilité de la flotte, conserve la course en EN_ATTENTE
     * plutôt que de la faire accepter manuellement par un chauffeur.
     */
    private CourseResponse attribuerAutomatiquementSiPossible(Course course) {
        if (course.getStatut() != StatutCourse.EN_ATTENTE) {
            return CourseMapper.toResponse(course);
        }

        var candidats = chauffeurs.findByStatut(StatutChauffeur.DISPONIBLE).stream()
                .filter(this::isDriverEligibleInGeneral)
                .filter(d -> d.getVehicule().getCapacite() >= course.getNombrePersonnes())
                .sorted(Comparator.comparingDouble(this::score))
                .toList();

        for (Chauffeur candidat : candidats) {
            Chauffeur locked = chauffeurs.findByIdForUpdate(candidat.getId()).orElse(null);
            if (locked == null || !isDriverEligibleInGeneral(locked)
                    || locked.getVehicule().getCapacite() < course.getNombrePersonnes()) {
                continue;
            }
            return assignLocked(course, locked);
        }
        return CourseMapper.toResponse(course);
    }

    private double score(Chauffeur driver) {
        return courses.countByChauffeurIdAndStatut(driver.getId(), StatutCourse.TERMINEE) * courseCountWeight
                + (driver.getVehicule() == null ? Double.MAX_VALUE / 4 : driver.getVehicule().getKilometrage() * kilometrageWeight);
    }

    private boolean isDriverEligibleInGeneral(Chauffeur d) {
        return d.getStatut() == StatutChauffeur.DISPONIBLE
                && d.getExpirationPermis() != null && !d.getExpirationPermis().isBefore(LocalDate.now())
                && d.getVehicule() != null
                && d.getVehicule().getStatut() == StatutVehicule.DISPONIBLE;
    }

    private CourseResponse assignLocked(Course course, Chauffeur driver) {
        if (course.getStatut() != StatutCourse.EN_ATTENTE) throw new BusinessException("Cette course a déjà été attribuée ou traitée");
        if (!isDriverEligibleInGeneral(driver)) throw new BusinessException("Chauffeur non éligible : statut, permis ou véhicule invalide");
        if (driver.getVehicule().getCapacite() < course.getNombrePersonnes()) throw new BusinessException("La capacité du véhicule est insuffisante");
        course.setChauffeur(driver);
        course.setVehicule(driver.getVehicule());
        course.setStatut(StatutCourse.ASSIGNEE);
        driver.setStatut(StatutChauffeur.EN_COURSE);
        driver.getVehicule().setStatut(StatutVehicule.EN_COURSE);
        chauffeurs.save(driver);
        return CourseMapper.toResponse(courses.save(course));
    }

    @Override @Transactional
    public CourseResponse demarrer(Long id, Long chauffeurId) {
        Course course = courses.findByIdForUpdate(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
        if (course.getChauffeur() == null || !course.getChauffeur().getId().equals(chauffeurId)) throw new BusinessException("Cette course ne vous appartient pas");
        if (course.getStatut() != StatutCourse.ASSIGNEE) throw new BusinessException("La course doit être assignée avant de démarrer");
        if (course.getChauffeur().getStatut() != StatutChauffeur.EN_COURSE || course.getVehicule() == null || course.getVehicule().getStatut() != StatutVehicule.EN_COURSE)
            throw new BusinessException("Le chauffeur ou le véhicule n'est pas dans un état cohérent");
        course.setStatut(StatutCourse.EN_COURS);
        return CourseMapper.toResponse(courses.save(course));
    }

    @Override @Transactional
    public CourseResponse terminer(Long id, Long chauffeurId) {
        Course course = courses.findByIdForUpdate(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
        if (course.getChauffeur() == null || !course.getChauffeur().getId().equals(chauffeurId)) throw new BusinessException("Cette course ne vous appartient pas");
        if (course.getStatut() != StatutCourse.EN_COURS) throw new BusinessException("La course doit être en cours");
        course.setStatut(StatutCourse.TERMINEE);
        Chauffeur driver = course.getChauffeur();
        driver.setStatut(StatutChauffeur.DISPONIBLE);
        if (course.getVehicule() != null) {
            course.getVehicule().setStatut(StatutVehicule.DISPONIBLE);
            course.getVehicule().setKilometrage(course.getVehicule().getKilometrage() + course.getDistance());
        }
        chauffeurs.save(driver);
        return CourseMapper.toResponse(courses.save(course));
    }

    @Override @Transactional
    public CourseResponse annuler(Long id) {
        Course course = courses.findByIdForUpdate(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
        if (course.getStatut() == StatutCourse.TERMINEE) throw new BusinessException("Une course terminée ne peut pas être annulée");
        if (course.getStatut() == StatutCourse.ANNULEE) throw new BusinessException("Course déjà annulée");
        if (course.getChauffeur() != null) {
            Chauffeur driver = chauffeurs.findByIdForUpdate(course.getChauffeur().getId()).orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable"));
            if (driver.getStatut() == StatutChauffeur.EN_COURSE) driver.setStatut(StatutChauffeur.DISPONIBLE);
            if (course.getVehicule() != null && course.getVehicule().getStatut() == StatutVehicule.EN_COURSE) course.getVehicule().setStatut(StatutVehicule.DISPONIBLE);
            chauffeurs.save(driver);
        }
        course.setStatut(StatutCourse.ANNULEE);
        return CourseMapper.toResponse(courses.save(course));
    }
}
