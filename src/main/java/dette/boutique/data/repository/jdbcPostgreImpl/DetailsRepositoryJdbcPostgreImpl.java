package dette.boutique.data.repository.jdbcPostgreImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbPostgreImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.DetailsRepository;

public class DetailsRepositoryJdbcPostgreImpl extends RepositoryDbPostgreImpl<Details> implements DetailsRepository {

    private static final String INSERTQUERY = "INSERT INTO details (quantite, prixTotal, article_id, dette_id) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_DETAILS = "UPDATE details "
            + "SET dette_id = ?, updated_at= ? "
            + "WHERE details.id = ?";

            private static final String SELECT_QUERY = "SELECT " +
            "    details.id AS detail_id, " +
            "    details.created_at AS detail_created_at, " +
            "    details.updated_at AS detail_updated_at, " +
            "    details.prixTotal AS detail_prix_total, " +
            "    details.quantite AS detail_quantite, " +
            "    details.article_id AS detail_article_id, " +
            "    details.dette_id AS detail_dette_id, " +
            "    etats.id AS etat_id, " +
            "    etats.nom AS etat_nom, " +
            "    etats.created_at AS etat_created_at, " +
            "    etats.updated_at AS etat_updated_at, " +
            "    articles.id AS article_id, " +
            "    articles.created_at AS article_created_at, " +
            "    articles.updated_at AS article_updated_at, " +
            "    articles.libelle AS article_libelle, " +
            "    articles.prixUnitaire AS article_prix_unitaire, " +
            "    articles.qteStock AS article_qte_stock, " +
            "    dettes.id AS dette_id, " +
            "    dettes.created_at AS dette_created_at, " +
            "    dettes.updated_at AS dette_updated_at, " +
            "    dettes.isarchive AS dette_is_archive, " +
            "    dettes.montant AS dette_montant, " +
            "    dettes.montant_verse AS dette_montant_verse, " +
            "    dettes.client_id AS dette_client_id, " +
            "    dettes.etat_id AS dette_etat_id " +
            "FROM " +
            "    details " +
            "LEFT JOIN " +
            "    articles ON details.article_id = articles.id " +
            "LEFT JOIN " +
            "    dettes ON details.dette_id = dettes.id " +
            "LEFT JOIN " +
            "    etats ON dettes.etat_id = etats.id";
    
    @Override
    public void insert(Details element) {

        try {
            this.connexion();
            String checkQuery = "SELECT id FROM details WHERE id = ?";
            this.init(checkQuery);
            this.ps.setInt(1, element.getId());
            ResultSet rs = this.ps.executeQuery();

            if (rs.next()) {
                this.init(UPDATE_DETAILS);
                setFieldsUpdate(this.ps, element);
                this.executeUpdate();
            } else {
                connexion();
                this.init(INSERTQUERY);
                setFields(ps, element);
                ps.executeUpdate();

                rs = this.ps.getGeneratedKeys();
                if (rs.next()) {
                    element.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client : " + e.getMessage());
        } finally {
            this.deconnexion();
        }
    }

    @Override
    public List<Details> selectAll() {

        List<Details> detailsList = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(SELECT_QUERY);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                detailsList.add(convertToObject(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des détails : " + e.getMessage());
            throw new RuntimeException("Récupération des détails échouée", e);
        } finally {
            deconnexion();
        }
        return detailsList;
    }

    @Override
    public void setFields(PreparedStatement pstmt, Details element) {
        try {
            pstmt.setInt(1, element.getQuantite());
            pstmt.setInt(2, element.getPrixTotal());
            pstmt.setInt(3, element.getArticle().getId());
            if (element.getDette() != null) {
                pstmt.setInt(4, element.getDette().getId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la configuration des paramètres : " + e.getMessage());
            throw new RuntimeException("Échec de configuration des paramètres", e);
        }
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Details element) {
        try {
            pstmt.setInt(1, element.getArticle().getId());
            pstmt.setTimestamp(2, Timestamp.valueOf(element.getUpdatedAt()));
            pstmt.setInt(3, element.getId());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la configuration des paramètres : " + e.getMessage());
            throw new RuntimeException("Échec de configuration des paramètres", e);
        }
    }


    @Override
    public String generateSql(Details element) {
        return "INSERT INTO details (quantite, prixTotal, article_id, dette_id) VALUES ("
                + element.getQuantite() + ", "
                + element.getPrixTotal() + ", "
                + element.getArticle().getId() + ", "
                + (element.getDette() != null ? element.getDette().getId() : "NULL") + ")";
    }

    @Override
    public Details selectById(int id) {
        throw new RuntimeException("Suppression du détail échouée");
    }

    @Override
    public void remove(Details element) {
        String deleteQuery = "DELETE FROM details WHERE id = ?";
        try {
            connexion();
            this.init(deleteQuery);
            ps.setInt(1, element.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(
                    "Erreur lors de la suppression du détail avec ID " + element.getId() + " : " + e.getMessage());
            throw new RuntimeException("Suppression du détail échouée", e);
        } finally {
            deconnexion();
        }
    }

    @Override
    public Details convertToObject(ResultSet rs) {
        try {
            int id = rs.getInt("detail_id");
            int quantite = rs.getInt("detail_quantite");
            int prixTotal = rs.getInt("detail_prix_total");
            LocalDateTime createdAt = rs.getTimestamp("detail_created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("detail_updated_at").toLocalDateTime();

            Article article = null;
            int articleId = rs.getInt("article_id");
            if (!rs.wasNull()) {
                String libelle = rs.getString("article_libelle");
                int qteStock = rs.getInt("article_qte_stock");
                int prixUnitaire = rs.getInt("article_prix_unitaire");
                LocalDateTime acreatedAt = rs.getTimestamp("article_created_at").toLocalDateTime();
                LocalDateTime aupdatedAt = rs.getTimestamp("article_updated_at").toLocalDateTime();
                article = new Article(articleId, libelle, prixUnitaire, qteStock, acreatedAt, aupdatedAt);
            }

            Etat etat = null;
            int etatId =rs.getInt("etat_id");
            if (!rs.wasNull()) {
                String nom = rs.getString("etat_nom");
                LocalDateTime acreatedAt = rs.getTimestamp("etat_created_at").toLocalDateTime();
                LocalDateTime aupdatedAt = rs.getTimestamp("etat_updated_at").toLocalDateTime();
                etat = new Etat(etatId, nom, acreatedAt, aupdatedAt);
            }

            Dette dette = null;
            int detteId = rs.getInt("dette_id");
            if (!rs.wasNull()) {
                int montant = rs.getInt("dette_montant");
                int montantVerse = rs.getInt("dette_montant_verse");
                boolean isArchive = rs.getBoolean("dette_is_archive");
                LocalDateTime dcreatedAt = rs.getTimestamp("dette_created_at").toLocalDateTime();
                LocalDateTime dupdatedAt = rs.getTimestamp("dette_updated_at").toLocalDateTime();
                dette = new Dette(detteId, montant, montantVerse, isArchive, etat, dcreatedAt, dupdatedAt);

            }
            return new Details(id, quantite, prixTotal, article, dette, createdAt, updatedAt);

        } catch (SQLException e) {
            System.err.println("Erreur lors de la conversion du résultat en objet Details : " + e.getMessage());
            throw new RuntimeException("Conversion échouée", e);
        }
    }
}
