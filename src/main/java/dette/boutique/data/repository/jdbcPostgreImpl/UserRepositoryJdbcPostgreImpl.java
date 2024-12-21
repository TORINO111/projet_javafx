package dette.boutique.data.repository.jdbcPostgreImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbPostgreImpl;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;

public class UserRepositoryJdbcPostgreImpl extends RepositoryDbPostgreImpl<User> implements UserRepository {

    private static final String INSERT_QUERY = "INSERT INTO users (login, password, role_id) VALUES (?, ?, ?)";
    private static final String SELECT_QUERY = "SELECT users.id AS user_id , users.login AS user_login, users.password AS user_password, users.created_at AS user_created_at, users.updated_at AS user_updated_at, users.active AS user_active, "
            + "roles.id AS role_id, roles.nom AS role_nom, roles.created_at AS role_created_at, roles.updated_at AS role_updated_at "
            + "FROM users "
            + "LEFT JOIN roles ON users.role_id = roles.id";
    private static final String UPDATE_CLIENT_USER = "UPDATE users "
            + "SET role_id = ?, updated_at = ?"
            + "WHERE id = ?";

    @Override
    public void insert(User user) {
        try {
            this.connexion();
            String checkQuery = "SELECT id FROM users WHERE login = ?";
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
            ps.setTimestamp(2, Timestamp.valueOf(user.getUpdatedAt()));
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
            int userId = resultSet.getInt("user_id");
            String loginUser = resultSet.getString("user_login");
            String passwordUser = resultSet.getString("user_password");
            boolean active = resultSet.getBoolean("user_active");
            LocalDateTime createdAt = resultSet.getTimestamp("user_created_at").toLocalDateTime();
            LocalDateTime updatedAt = resultSet.getTimestamp("user_updated_at").toLocalDateTime();

            String roleNom = resultSet.getString("role_nom");
            int roleId = resultSet.getInt("role_id");
            LocalDateTime rcreatedAt = resultSet.getTimestamp("role_created_at").toLocalDateTime();
            LocalDateTime rupdatedAt = resultSet.getTimestamp("role_updated_at").toLocalDateTime();
            Role role = new Role(roleId, roleNom, rcreatedAt, rupdatedAt);

            User user = new User(userId, active, loginUser, passwordUser, role, createdAt, updatedAt);

            return user;
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to user object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en user", e);
        }
    }

    @Override
    public void setFields(PreparedStatement ps, User element) {
        try {
            ps.setString(1, element.getLogin());
            ps.setString(2, element.getPassword());

            if (element.getRole() != null) {
                ps.setInt(3, element.getRole().getId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to Client object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en client", e);
        }
    }

    @Override
    public void setFieldsUpdate(PreparedStatement ps, User element) {
        LocalDateTime now = LocalDateTime.now();
        try {
            if (element.getClient() != null) {
                ps.setInt(1, element.getClient().getId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setTimestamp(2, Timestamp.valueOf(now));

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
