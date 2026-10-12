package com.sio2.taxiparking.entity;

import com.sio2.taxiparking.enumeration.StatutCourse;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private Client client;

	@ManyToOne
	private Chauffeur chauffeur;

	@ManyToOne
	private Vehicule vehicule;

	@Column(nullable = false)
	private String depart;

	@Column(nullable = false)
	private String destination;

	@Column(name = "nombre_personnes", nullable = false)
	private Integer nombrePersonnes;

	@Column(nullable = false)
	private Double distance = 0.0;

	@Column(nullable = false)
	private Double tarif;

	@Column(nullable = false)
	private LocalDateTime dateHeure;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatutCourse statut = StatutCourse.EN_ATTENTE;

	public Long getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client v) {
		client = v;
	}

	public Chauffeur getChauffeur() {
		return chauffeur;
	}

	public void setChauffeur(Chauffeur v) {
		chauffeur = v;
	}

	public Vehicule getVehicule() {
		return vehicule;
	}

	public void setVehicule(Vehicule v) {
		vehicule = v;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String v) {
		depart = v;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String v) {
		destination = v;
	}

	public Integer getNombrePersonnes() {
		return nombrePersonnes;
	}

	public void setNombrePersonnes(Integer v) {
		nombrePersonnes = v;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double v) {
		distance = v;
	}

	public Double getTarif() {
		return tarif;
	}

	public void setTarif(Double v) {
		tarif = v;
	}

	public LocalDateTime getDateHeure() {
		return dateHeure;
	}

	public void setDateHeure(LocalDateTime v) {
		dateHeure = v;
	}

	public StatutCourse getStatut() {
		return statut;
	}

	public void setStatut(StatutCourse v) {
		statut = v;
	}
}
