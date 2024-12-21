package dette.boutique.data.repository.jdbcPostgreImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbPostgreImpl;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.EtatRepository;

public class EtatRepositoryJdbcPostgreImpl extends RepositoryDbPostgreImpl<Etat> implements EtatRepository {

    private static final String SELECT_QUERY = "SELECT etats.id AS etat_id, etats.nom AS etat_nom, etats.created_at AS etat_created_at, etats.updated_at AS etat_updated_at FROM etats";

    @Override
    public void insert(Etat element) {
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public List<Etat> selectAll() {
        List<Etat> listEtats = new ArrayList<>();
        try {
            connexion();
            this.init(SELECT_QUERY);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listEtats.add(convertToObject(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } finally {
            deconnexion();
        }

        return listEtats;
    }

    @Override
    public void setFields(PreparedStatement pstmt, Etat element)  {
        throw new UnsupportedOperationException("Unimplemented method 'setFields'");
    }

    @Override
    public String generateSql(Etat element) {
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Etat selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Etat element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    public Etat convertToObject(ResultSet rs)  {
        try {
            String nom = rs.getString("etat_nom");
            LocalDateTime createdAt = rs.getTimestamp("etat_created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("etat_updated_at").toLocalDateTime();
            int id = rs.getInt("etat_id");
            return new Etat(id, nom, createdAt, updatedAt);
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to role object: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Echec de conversion du ResultSet en role", e);
        }
    }


}
