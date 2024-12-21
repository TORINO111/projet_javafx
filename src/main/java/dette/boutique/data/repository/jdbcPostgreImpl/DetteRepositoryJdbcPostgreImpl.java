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
import dette.boutique.data.repository.DetteRepository;

public class DetteRepositoryJdbcPostgreImpl extends RepositoryDbPostgreImpl<Dette> implements DetteRepository {

    private static final String INSERTQUERY = "INSERT INTO dettes (montant, montant_verse, client_id, etat_id) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_DETTE = "UPDATE dettes "
            + "SET montant_verse = ?, updated_at = ?, etat_id = ?, isArchive = ? "
            + "WHERE dettes.id = ?";

    private static final String SELECT_QUERY = "SELECT " +
            "    dettes.id AS dette_id, " +
            "    dettes.created_at AS dette_created_at, " +
            "    dettes.updated_at AS dette_updated_at, " +
            "    dettes.isarchive AS dette_is_archive, " +
            "    dettes.montant AS dette_montant, " +
            "    dettes.montant_verse AS dette_montant_verse, " +
            "    dettes.client_id AS dette_client_id, " +
            "    dettes.etat_id AS dette_etat_id, " +
            "    clients.id AS client_id, " +
            "    clients.created_at AS client_created_at, " +
            "    clients.updated_at AS client_updated_at, " +
            "    clients.nom AS client_nom, " +
            "    clients.prenom AS client_prenom, " +
            "    clients.adresse AS client_adresse, " +
            "    clients.telephone AS client_telephone, " +
            "    etats.id AS etat_id, " +
            "    etats.created_at AS etat_created_at, " +
            "    etats.updated_at AS etat_updated_at, " +
            "    etats.nom AS etat_nom " +
            "FROM " +
            "    dettes " +
            "LEFT JOIN " +
            "    clients ON dettes.client_id = clients.id " +
            "LEFT JOIN " +
            "    etats ON dettes.etat_id = etats.id;";

    @Override
    public void insert(Dette element) {
        try {
            this.connexion();
            String checkQuery = "SELECT id FROM dettes WHERE client_id = ? AND montant = ? AND montant_verse = ? ";
            this.init(checkQuery);
            if (element.getClient() != null) {
                ps.setInt(1, element.getClient().getId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            this.ps.setInt(2, element.getMontant());
            this.ps.setInt(3, element.getMontantVerse());
            ResultSet rs = this.ps.executeQuery();

            if (rs.next()) {
                this.init(UPDATE_DETTE);
                setFieldsUpdate(this.ps, element);
                this.executeUpdate();
            } else {
                    this.init(INSERTQUERY);
                setFields(ps, element);
                this.executeUpdate();

                rs = this.ps.getGeneratedKeys();
                if (rs.next()) {
                    element.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client : " + e.getMessage());
            e.printStackTrace();
        } finally {
            this.deconnexion();
        }
    }

    @Override
    public List<Dette> selectAll() {
        List<Dette> listDettes = new ArrayList<>();
        try {
            connexion();
            this.init(SELECT_QUERY);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listDettes.add(convertToObject(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } finally {
            deconnexion();
        }

        return listDettes;
    }

    @Override
    public void setFields(PreparedStatement pstmt, Dette element) {
        try {
            pstmt.setInt(1, element.getMontant());
            if (element.getMontantVerse() != 0) {
                pstmt.setInt(2, element.getMontantVerse());
            } else {
                pstmt.setInt(2, 0);
            }
            if (element.getClient() != null) {
                pstmt.setInt(3, element.getClient().getId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            if (element.getEtat() != null) {
                pstmt.setInt(4, element.getEtat().getId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Dette element) {
        element.setUpdatedAt(LocalDateTime.now());
        try {
            pstmt.setInt(1, element.getMontantVerse());
            pstmt.setTimestamp(2, Timestamp.valueOf(element.getUpdatedAt()));
            pstmt.setInt(3, element.getEtat().getId());
            pstmt.setBoolean(4, element.isIsarchive());
            pstmt.setInt(5, element.getId());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la configuration des paramètres : " + e.getMessage());
            throw new RuntimeException("Échec de configuration des paramètres", e);
        }
    }

    @Override
    public String generateSql(Dette element) {
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Dette selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Dette element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Dette convertToObject(ResultSet rs) {
        try {
            int id = rs.getInt("dette_id");
            int montant = rs.getInt("dette_montant");
            int montantVerse = rs.getInt("dette_montant_verse");
            boolean isArchive = rs.getBoolean("dette_is_archive");
            LocalDateTime createdAt = rs.getTimestamp("dette_created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("dette_updated_at").toLocalDateTime();

            Client client = null;
            int clientId = rs.getInt("client_id");
            if (!rs.wasNull()) {
                String nomClient = rs.getString("client_nom");
                String prenomClient = rs.getString("client_prenom");
                String telephone = rs.getString("client_telephone");
                String adresse = rs.getString("client_adresse");
                LocalDateTime ccreatedAt = rs.getTimestamp("client_created_at").toLocalDateTime();
                LocalDateTime cupdatedAt = rs.getTimestamp("client_updated_at").toLocalDateTime();
                client = new Client(clientId, nomClient, prenomClient, telephone, adresse, ccreatedAt, cupdatedAt);
            }

            Etat etat = null;
            int etatId = rs.getInt("etat_id");
            if (!rs.wasNull()) {
                String nomEtat = rs.getString("etat_nom");
                LocalDateTime ecreatedAt = rs.getTimestamp("etat_created_at").toLocalDateTime();
                LocalDateTime eupdatedAt = rs.getTimestamp("etat_updated_at").toLocalDateTime();
                etat = new Etat(etatId, nomEtat, ecreatedAt, eupdatedAt);
            }
            return new Dette(id, montant, montantVerse, isArchive, createdAt, updatedAt, client, etat);

        } catch (SQLException e) {
            System.err.println("Erreur lors de la conversion du résultat en objet Details : " + e.getMessage());
            throw new RuntimeException("Conversion échouée", e);
        }
    }

}
