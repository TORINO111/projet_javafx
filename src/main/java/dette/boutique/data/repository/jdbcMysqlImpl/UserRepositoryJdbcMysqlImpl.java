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
import dette.boutique.data.repository.UserRepository;

public class UserRepositoryJdbcMysqlImpl extends RepositoryDbPostgreImpl<User> implements UserRepository {

    private static final String INSERT_QUERY = "INSERT INTO users (login, password, client_id, role_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_QUERY = "SELECT users.* , "
            + "roles.*, "
            + "clients.*"
            + "FROM users "
            + "LEFT JOIN clients ON users.id = clients.user_id "
            + "LEFT JOIN roles ON users.role_id = roles.id";
    private static final String UPDATE_CLIENT_USER = "UPDATE users "
            + "SET client_id = ?, updated_at = ?"
            + "WHERE users.id = ?";

    @Override
    public void insert(User user) {
        try {
            this.connexion();
            String checkQuery = "SELECT id FROM users WHERE users.login = ?";
            this.init(checkQuery);
            this.ps.setString(1, user.getLogin());
            ResultSet rs = this.ps.executeQuery();

            if (rs.next()) {
                this.init(UPDATE_CLIENT_USER);
                setFieldsUpdate(this.ps, user);
                this.executeUpdate();
            } else {
                this.init(INSERT_QUERY);
                setFields(this.ps, user);
                this.executeUpdate();
                rs = this.ps.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'utilisateur : " + e.getMessage());
        } finally {
            this.deconnexion();
        }
    }

    @Override
    public void updateClientForUser(User user) {
        try {
            connexion();
            this.init(UPDATE_CLIENT_USER);
            ps.setInt(1, user.getClient() != null ? user.getClient().getId() : null);
            ps.setTimestamp(2,  Timestamp.valueOf(user.getUpdatedAt()));
            ps.setInt(3, user.getId());
            this.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        } finally {
            this.deconnexion();
        }
    }

    @Override
    public List<User> selectAll() {
        List<User> listUsers = new ArrayList<>();
        try {
            connexion();
            this.init(SELECT_QUERY);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listUsers.add(convertToObject(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la BD : " + e.getMessage());
        } finally {
            this.deconnexion();
        }
        return listUsers;
    }

    @Override
    public User convertToObject(ResultSet resultSet) {
        try {
            int userId = resultSet.getInt("users.id");
            String loginUser = resultSet.getString("users.login");
            String passwordUser = resultSet.getString("users.password");
            boolean active = resultSet.getBoolean("users.active");
            LocalDateTime createdAt = resultSet.getTimestamp("users.created_at").toLocalDateTime();
            LocalDateTime updatedAt = resultSet.getTimestamp("users.updated_at").toLocalDateTime();

            String roleNom = resultSet.getString("roles.nom");
            int roleId= resultSet.getInt("roles.id");
            LocalDateTime rcreatedAt = resultSet.getTimestamp("roles.created_at").toLocalDateTime();
            LocalDateTime rupdatedAt = resultSet.getTimestamp("roles.updated_at").toLocalDateTime();
            Role role = new Role(roleId, roleNom, rcreatedAt, rupdatedAt);

            User user = new User(userId, active, loginUser, passwordUser, role, createdAt, updatedAt);

            
            int clientId = resultSet.getInt("clients.id");
            if (!resultSet.wasNull()) {
                String nomClient = resultSet.getString("clients.nom");
                String prenomClient = resultSet.getString("clients.prenom");
                String telephoneClient = resultSet.getString("clients.telephone");
                String adresseClient = resultSet.getString("clients.adresse");
                LocalDateTime ccreatedAt = resultSet.getTimestamp("clients.created_at").toLocalDateTime();
                LocalDateTime uupdatedAt = resultSet.getTimestamp("clients.updated_at").toLocalDateTime();
    
                Client client = new Client(clientId, nomClient, prenomClient, telephoneClient, adresseClient, user, ccreatedAt, uupdatedAt);
                user.setClient(client);
            }
            return user;
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to user object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en user", e);
        }
    }

    @Override
    public void setFields(PreparedStatement ps, User element) {
        try{
            ps.setString(1, element.getLogin());
            ps.setString(2, element.getPassword());

            if (element.getClient() != null) {
                ps.setInt(3, element.getClient().getId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setInt(4, element.getRole().getId());
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to Client object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en client", e);
        }
    }

    @Override
    public void setFieldsUpdate(PreparedStatement ps, User element) {
        LocalDateTime now = LocalDateTime.now();
        try{
            if (element.getClient() != null) {
                ps.setInt(1, element.getClient().getId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setTimestamp(2,Timestamp.valueOf(now));

            ps.setInt(3, element.getId());
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to Client object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en client", e);
        }
    }


    @Override
    public User selectById(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public User selectByLogin(String login) {
        // Méthode non implémentée
        throw new UnsupportedOperationException("Unimplemented method 'selectByLogin'");
    }

    @Override
    public String generateSql(User element) {
        // Méthode non implémentée
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public void remove(User element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
}
