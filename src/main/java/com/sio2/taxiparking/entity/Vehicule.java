package com.sio2.taxiparking.entity;

import com.sio2.taxiparking.enumeration.StatutVehicule;
import jakarta.persistence.*;

@Entity
@Table(name = "vehicules")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false)
    private Integer annee;

    @Column(nullable = false)
    private Integer capacite;

    @Column(nullable = false)
    private Double kilometrage = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVehicule statut = StatutVehicule.DISPONIBLE;

    public Long getId() { return id; }
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }
    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }
    public Double getKilometrage() { return kilometrage; }
    public void setKilometrage(Double kilometrage) { this.kilometrage = kilometrage; }
    public StatutVehicule getStatut() { return statut; }
    public void setStatut(StatutVehicule statut) { this.statut = statut; }
}
