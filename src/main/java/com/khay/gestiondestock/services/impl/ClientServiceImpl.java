package com.khay.gestiondestock.services.impl;

import com.khay.gestiondestock.dto.ClientDto;
import com.khay.gestiondestock.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    @Override
    public ClientDto save(ClientDto clientDto) {
        return null;
    }

    @Override
    public ClientDto findById(Integer id) {
        return null;
    }

    @Override
    public List<ClientDto> findAll() {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
