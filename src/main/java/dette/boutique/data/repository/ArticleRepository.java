package dette.boutique.data.repository;

import dette.boutique.core.repository.Repository;
import dette.boutique.data.entities.Article;

public interface ArticleRepository extends Repository<Article> {
    public Article findByLibelle(String libelle);

    public boolean updateQteStock(Article article, int qteStock);
}
