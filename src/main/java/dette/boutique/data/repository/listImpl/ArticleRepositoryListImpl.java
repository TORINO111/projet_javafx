package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryListImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.ArticleRepository;

public class ArticleRepositoryListImpl extends RepositoryListImpl<Article> implements ArticleRepository {

    public ArticleRepositoryListImpl(List<Article> data) {
        super(data);
    }

    public ArticleRepositoryListImpl() {
        super(new ArrayList<>());
    }

    @Override
    public Article findByLibelle(String libelle) {
        for (Article article : data) {
            if (article.getLibelle().equals(libelle)) {
                return article;
            }
        }
        return null;
    }

    @Override
    public Article selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Article element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public boolean updateQteStock(Article article, int qteStock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
