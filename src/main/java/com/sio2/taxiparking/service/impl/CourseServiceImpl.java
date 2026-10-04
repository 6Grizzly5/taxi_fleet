package com.sio2.taxiparking.service.impl;
import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.enumeration.*;
import com.sio2.taxiparking.exception.*;
import com.sio2.taxiparking.mapper.CourseMapper;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.CourseService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository repository;
    private final ClientRepository clientRepository;
    private final ChauffeurRepository chauffeurRepository;

    public CourseServiceImpl(CourseRepository repository, ClientRepository clientRepository, ChauffeurRepository chauffeurRepository) {
        this.repository = repository; this.clientRepository = clientRepository; this.chauffeurRepository = chauffeurRepository;
    }

    public CourseResponse ajouter(CourseRequest r) {
        Client client = clientRepository.findById(r.clientId())
            .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        Course c = new Course();
        c.setClient(client); c.setDepart(r.depart()); c.setDestination(r.destination());
        c.setDistance(r.distance()); c.setTarif(r.tarif()); c.setDateHeure(r.dateHeure());
        return CourseMapper.toResponse(repository.save(c));
    }

    public Page<CourseResponse> trouverTous(int page, int size, String sort) {
        return repository.findAll(PageRequest.of(page, size, Sort.by(sort).descending())).map(CourseMapper::toResponse);
    }

    public CourseResponse trouverParId(Long id) {
        return CourseMapper.toResponse(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course introuvable")));
    }

    @Transactional
    public CourseResponse attribuer(Long id) {
        Course c = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
        if (c.getStatut() != StatutCourse.EN_ATTENTE) throw new BusinessException("La course n'est plus en attente");

        var chauffeurs = chauffeurRepository.findByStatutAndExpirationPermisAfter(StatutChauffeur.DISPONIBLE, LocalDate.now());
        Chauffeur meilleur = chauffeurs.stream()
            .filter(x -> x.getVehicule() != null && x.getVehicule().getStatut() == StatutVehicule.DISPONIBLE)
            .min((a,b) -> Long.compare(repository.countByChauffeurId(a.getId()), repository.countByChauffeurId(b.getId())))
            .orElseThrow(() -> new BusinessException("Aucun chauffeur éligible disponible"));

        c.setChauffeur(meilleur);
        c.setVehicule(meilleur.getVehicule());
        c.setStatut(StatutCourse.ASSIGNEE);
        meilleur.setStatut(StatutChauffeur.EN_COURSE);
        meilleur.getVehicule().setStatut(StatutVehicule.EN_COURSE);
        chauffeurRepository.save(meilleur);
        return CourseMapper.toResponse(repository.save(c));
    }

    @Transactional
    public CourseResponse demarrer(Long id) {
        Course c = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
        if (c.getStatut() != StatutCourse.ASSIGNEE) throw new BusinessException("La course doit être assignée");
        c.setStatut(StatutCourse.EN_COURS);
        return CourseMapper.toResponse(repository.save(c));
    }

    @Transactional
    public CourseResponse terminer(Long id) {
        Course c = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
        if (c.getStatut() != StatutCourse.EN_COURS) throw new BusinessException("La course doit être en cours");
        c.setStatut(StatutCourse.TERMINEE);
        if (c.getChauffeur() != null) c.getChauffeur().setStatut(StatutChauffeur.DISPONIBLE);
        if (c.getVehicule() != null) {
            c.getVehicule().setStatut(StatutVehicule.DISPONIBLE);
            c.getVehicule().setKilometrage(c.getVehicule().getKilometrage() + c.getDistance());
        }
        return CourseMapper.toResponse(repository.save(c));
    }
}
