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
import dette.boutique.data.repository.ArticleRepository;

public class ArticleRepositoryJdbcPostgreImpl extends RepositoryDbPostgreImpl<Article> implements ArticleRepository {
    private static final String INSERT_QUERY = "INSERT INTO articles (libelle, prixunitaire, qtestock) VALUES (?, ?, ?)";

    private static final String SELECT_QUERY = "SELECT articles.id AS article_id, articles.libelle AS article_libelle, articles.qtestock AS article_qte_stock, articles.prixunitaire As article_prix_unitaire, articles.created_at AS article_created_at, articles.updated_at AS article_updated_at, "+
            "details.id AS details_id, details.prixtotal AS details_prix_total, details.quantite AS details_quantite, details.article_id AS details_article_id, details.dette_id AS details_dette_id, details.created_at AS details_created_at, details.updated_at AS details_updated_at, "+
            "    etats.id AS etat_id, " +
            "    etats.created_at AS etat_created_at, " +
            "    etats.updated_at AS etat_updated_at, " +
            "    etats.nom AS etat_nom, " +
            "    dettes.id AS dette_id, " +
            "    dettes.created_at AS dette_created_at, " +
            "    dettes.updated_at AS dette_updated_at, " +
            "    dettes.isarchive AS dette_is_archive, " +
            "    dettes.montant AS dette_montant, " +
            "    dettes.montant_verse AS dette_montant_verse, " +
            "    dettes.client_id AS dette_client_id, " +
            "    dettes.etat_id AS dette_etat_id " +
            "FROM articles " +
            "LEFT JOIN details ON articles.id = details.article_id "+
            "LEFT JOIN dettes ON details.dette_id = dettes.id " +
            "LEFT JOIN etats ON dettes.etat_id = etats.id" ;

    private static final String SELECT_ARTICLE_QUERY = "SELECT * FROM articles WHERE .id = ?";

    private static final String UPDATE_ARTICLE = "UPDATE articles SET qtestock = ? , updated_at = ? WHERE id = ?";

    // private static final String SELECT_DETAILS_QUERY = "SELECT details.quantite,
    // details.prixTotal, "
    // + "details.articles.d, details.dette_id, "
    // + "articles.libelle AS articles.ibelle, articles.qteStock AS
    // articles.qteStock, articles.prixUnitaire AS articles.prixUnitaire "
    // + "FROM details "
    // + "LEFT JOIN articles ON details.articles.d = articles.id "
    // + "LEFT JOIN dettes ON details.dette_id = dettes.id";

    @Override
    public void insert(Article article) {
        try {
            this.connexion();
            String checkQuery = "SELECT id FROM articles WHERE articles.libelle = ?";
            this.init(checkQuery);
            this.ps.setString(1, article.getLibelle());
            ResultSet rs = this.ps.executeQuery();

            if (rs.next()) {
                this.init(UPDATE_ARTICLE);
                setFieldsUpdate(this.ps, article);
                this.executeUpdate();
            } else {
                this.init(INSERT_QUERY);
                setFields(this.ps, article);
                this.executeUpdate();
                rs = this.ps.getGeneratedKeys();
                if (rs.next()) {
                    article.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'article : " + e.getMessage());
        } finally {
            this.deconnexion();
        }
    }

    @Override
    public List<Article> selectAll() {
        List<Article> listArticles = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(SELECT_QUERY);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listArticles.add(convertToObject(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } finally {
            deconnexion();
        }

        return listArticles;
    }

    @Override
    public Article selectById(int id) {
        Article article = null;
        try {
            connexion();
            this.init(SELECT_ARTICLE_QUERY);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                article = convertToObject(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'article : " + e.getMessage());
        } finally {
            deconnexion();
        }
        return article;
    }

    @Override
    public void setFields(PreparedStatement pstmt, Article article) {
        try {
            pstmt.setString(1, article.getLibelle());
            pstmt.setInt(2, article.getPrixUnitaire());
            pstmt.setInt(3, article.getQteStock());
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while setting fields for Article: " + e.getMessage());
            throw new RuntimeException("Failed to set fields for Article", e);
        }
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Article article) {
        try {
            pstmt.setInt(1, article.getQteStock());
            pstmt.setTimestamp(2, Timestamp.valueOf(article.getUpdatedAt()));
            pstmt.setInt(3, article.getId());
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while setting fields for Article: " + e.getMessage());
            throw new RuntimeException("Failed to set fields for Article", e);
        }
    }

    @Override
    public Article convertToObject(ResultSet resultSet) {
        try {
            Article article = null;
            int articleId = resultSet.getInt("article_id");
            if (!resultSet.wasNull()) {
                String libelle = resultSet.getString("article_libelle");
                int qteStock = resultSet.getInt("article_qte_stock");
                int prixUnitaire = resultSet.getInt("article_prix_unitaire");
                LocalDateTime created_at = resultSet.getTimestamp("article_created_at").toLocalDateTime();
                LocalDateTime updated_at = resultSet.getTimestamp("article_updated_at").toLocalDateTime();

                article = new Article(articleId, libelle, prixUnitaire, qteStock,created_at, updated_at);
            }
            
            Dette dette = null;
            int detteId = resultSet.getInt("dette_id");
                if (detteId > 0) {
                    String nom = resultSet.getString("etat_nom");
                    LocalDateTime ecreatedAt = resultSet.getTimestamp("etat_created_at").toLocalDateTime();
                    LocalDateTime eupdatedAt = resultSet.getTimestamp("etat_updated_at").toLocalDateTime();
                    int id = resultSet.getInt("etat_id");
                    Etat etat = new Etat(id, nom, ecreatedAt, eupdatedAt);

                    LocalDateTime detteCreatedAt = resultSet.getTimestamp("dette_created_at").toLocalDateTime();
                    LocalDateTime detteUpdatedAt = resultSet.getTimestamp("dette_updated_at").toLocalDateTime();
                    boolean isArchive = resultSet.getBoolean("dette_is_archive");
                    int montant = resultSet.getInt("dette_montant");
                    int montantVerse = resultSet.getInt("dette_montant_verse");

                    dette = new Dette(detteId, montant, montantVerse, isArchive, etat, detteCreatedAt,
                            detteUpdatedAt);
                }

            Details detail = null;
            int detailId = resultSet.getInt("details_id");
            if (!resultSet.wasNull()){
                int quantite =  resultSet.getInt("details_quantite");
                int prixTotal =  resultSet.getInt("details_prix_total");
                LocalDateTime Dcreated_at = resultSet.getTimestamp("details_created_at").toLocalDateTime();
                LocalDateTime Dupdated_at = resultSet.getTimestamp("details_updated_at").toLocalDateTime();
                
                detail = new Details(detailId, quantite, prixTotal, article, dette, Dcreated_at, Dupdated_at);
            }
            article.getDetails().add(detail);
            return article;

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while converting ResultSet to Article: " + e.getMessage());
            throw new RuntimeException("Failed to convert ResultSet to Article", e);
        }
    }

    @Override
    public Article findByLibelle(String libelle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateQteStock(Article article, int qteStock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
