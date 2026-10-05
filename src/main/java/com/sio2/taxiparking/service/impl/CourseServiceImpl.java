package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.entity.Chauffeur;
import com.sio2.taxiparking.entity.Client;
import com.sio2.taxiparking.entity.Course;
import com.sio2.taxiparking.entity.Vehicule;
import com.sio2.taxiparking.enumeration.StatutChauffeur;
import com.sio2.taxiparking.enumeration.StatutCourse;
import com.sio2.taxiparking.enumeration.StatutVehicule;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.exception.ResourceNotFoundException;
import com.sio2.taxiparking.mapper.CourseMapper;
import com.sio2.taxiparking.repository.ChauffeurRepository;
import com.sio2.taxiparking.repository.ClientRepository;
import com.sio2.taxiparking.repository.CourseRepository;
import com.sio2.taxiparking.service.CourseService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;

@Service
public class CourseServiceImpl
        implements CourseService {

    private final CourseRepository repository;
    private final ClientRepository clientRepository;
    private final ChauffeurRepository chauffeurRepository;

    public CourseServiceImpl(
            CourseRepository repository,
            ClientRepository clientRepository,
            ChauffeurRepository chauffeurRepository
    ) {

        this.repository = repository;
        this.clientRepository = clientRepository;
        this.chauffeurRepository = chauffeurRepository;
    }

    @Override
    public CourseResponse ajouter(
            CourseRequest request
    ) {

        Client client =
                clientRepository.findById(
                        request.clientId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client introuvable"
                        )
                );

        Course course = new Course();

        course.setClient(client);
        course.setDepart(
                request.depart().trim()
        );
        course.setDestination(
                request.destination().trim()
        );
        course.setDistance(
                request.distance()
        );
        course.setTarif(
                request.tarif()
        );
        course.setDateHeure(
                request.dateHeure()
        );
        course.setStatut(
                StatutCourse.EN_ATTENTE
        );

        return CourseMapper.toResponse(
                repository.save(course)
        );
    }

    @Override
    public Page<CourseResponse> trouverTous(
            int page,
            int size,
            String sort,
            String search
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sort).descending()
                );

        String recherche =
                search == null
                        ? ""
                        : search.trim();

        return repository
                .findByDepartContainingIgnoreCaseOrDestinationContainingIgnoreCase(
                        recherche,
                        recherche,
                        pageable
                )
                .map(CourseMapper::toResponse);
    }

    @Override
    public CourseResponse trouverParId(
            Long id
    ) {

        return CourseMapper.toResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course introuvable"
                                )
                        )
        );
    }

    @Override
    @Transactional
    public CourseResponse attribuer(
            Long id
    ) {

        Course course =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course introuvable"
                                )
                        );

        if (course.getStatut()
                != StatutCourse.EN_ATTENTE) {

            throw new BusinessException(
                    "La course n'est plus en attente"
            );
        }

        var chauffeurs =
                chauffeurRepository
                        .findByStatutAndExpirationPermisAfter(
                                StatutChauffeur.DISPONIBLE,
                                LocalDate.now()
                        );

        Chauffeur meilleur =
                chauffeurs.stream()

                        .filter(chauffeur ->
                                chauffeur.getVehicule() != null
                        )

                        .filter(chauffeur ->
                                chauffeur
                                    .getVehicule()
                                    .getStatut()
                                    == StatutVehicule.DISPONIBLE
                        )

                        .min(
                                Comparator.comparingInt(
                                        this::calculerScore
                                )
                        )

                        .orElseThrow(() ->
                                new BusinessException(
                                        "Aucun chauffeur éligible disponible"
                                )
                        );

        Vehicule vehicule =
                meilleur.getVehicule();

        course.setChauffeur(meilleur);
        course.setVehicule(vehicule);
        course.setStatut(
                StatutCourse.ASSIGNEE
        );

        meilleur.setStatut(
                StatutChauffeur.EN_COURSE
        );

        vehicule.setStatut(
                StatutVehicule.EN_COURSE
        );

        chauffeurRepository.save(meilleur);

        return CourseMapper.toResponse(
                repository.save(course)
        );
    }

    /*
     * Score d'affectation :
     *
     * - moins de courses réalisées = meilleur
     * - véhicule moins kilométré = meilleur
     *
     * Le score reste volontairement simple
     * pour rester compréhensible lors de la soutenance.
     */
    private int calculerScore(
            Chauffeur chauffeur
    ) {

        long nombreCourses =
                repository.countByChauffeurId(
                        chauffeur.getId()
                );

        double kilometrage =
                chauffeur.getVehicule()
                        .getKilometrage();

        return (int) (
                nombreCourses * 100
                + kilometrage / 1000
        );
    }

    @Override
    @Transactional
    public CourseResponse demarrer(
            Long id
    ) {

        Course course =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course introuvable"
                                )
                        );

        if (course.getStatut()
                != StatutCourse.ASSIGNEE) {

            throw new BusinessException(
                    "La course doit être assignée avant de démarrer"
            );
        }

        course.setStatut(
                StatutCourse.EN_COURS
        );

        return CourseMapper.toResponse(
                repository.save(course)
        );
    }

    @Override
    @Transactional
    public CourseResponse terminer(
            Long id
    ) {

        Course course =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course introuvable"
                                )
                        );

        if (course.getStatut()
                != StatutCourse.EN_COURS) {

            throw new BusinessException(
                    "La course doit être en cours"
            );
        }

        course.setStatut(
                StatutCourse.TERMINEE
        );

        if (course.getChauffeur() != null) {

            course.getChauffeur()
                    .setStatut(
                            StatutChauffeur.DISPONIBLE
                    );
        }

        if (course.getVehicule() != null) {

            course.getVehicule()
                    .setStatut(
                            StatutVehicule.DISPONIBLE
                    );

            double kilometrage =
                    course.getVehicule()
                            .getKilometrage();

            course.getVehicule()
                    .setKilometrage(
                            kilometrage
                            + course.getDistance()
                    );
        }

        return CourseMapper.toResponse(
                repository.save(course)
        );
    }

    @Override
    @Transactional
    public CourseResponse annuler(
            Long id
    ) {

        Course course =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course introuvable"
                                )
                        );

        if (course.getStatut()
                == StatutCourse.TERMINEE) {

            throw new BusinessException(
                    "Une course terminée ne peut pas être annulée"
            );
        }

        if (course.getStatut()
                == StatutCourse.EN_COURS) {

            throw new BusinessException(
                    "Une course en cours ne peut pas être annulée"
            );
        }

        if (course.getChauffeur() != null) {

            course.getChauffeur()
                    .setStatut(
                            StatutChauffeur.DISPONIBLE
                    );
        }

        if (course.getVehicule() != null) {

            course.getVehicule()
                    .setStatut(
                            StatutVehicule.DISPONIBLE
                    );
        }

        course.setStatut(
                StatutCourse.ANNULEE
        );

        return CourseMapper.toResponse(
                repository.save(course)
        );
    }
}