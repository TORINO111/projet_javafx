package dette.boutique.data.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "etats")
public class Etat extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String nom;

    public Etat() {
    }

    public Etat(String nom) {
        this.nom = nom;
    }
    
    public Etat(int id, String name) {
        this.id = id;
        this.nom = name;
    }
    
    public Etat(int etatId, String nomEtat, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = etatId;
        this.nom = nomEtat;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Etat[ " + this.nom + " ]";
    }
}
