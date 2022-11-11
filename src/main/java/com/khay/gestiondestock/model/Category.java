package com.khay.gestiondestock.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
public class Category extends AbstractEntity{

    @Column(name = "code")
    private String code;

    @Column(name = "designation")
    private String designation;

    /*
     *  "idEntreprise c'est un attribut purement technique si on parle de conception UML
     *  ce n'est 100% correct de le mettre. Mais ci on parle pour simplifier l'implementation
     *  technique cette id va simplifier beaucoup les taches.
     * */
    @Column(name = "identreprise")
    private Integer idEntreprise;

    // Une categorie a plusieurs articles
    @OneToMany(mappedBy = "category")
    private List<Article> atricles;
}
