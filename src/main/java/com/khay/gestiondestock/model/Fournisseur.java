package com.khay.gestiondestock.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "fournisseur")
public class Fournisseur extends AbstractEntity{

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    // signifie que c'est champ composé qu'on peut utiliser dans d'autres entités plusieurs fois
    @Embedded
    private Adresse adresse;

    @Column(name = "photo")
    private String photo;

    @Column(name = "mail")
    private String mail;

    @Column(name = "numTel")
    private String numTel;

    /*
     *  "idEntreprise c'est un attribut purement technique si on parle de conception UML
     *  ce n'est 100% correct de le mettre. Mais ci on parle pour simplifier l'implementation
     *  technique cette id va simplifier beaucoup les taches.
     * */

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @OneToMany(mappedBy = "fournisseur")
    private List<CommandeFournisseur> commandeFournisseurs;
}
