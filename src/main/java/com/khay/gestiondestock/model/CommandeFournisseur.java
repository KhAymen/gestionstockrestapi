package com.khay.gestiondestock.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "commandefournisseur")
public class CommandeFournisseur extends AbstractEntity{

    @Column(name = "code")
    private String code;

    @Column(name = "datecommande")
    private Instant dateCommande;

    /*
     *  "idEntreprise c'est un attribut purement technique si on parle de conception UML
     *  ce n'est 100% correct de le mettre. Mais ci on parle pour simplifier l'implementation
     *  technique cette id va simplifier beaucoup les taches.
     * */
    @Column(name = "identreprise")
    private Integer idEntreprise;


    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commandeFournisseur")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;
}
