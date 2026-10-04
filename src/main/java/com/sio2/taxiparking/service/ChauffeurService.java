package com.sio2.taxiparking.service;

import com.sio2.taxiparking.dto.request.ChauffeurRequest;
import com.sio2.taxiparking.dto.response.ChauffeurResponse;
import org.springframework.data.domain.Page;

public interface ChauffeurService {

    ChauffeurResponse ajouter(ChauffeurRequest request);

    ChauffeurResponse modifier(Long id, ChauffeurRequest request);

    Page<ChauffeurResponse> trouverTous(
            int page,
            int size,
            String sort,
            String search
    );

    ChauffeurResponse trouverParId(Long id);

    void supprimer(Long id);
}