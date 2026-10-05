package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findByNomContainingIgnoreCase(
            String nom,
            Pageable pageable
    );

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}