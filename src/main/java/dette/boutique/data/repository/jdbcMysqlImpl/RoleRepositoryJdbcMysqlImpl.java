package dette.boutique.data.repository.jdbcMysqlImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbMysqlImpl;
import dette.boutique.data.entities.Role;
import dette.boutique.data.repository.RoleRepository;

public class RoleRepositoryJdbcMysqlImpl extends RepositoryDbMysqlImpl<Role> implements RoleRepository {

    public RoleRepositoryJdbcMysqlImpl() {
        this.tableName = "roles";
    }

    private final String INSERT_QUERY = "INSERT INTO roles (nom, created_at, updated_at) VALUES (?, ?, ?)";
    private final String FIND_ROLE_BY_NAME_QUERY = "SELECT * FROM roles WHERE nom = ?";
    private static final String SELECT_QUERY = "SELECT roles.id AS role_id, roles.nom AS roles_nom, roles.created_at AS role_created_at, roles.updated_at AS roles_updated_at FROM roles";

    @Override
    public void insert(Role role) {
        try {
            this.connexion();
            this.init(INSERT_QUERY);
            setFields(this.ps, role);
            this.executeUpdate();
            ResultSet rs = this.ps.getGeneratedKeys();
            if (rs.next()) {
                role.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du role : " + e.getMessage());
        } finally {
            this.deconnexion();
        }
    }

    @Override
    public List<Role> selectAll() {
        List<Role> listRoles = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(SELECT_QUERY);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listRoles.add(convertToObject(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } finally {
            deconnexion();
        }

        return listRoles;
    }

    @Override
    public void setFields(PreparedStatement pstmt, Role element)  {
        try {
            element.setCreatedAt(LocalDateTime.now());
            element.setUpdatedAt(LocalDateTime.now());
            pstmt.setInt(1, element.getId());
            pstmt.setString(2, element.getNom());
            pstmt.setTimestamp(3, Timestamp.valueOf(element.getCreatedAt()));
            pstmt.setTimestamp(4, Timestamp.valueOf(element.getUpdatedAt()));
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while setting fields for Role: " + e.getMessage());
            throw new RuntimeException("Failed to set fields for Role", e);
        }
    }

    @Override
    public String generateSql(Role element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Role convertToObject(ResultSet rs)  {
        try {
            String nom = rs.getString("role_nom");
            LocalDateTime createdAt = rs.getTimestamp("role_created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("role_updated_at").toLocalDateTime();
            int id = rs.getInt("role_id");
            return new Role(id, nom, createdAt, updatedAt);
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to role object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en role", e);
        }
    }

    @Override
    public Role selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Role element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Role findRoleByName(String nomRole) {
        Role role = null;
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(FIND_ROLE_BY_NAME_QUERY);
            ps.setString(1, nomRole);

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                role = convertToObject(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } finally {
            deconnexion();
        }

        return role;
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Role element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
