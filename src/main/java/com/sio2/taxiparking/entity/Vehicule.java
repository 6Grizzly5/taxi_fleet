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

	public Long getId() {
		return id;
	}

	public String getImmatriculation() {
		return immatriculation;
	}

	public void setImmatriculation(String v) {
		immatriculation = v;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String v) {
		marque = v;
	}

	public String getModele() {
		return modele;
	}

	public void setModele(String v) {
		modele = v;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer v) {
		annee = v;
	}

	public Integer getCapacite() {
		return capacite;
	}

	public void setCapacite(Integer v) {
		capacite = v;
	}

	public Double getKilometrage() {
		return kilometrage;
	}

	public void setKilometrage(Double v) {
		kilometrage = v;
	}

	public StatutVehicule getStatut() {
		return statut;
	}

	public void setStatut(StatutVehicule v) {
		statut = v;
	}
}
