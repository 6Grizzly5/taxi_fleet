package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.ChauffeurRequest;
import com.sio2.taxiparking.dto.response.ChauffeurResponse;
import com.sio2.taxiparking.entity.Chauffeur;
import com.sio2.taxiparking.entity.Vehicule;
import com.sio2.taxiparking.enumeration.StatutVehicule;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.exception.ResourceNotFoundException;
import com.sio2.taxiparking.mapper.ChauffeurMapper;
import com.sio2.taxiparking.repository.ChauffeurRepository;
import com.sio2.taxiparking.repository.VehiculeRepository;
import com.sio2.taxiparking.service.ChauffeurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChauffeurServiceImpl implements ChauffeurService {

    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;

    public ChauffeurServiceImpl(
            ChauffeurRepository chauffeurRepository,
            VehiculeRepository vehiculeRepository
    ) {
        this.chauffeurRepository = chauffeurRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    @Override
    @Transactional
    public ChauffeurResponse ajouter(ChauffeurRequest request) {

        if (chauffeurRepository.existsByNumeroPermis(request.numeroPermis())) {
            throw new BusinessException("Ce numéro de permis existe déjà");
        }

        Chauffeur chauffeur = new Chauffeur();

        chauffeur.setNom(request.nom());
        chauffeur.setTelephone(request.telephone());
        chauffeur.setNumeroPermis(request.numeroPermis());
        chauffeur.setExpirationPermis(request.expirationPermis());
        chauffeur.setDateEmbauche(request.dateEmbauche());

        if (request.vehiculeId() != null) {

            Vehicule vehicule = vehiculeRepository
                    .findById(request.vehiculeId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Véhicule introuvable"
                            )
                    );

            if (vehicule.getStatut() != StatutVehicule.DISPONIBLE) {
                throw new BusinessException(
                        "Ce véhicule n'est pas disponible"
                );
            }

            chauffeur.setVehicule(vehicule);
        }

        return ChauffeurMapper.toResponse(
                chauffeurRepository.save(chauffeur)
        );
    }

    @Override
    @Transactional
    public ChauffeurResponse modifier(
            Long id,
            ChauffeurRequest request
    ) {

        Chauffeur chauffeur = chauffeurRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chauffeur introuvable"
                        )
                );

        if (!chauffeur.getNumeroPermis().equals(request.numeroPermis())
                && chauffeurRepository.existsByNumeroPermis(
                        request.numeroPermis()
                )) {

            throw new BusinessException(
                    "Ce numéro de permis existe déjà"
            );
        }

        chauffeur.setNom(request.nom());
        chauffeur.setTelephone(request.telephone());
        chauffeur.setNumeroPermis(request.numeroPermis());
        chauffeur.setExpirationPermis(request.expirationPermis());
        chauffeur.setDateEmbauche(request.dateEmbauche());

        if (request.vehiculeId() != null) {

            Vehicule vehicule = vehiculeRepository
                    .findById(request.vehiculeId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Véhicule introuvable"
                            )
                    );

            if (vehicule.getStatut() != StatutVehicule.DISPONIBLE
                    && (chauffeur.getVehicule() == null
                    || !vehicule.getId().equals(
                            chauffeur.getVehicule().getId()
                    ))) {

                throw new BusinessException(
                        "Ce véhicule n'est pas disponible"
                );
            }

            chauffeur.setVehicule(vehicule);

        } else {
            chauffeur.setVehicule(null);
        }

        return ChauffeurMapper.toResponse(
                chauffeurRepository.save(chauffeur)
        );
    }

    @Override
    public Page<ChauffeurResponse> trouverTous(
            int page,
            int size,
            String sort,
            String search
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort).ascending()
        );

        String recherche = search == null ? "" : search;

        return chauffeurRepository
                .findByNomContainingIgnoreCase(
                        recherche,
                        pageable
                )
                .map(ChauffeurMapper::toResponse);
    }

    @Override
    public ChauffeurResponse trouverParId(Long id) {

        Chauffeur chauffeur = chauffeurRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chauffeur introuvable"
                        )
                );

        return ChauffeurMapper.toResponse(chauffeur);
    }

    @Override
    @Transactional
    public void supprimer(Long id) {

        Chauffeur chauffeur = chauffeurRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chauffeur introuvable"
                        )
                );

        if (chauffeur.getVehicule() != null) {
            throw new BusinessException(
                    "Impossible de supprimer un chauffeur affecté à un véhicule"
            );
        }

        chauffeurRepository.delete(chauffeur);
    }
}