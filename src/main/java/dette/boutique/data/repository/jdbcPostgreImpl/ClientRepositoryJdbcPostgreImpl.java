package dette.boutique.data.repository.jdbcPostgreImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbPostgreImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.ClientRepository;

public class ClientRepositoryJdbcPostgreImpl extends RepositoryDbPostgreImpl<Client> implements ClientRepository {

    private final String INSERT_QUERY = "INSERT INTO clients (nom, prenom, telephone, adresse, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_QUERY = "SELECT " +
            "    clients.id AS client_id, " +
            "    clients.created_at AS client_created_at, " +
            "    clients.updated_at AS client_updated_at, " +
            "    clients.adresse AS client_adresse, " +
            "    clients.nom AS client_nom, " +
            "    clients.telephone AS client_telephone, " +
            "    clients.prenom AS client_prenom, " +
            "    clients.user_id AS client_user_id, " +
            "    users.id AS user_id, " +
            "    users.created_at AS user_created_at, " +
            "    users.updated_at AS user_updated_at, " +
            "    users.login AS user_login, " +
            "    users.password AS user_password, " +
            "    users.active AS user_active, " +
            "    users.role_id AS user_role_id, " +
            "    roles.id AS role_id, " +
            "    roles.created_at AS role_created_at, " +
            "    roles.updated_at AS role_updated_at, " +
            "    roles.nom AS role_nom " +
            "FROM " +
            "    clients " +
            "LEFT JOIN " +
            "    users ON clients.user_id = users.id " +
            "LEFT JOIN " +
            "    roles ON users.role_id = roles.id " ;

    private static final String SELECT_CLIENT_QUERY = "SELECT " +
            "    clients.id AS client_id, " +
            "    clients.created_at AS client_created_at, " +
            "    clients.updated_at AS client_updated_at, " +
            "    clients.adresse AS client_adresse, " +
            "    clients.nom AS client_nom, " +
            "    clients.telephone AS client_telephone, " +
            "    clients.prenom AS client_prenom, " +
            "    clients.user_id AS client_user_id, " +
            "    dettes.id AS dette_id, " +
            "    dettes.created_at AS dette_created_at, " +
            "    dettes.updated_at AS dette_updated_at, " +
            "    dettes.isarchive AS dette_is_archive, " +
            "    dettes.montant AS dette_montant, " +
            "    dettes.montant_verse AS dette_montant_verse, " +
            "    dettes.client_id AS dette_client_id, " +
            "    dettes.etat_id AS dette_etat_id, " +
            "    etats.id AS etat_id, " +
            "    etats.created_at AS etat_created_at, " +
            "    etats.updated_at AS etat_updated_at, " +
            "    etats.nom AS etat_nom, " +
            "    users.id AS user_id, " +
            "    users.created_at AS user_created_at, " +
            "    users.updated_at AS user_updated_at, " +
            "    users.login AS user_login, " +
            "    users.password AS user_password, " +
            "    users.active AS user_active, " +
            "    users.role_id AS user_role_id, " +
            "    roles.id AS role_id, " +
            "    roles.created_at AS role_created_at, " +
            "    roles.updated_at AS role_updated_at, " +
            "    roles.nom AS role_nom " +
            "FROM " +
            "    clients " +
            "LEFT JOIN " +
            "    dettes ON clients.id = dettes.client_id " +
            "LEFT JOIN " +
            "    users ON clients.user_id = users.id " +
            "LEFT JOIN " +
            "    roles ON users.role_id = roles.id " +
            "LEFT JOIN " +
            "    etats ON dettes.etat_id = etats.id" +
            "WHERE clients.telephone = ?";

    private static final String UPDATE_CLIENT = "UPDATE clients SET user_id = ?, updated_at= ? WHERE id = ?";

