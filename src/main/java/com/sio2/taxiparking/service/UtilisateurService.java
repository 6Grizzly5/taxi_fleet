package com.sio2.taxiparking.service;

import com.sio2.taxiparking.dto.request.*;
import com.sio2.taxiparking.entity.Utilisateur;

public interface UtilisateurService {

	Utilisateur inscrireClient(InscriptionRequest r);

	Utilisateur inscrireChauffeur(ChauffeurInscriptionRequest r);
}
