package dette.boutique.data.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false, of = { "login", "password" })
@Entity
@ToString(exclude = { "client" })
@NamedQueries({
    @NamedQuery(name = "selectByLogin", query = "SELECT u FROM User u WHERE u.login Like :login")
})
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(length = 25, unique = true, nullable = false)
    private String login;

    @ColumnDefault(value = "true")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user")
    private Client client;

    @Transient
    private static int increment = 0;

    public User() {
    }

    // public User() {
    //     this.client = null;
    //     this.active = false;
    //     this.id += increment;
    // }

    public User(String login, String passsword, Role role) {
        this.id += increment;
        this.login = login;
        this.password = passsword;
        this.role = role;
        this.active = true;
    }

    public User(String login, String passsword) {
        this.id += increment;
        this.login = login;
        this.password = passsword;
        this.active = true;
    }


    public User(int id, boolean active, String login, String passsword, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.login = login;
        this.password = passsword;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = true;
    }

    public User(int id, boolean active, String login, String passsword, Role role, Client client, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.login = login;
        this.password = passsword;
        this.role = role;
        this.client = client;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = true;
    }

    public User(int id, String login, String password, LocalDateTime created_at, LocalDateTime updated_at, Role role) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.createdAt = created_at;
        this.updatedAt = created_at;
        this.role = role;
        this.active = true;
    }

}
