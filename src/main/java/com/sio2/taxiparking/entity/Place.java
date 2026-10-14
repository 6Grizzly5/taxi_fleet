package com.sio2.taxiparking.entity;

import com.sio2.taxiparking.enumeration.*;
import jakarta.persistence.*;

@Entity
@Table(name = "places")
public class Place {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String numero;

	@Column(nullable = false)
	private String zone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TypePlace type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatutPlace statut = StatutPlace.LIBRE;

	public Long getId() {
		return id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String v) {
		numero = v;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String v) {
		zone = v;
	}

	public TypePlace getType() {
		return type;
	}

	public void setType(TypePlace v) {
		type = v;
	}

	public StatutPlace getStatut() {
		return statut;
	}

	public void setStatut(StatutPlace v) {
		statut = v;
	}
}
