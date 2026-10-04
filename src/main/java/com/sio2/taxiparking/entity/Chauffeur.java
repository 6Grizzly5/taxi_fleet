package com.sio2.taxiparking.entity;

import com.sio2.taxiparking.enumeration.StatutChauffeur;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "chauffeurs")
public class Chauffeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String telephone;

    @Column(name = "numero_permis", nullable = false, unique = true)
    private String numeroPermis;

    @Column(name = "expiration_permis", nullable = false)
    private LocalDate expirationPermis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutChauffeur statut = StatutChauffeur.DISPONIBLE;

    @Column(name = "date_embauche", nullable = false)
    private LocalDate dateEmbauche;

    @OneToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getNumeroPermis() { return numeroPermis; }
    public void setNumeroPermis(String numeroPermis) { this.numeroPermis = numeroPermis; }
    public LocalDate getExpirationPermis() { return expirationPermis; }
    public void setExpirationPermis(LocalDate expirationPermis) { this.expirationPermis = expirationPermis; }
    public StatutChauffeur getStatut() { return statut; }
    public void setStatut(StatutChauffeur statut) { this.statut = statut; }
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }
}
