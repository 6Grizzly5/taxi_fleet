package com.sio2.taxiparking.service;

import com.sio2.taxiparking.dto.request.ChauffeurRequest;
import com.sio2.taxiparking.dto.response.ChauffeurResponse;
import org.springframework.data.domain.Page;

public interface ChauffeurService {
    ChauffeurResponse ajouter(ChauffeurRequest request);
    Page<ChauffeurResponse> trouverTous(int page, int size, String sort, String search);
    ChauffeurResponse trouverParId(Long id);
    ChauffeurResponse valider(Long id);
    ChauffeurResponse suspendre(Long id);
}
