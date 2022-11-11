package com.khay.gestiondestock.controller.api;

import com.khay.gestiondestock.dto.VentesDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.khay.gestiondestock.utils.Constants.VENTES_ENDPOINT;

@Api(VENTES_ENDPOINT)
public interface VentesApi {

    @PostMapping(VENTES_ENDPOINT + "/create")
    VentesDto save(@RequestBody VentesDto ventesDto);

    @GetMapping(VENTES_ENDPOINT + "/{idVente}")
    VentesDto findById(@PathVariable("idVente") Integer id);

    @GetMapping(VENTES_ENDPOINT + "/all")
    List<VentesDto> findAll();

    @GetMapping(VENTES_ENDPOINT + "/{codeVente}")
    VentesDto findByCode(@PathVariable("codeVente")String code);

    @DeleteMapping(VENTES_ENDPOINT + "/delete/{idVente}")
    void delete(@PathVariable("idVente") Integer id);
}
