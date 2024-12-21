package dette.boutique.data.repository.jdbcMysqlImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbPostgreImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.ClientRepository;

public class ClientRepositoryJdbcMysqlImpl extends RepositoryDbPostgreImpl<Client> implements ClientRepository {

    private final String INSERT_QUERY = "INSERT INTO clients (nom, prenom, telephone, adresse, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_QUERY = "SELECT clients.*, "
            + "users.*, roles.* "
            + "FROM clients "
            + "LEFT JOIN users ON clients.user_id = users.id "
            + "LEFT JOIN roles ON users.role_id = roles.id";

    private static final String SELECT_CLIENT_QUERY = "SELECT clients.*, "
            + "users.*, roles.* "
            + "FROM clients "
            + "LEFT JOIN users ON clients.user_id = users.id "
            + "LEFT JOIN roles ON users.role_id = roles.id"
            + "WHERE clients.telephone = ?";

    private static final String UPDATE_CLIENT = "UPDATE clients "
            + "SET clients.user_id = ?, clients.updated_at= ?"
            + "WHERE id = ?";

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
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(SELECT_QUERY);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listClients.add(convertToObject(resultSet));
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
            int clientId = resultSet.getInt("clients.id");
            String nomClient = resultSet.getString("clients.nom");
            String prenomClient = resultSet.getString("clients.prenom");
            String telephone = resultSet.getString("clients.telephone");
            String adresse = resultSet.getString("clients.adresse");
            LocalDateTime createdAt = resultSet.getTimestamp("clients.created_at").toLocalDateTime();
            LocalDateTime updatedAt = resultSet.getTimestamp("clients.updated_at").toLocalDateTime();

            User user = null;
            if (resultSet.getInt("users.id") > 0) {
                String roleNom = resultSet.getString("roles.nom");
                int roleId= resultSet.getInt("role_id");
                LocalDateTime roleCreatedAt = resultSet.getTimestamp("roles.created_at").toLocalDateTime();
                LocalDateTime roleUpdatedAt = resultSet.getTimestamp("roles.updated_at").toLocalDateTime();

                Role role = new Role(roleId, roleNom, roleCreatedAt, roleUpdatedAt);

                user = new User(resultSet.getInt("users.id"),
                        resultSet.getString("users.login"),
                        resultSet.getString("users.password"),
                        resultSet.getTimestamp("users.created_at").toLocalDateTime(),
                        resultSet.getTimestamp("users.updated_at").toLocalDateTime(),
                        role);

            }

            return new Client(clientId, nomClient, prenomClient, telephone, adresse, user, createdAt, updatedAt);
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to Client object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en client", e);
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
