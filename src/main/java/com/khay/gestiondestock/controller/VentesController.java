package com.khay.gestiondestock.controller;

import com.khay.gestiondestock.controller.api.VentesApi;
import com.khay.gestiondestock.dto.VentesDto;
import com.khay.gestiondestock.services.VentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VentesController implements VentesApi {

    private VentesService ventesService;

    @Autowired
    public VentesController(VentesService ventesService) {
        this.ventesService = ventesService;
    }

    @Override
    public VentesDto save(VentesDto ventesDto) {
        return ventesService.save(ventesDto);
    }

    @Override
    public VentesDto findById(Integer id) {
        return ventesService.findById(id);
    }

    @Override
    public List<VentesDto> findAll() {
        return ventesService.findAll();
    }

    @Override
    public VentesDto findByCode(String code) {
        return ventesService.findByCode(code);
    }

    @Override
    public void delete(Integer id) {
        ventesService.delete(id);
    }
}
