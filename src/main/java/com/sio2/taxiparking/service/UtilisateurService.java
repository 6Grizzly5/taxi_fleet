package com.sio2.taxiparking.service;

import com.sio2.taxiparking.dto.request.InscriptionRequest;
import com.sio2.taxiparking.entity.Utilisateur;

public interface UtilisateurService {
    Utilisateur inscrire(InscriptionRequest request);
}