    @Override
    public void insert(Client client) {
        try {
            this.connexion();
            String checkQuery = "SELECT id FROM clients WHERE clients.telephone = ?";
            this.init(checkQuery);
            this.ps.setString(1, client.getTelephone());
            ResultSet rs = this.ps.executeQuery();

            if (rs.next()) {
                this.init(UPDATE_CLIENT);
                setFieldsUpdate(this.ps, client);
                this.executeUpdate();
            } else {
                this.init(INSERT_QUERY);
                setFields(this.ps, client);
                this.executeUpdate();
                rs = this.ps.getGeneratedKeys();
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client : " + e.getMessage());
        } finally {
            this.deconnexion();
        }
    }

    @Override
    public List<Client> selectAll() {
        List<Client> listClients = new ArrayList<>();
        try {
            connexion();
            this.init(SELECT_QUERY);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Client client = convertToObject(resultSet);
                listClients.add(client);
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } finally {
            deconnexion();
        }

        return listClients;
    }

    @Override
    public Client findByTel(String telephone) {
        Client client = null;
        try {
            connexion();
            this.init(SELECT_CLIENT_QUERY);
            ps.setString(1, telephone);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                client = convertToObject(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
        } finally {
            deconnexion();
        }
        return client;
    }

    @Override
    public String generateSql(Client element) {
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public void setFields(PreparedStatement pstmt, Client client) {
        try {
            client.setCreatedAt(LocalDateTime.now());
            client.setUpdatedAt(LocalDateTime.now());
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getTelephone());
            pstmt.setString(4, client.getAdresse());
            if (client.getUser() != null) {
                pstmt.setInt(5, client.getUser().getId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            pstmt.setTimestamp(6, Timestamp.valueOf(client.getCreatedAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(client.getUpdatedAt()));
        } catch (SQLException e) {
            // Log the error with details of the issue
            System.err.println("SQL Exception occurred while setting fields for Client: " + e.getMessage());
            e.printStackTrace(); // Or use a logger to handle the exception logging
            throw new RuntimeException("Failed to set fields for Client", e); // Optionally re-throw or wrap it
        }
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Client client) {
        try {
            client.setUpdatedAt(LocalDateTime.now());
            if (client.getUser() != null) {
                pstmt.setInt(1, client.getUser().getId());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setTimestamp(2, Timestamp.valueOf(client.getUpdatedAt()));
            pstmt.setInt(3, client.getId());
        } catch (SQLException e) {
            // Log the error with details of the issue
            System.err.println("SQL Exception occurred while setting fields for Client: " + e.getMessage());
            e.printStackTrace(); // Or use a logger to handle the exception logging
            throw new RuntimeException("Failed to set fields for Client", e); // Optionally re-throw or wrap it
        }
    }

    @Override
    public Client convertToObject(ResultSet resultSet) {
        try {
            Client client = null;
            int clientId = resultSet.getInt("client_id");
            if (clientId > 0) {
                String nomClient = resultSet.getString("client_nom");
                String prenomClient = resultSet.getString("client_prenom");
                String telephone = resultSet.getString("client_telephone");
                String adresse = resultSet.getString("client_adresse");
                LocalDateTime createdAt = resultSet.getTimestamp("client_created_at").toLocalDateTime();
                LocalDateTime updatedAt = resultSet.getTimestamp("client_updated_at").toLocalDateTime();

                User user = null;
                if (resultSet.getInt("user_id") > 0) {
                    String roleNom = resultSet.getString("role_nom");
                    int roleId = resultSet.getInt("role_id");
                    LocalDateTime roleCreatedAt = resultSet.getTimestamp("role_created_at").toLocalDateTime();
                    LocalDateTime roleUpdatedAt = resultSet.getTimestamp("role_updated_at").toLocalDateTime();

                    Role role = new Role(roleId, roleNom, roleCreatedAt, roleUpdatedAt);

                    user = new User(resultSet.getInt("user_id"),
                            resultSet.getBoolean("user_active"),
                            resultSet.getString("user_login"),
                            resultSet.getString("user_password"),
                            role,
                            resultSet.getTimestamp("user_created_at").toLocalDateTime(),
                            resultSet.getTimestamp("user_updated_at").toLocalDateTime());
                }
                client = new Client(clientId, nomClient, prenomClient, telephone, adresse, user, createdAt,
                        updatedAt);
                if (client.getDettes() == null) {
                    client.setDettes(new ArrayList<>());
                }
                // Ajouter une dette au client
                // int detteId = resultSet.getInt("dette_id");
                // if (detteId > 0) {
                //     String nom = resultSet.getString("etat_nom");
                //     LocalDateTime ecreatedAt = resultSet.getTimestamp("etat_created_at").toLocalDateTime();
                //     LocalDateTime eupdatedAt = resultSet.getTimestamp("etat_updated_at").toLocalDateTime();
                //     int id = resultSet.getInt("etat_id");
                //     Etat etat = new Etat(id, nom, ecreatedAt, eupdatedAt);

                //     LocalDateTime detteCreatedAt = resultSet.getTimestamp("dette_created_at").toLocalDateTime();
                //     LocalDateTime detteUpdatedAt = resultSet.getTimestamp("dette_updated_at").toLocalDateTime();
                //     boolean isArchive = resultSet.getBoolean("dette_is_archive");
                //     int montant = resultSet.getInt("dette_montant");
                //     int montantVerse = resultSet.getInt("dette_montant_verse");

                //     Dette dette = new Dette(detteId, montant, montantVerse, isArchive, etat, detteCreatedAt,
                //             detteUpdatedAt);
                //     client.getDettes().add(dette);
                // }
                return client;
            }
            return null;
        } catch (SQLException e) {
            System.err
                    .println("SQL Exception occurred while converting ResultSet to Client objects: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en clients", e);
        }
    }

    @Override
    public Client selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Client element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
}
