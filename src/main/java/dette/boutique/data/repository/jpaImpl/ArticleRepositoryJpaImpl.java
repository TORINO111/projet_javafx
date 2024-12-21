package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.ArticleRepository;
import jakarta.persistence.EntityManager;

public class ArticleRepositoryJpaImpl extends RepositoryJpaImpl<Article> implements ArticleRepository {

    public ArticleRepositoryJpaImpl(EntityManager em) {
        super(Article.class, em);
    }

    @Override
    public Article findByLibelle(String libelle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByLibelle'");
    }

    @Override
    public boolean updateQteStock(Article article, int qteStock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
