package dette.boutique.data.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, of = {"libelle"})
@Entity
@Table(name = "articles")
public class Article extends AbstractEntity {

    @Column(length = 55, unique = true, nullable = false)
    private String libelle;

    @Column(nullable = false)
    private int qteStock;

    @Column(nullable = false)
    private int prixUnitaire;

    @OneToMany(mappedBy = "article", cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = false)
    private List<Details> details;

    @Transient
    private static int increment = 1;

    // public Article() {
    // this.id += increment;
    // this.ref = String.format("A%06d", id);
    // }

    public Article() {
    }

    public Article(String lib, int prixUnitaire, int stock) {
        this.libelle = lib;
        this.prixUnitaire = prixUnitaire;
        this.qteStock = stock;
        this.details = new ArrayList<>();
    }

    public Article(int id,String lib, int price, int stock) {
        this.id = id;
        this.libelle = lib;
        this.prixUnitaire = price;
        this.qteStock = stock;
        this.details = new ArrayList<>();
    }

    public Article(int id, String lib, int price, int stock, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.libelle = lib;
        this.prixUnitaire = price;
        this.qteStock = stock;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
        this.details = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Article[Libelle= " + libelle + "; Prix unitaire=" + prixUnitaire + "; stock= " + qteStock + "]";
    }
}
