package dette.boutique.data.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, of = { "client", "montantVerse" })
@Entity
@Table(name = "dettes")
public class Dette extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "dette", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH }, orphanRemoval = true)
    private List<Details> details = new ArrayList<>();

    @Column(name = "montant_verse", nullable = true)
    private int montantVerse;

    @Column(nullable = false)
    private int montant;

    @ManyToOne
    @JoinColumn(name = "etat_id", nullable=false)
    private Etat etat;

    @Transient
    private int montantRestant;

    @Transient
    private static int newDette = 0;

    @Column(name = "isArchive", nullable = false)
    private boolean isarchive = false;

    // public Dette() {
    // this.id = ++newDette;
    // }

    public Dette() {
    }

    public Dette(int montant, Client client, List<Details> details, Etat etat) {
        this.montant = montant;
        this.client = client;
        this.details = details;
        this.etat = etat;
    }

    public Dette(int montant, Client client, Etat etat) {
        this.montant = montant;
        this.client = client;
        this.etat = etat;
    }

    public Dette(int montant, int montantVerse, Client client, List<Details> details, Etat etat) {
        this.montant = montant;
        this.montantVerse = montantVerse;
        this.montantRestant = montant - montantVerse;
        this.client = client;
        this.details = details;
        this.etat = etat;
    }

    public Dette(int id, int montant2, int montantVerse2, boolean isArchive2, Etat etat, LocalDateTime dcreatedAt, LocalDateTime dupdatedAt) {
        this.id = id;
        this.montant = montant2;
        this.montantVerse = montantVerse2;
        this.montantRestant = montant2 - montantVerse2;
        this.isarchive = isArchive2;
        this.createdAt = dcreatedAt;
        this.updatedAt = dupdatedAt;
        this.etat = etat;
    }

    public Dette(int id, int montant2, int montantVerse2, boolean isArchive2, LocalDateTime createdAt,
            LocalDateTime updatedAt, Client client2, Etat etat2) {
                this.id = id;
                this.montant = montant2;
                this.montantVerse = montantVerse2;
                this.montantRestant = montant2 - montantVerse2;
                this.isarchive = isArchive2;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
                this.etat = etat2;
                this.client = client2;
    }

    @Override
    public String toString() {
        return "Client: " + client.getNom() + " " + client.getPrenom() + "; montant:" + montant + "; montant versé:"
                + montantVerse + "; montant restant: " + (montantRestant = montant - montantVerse)
                + "; etat de la dette: " + etat.getNom();
    }
}
