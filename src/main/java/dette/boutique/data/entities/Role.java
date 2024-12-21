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
@Table(name = "roles")
public class Role extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String nom;

    public Role() {
    }

    public Role(String nom) {
        this.nom = nom;
    }
    
    public Role(int id, String name) {
        this.id = id;
        this.nom = name;
    }

    public Role(int id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Role[nom=" + nom + "]";
    }
}
