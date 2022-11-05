package com.khay.gestiondestock.controller.api;

import com.khay.gestiondestock.dto.UtilisateurDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.khay.gestiondestock.utils.Constants.UTILISATEUR_ENDPOINT;
import static com.khay.gestiondestock.utils.Constants.ENTREPRISE_ENDPOINT;

@Api(UTILISATEUR_ENDPOINT)
public interface UtilisateurApi {

    @PostMapping(UTILISATEUR_ENDPOINT + "/create")
    UtilisateurDto save(@RequestBody UtilisateurDto dto);

    @GetMapping(UTILISATEUR_ENDPOINT + "/{idUtilisateur}")
    UtilisateurDto findById(@PathVariable("idUtilisateur") Integer id);


    @GetMapping(UTILISATEUR_ENDPOINT +"/all")
    List<UtilisateurDto> findAll();

    @DeleteMapping(ENTREPRISE_ENDPOINT + "/{idUtilisateur}/delete")
    void delete(@PathVariable("idUtilisateur") Integer id);
}
