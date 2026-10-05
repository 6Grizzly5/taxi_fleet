package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.VehiculeRequest;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import com.sio2.taxiparking.entity.Vehicule;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.exception.ResourceNotFoundException;
import com.sio2.taxiparking.mapper.VehiculeMapper;
import com.sio2.taxiparking.repository.VehiculeRepository;
import com.sio2.taxiparking.service.VehiculeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VehiculeServiceImpl
        implements VehiculeService {

    private final VehiculeRepository repository;

    public VehiculeServiceImpl(
            VehiculeRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public VehiculeResponse ajouter(
            VehiculeRequest request
    ) {

        String immatriculation =
                request.immatriculation()
                        .trim()
                        .toUpperCase();

        if (repository.existsByImmatriculation(
                immatriculation
        )) {

            throw new BusinessException(
                    "Cette immatriculation existe déjà"
            );
        }

        Vehicule vehicule = new Vehicule();

        vehicule.setImmatriculation(
                immatriculation
        );

        vehicule.setMarque(
                request.marque().trim()
        );

        vehicule.setModele(
                request.modele().trim()
        );

        vehicule.setAnnee(
                request.annee()
        );

        vehicule.setCapacite(
                request.capacite()
        );

        vehicule.setKilometrage(
                request.kilometrage()
        );

        return VehiculeMapper.toResponse(
                repository.save(vehicule)
        );
    }

    @Override
    public VehiculeResponse modifier(
            Long id,
            VehiculeRequest request
    ) {

        Vehicule vehicule =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Véhicule introuvable"
                                )
                        );

        String immatriculation =
                request.immatriculation()
                        .trim()
                        .toUpperCase();

        if (repository.existsByImmatriculationAndIdNot(
                immatriculation,
                id
        )) {

            throw new BusinessException(
                    "Cette immatriculation est déjà utilisée"
            );
        }

        vehicule.setImmatriculation(
                immatriculation
        );

        vehicule.setMarque(
                request.marque().trim()
        );

        vehicule.setModele(
                request.modele().trim()
        );

        vehicule.setAnnee(
                request.annee()
        );

        vehicule.setCapacite(
                request.capacite()
        );

        vehicule.setKilometrage(
                request.kilometrage()
        );

        return VehiculeMapper.toResponse(
                repository.save(vehicule)
        );
    }

    @Override
    public Page<VehiculeResponse> trouverTous(
            int page,
            int size,
            String sort,
            String search
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sort).ascending()
                );

        String recherche =
                search == null
                        ? ""
                        : search.trim();

        return repository
                .findByImmatriculationContainingIgnoreCase(
                        recherche,
                        pageable
                )
                .map(VehiculeMapper::toResponse);
    }

    @Override
    public VehiculeResponse trouverParId(
            Long id
    ) {

        return VehiculeMapper.toResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Véhicule introuvable"
                                )
                        )
        );
    }

    @Override
    public void supprimer(Long id) {

        Vehicule vehicule =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Véhicule introuvable"
                                )
                        );

        if (vehicule.getStatut().name()
                .equals("EN_COURSE")) {

            throw new BusinessException(
                    "Impossible de supprimer un véhicule en course"
            );
        }

        repository.delete(vehicule);
    }
}