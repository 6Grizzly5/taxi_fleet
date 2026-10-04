package com.sio2.taxiparking.mapper;
import com.sio2.taxiparking.dto.response.ClientResponse;
import com.sio2.taxiparking.entity.Client;
public final class ClientMapper {
    private ClientMapper() {}
    public static ClientResponse toResponse(Client c) {
        return new ClientResponse(c.getId(), c.getNom(), c.getTelephone(), c.getEmail());
    }
}
