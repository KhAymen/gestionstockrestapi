package com.khay.gestiondestock.utils;

public interface Constants {

    String APP_ROOT = "/gestiondestock/v1";
    String COMMANDE_FOURNISSUER_ENDPOINT = APP_ROOT + "/commandesfournisseurs";
    String CREATE_COMMANDE_FOURNISSUER_ENDPOINT = COMMANDE_FOURNISSUER_ENDPOINT + "/create";
    String FIND_COMMANDE_FOURNISSUER_BY_ID_ENDPOINT = COMMANDE_FOURNISSUER_ENDPOINT + "/{idCommandeFournisseur}";
    String FIND_COMMANDE_FOURNISSUER_BY_CODE = COMMANDE_FOURNISSUER_ENDPOINT + "/{codeCommandeFournisseur}";
    String FIND_ALL_COMMANDE_FOURNISSUER = COMMANDE_FOURNISSUER_ENDPOINT + "/all";
    String DELETE_COMMANDE_FOURNISSUER_ENDPOINT = COMMANDE_FOURNISSUER_ENDPOINT + "/delete/{idCommandeFournisseur}";

    String FOURNISSEUR_ENDPOINT = APP_ROOT + "/fournisseurs";

    String ENTREPRISE_ENDPOINT = APP_ROOT + "/entreprises";

    String UTILISATEUR_ENDPOINT = APP_ROOT + "/utilisateurs";

    String VENTES_ENDPOINT = APP_ROOT + "/ventes";

    String AUTHENTICATION_ENDPOINT = APP_ROOT + "/auth";
}
