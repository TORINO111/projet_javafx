package dette.boutique.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.core.Service;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Details;
import dette.boutique.data.repository.ArticleRepository;

public class ArticleService extends Service<Article> implements Item<Article> {
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void create(Article article) {
        articleRepository.insert(article);
    }

    @Override
    public List<Article> list() {
        return articleRepository.selectAll();
    }

    public void update(Article article) {
        articleRepository.insert(article);
    }

    public void afficherArticlesDispo() {
        List<Article> articlesDispo = listeArticlesDispo();
        if (!articlesDispo.isEmpty()) {
            System.out.println("------Articles en stock------");
            afficherListe(articlesDispo);
        } else {
            System.out.println("Aucun article en stock");
        }
    }

    public void afficherArticlesIndispo() {
        List<Article> articlesIndispo = listeArticlesIndispo();
        if (!articlesIndispo.isEmpty()) {
            System.out.println("------Articles en rupture de stock------");
            afficherListe(articlesIndispo);
        } else {
            System.out.println("Aucun article en rupture de stock");
        }
    }

    public void assignerArticlesAuDetail(List<Details> panierArticles) {
        for (Details detail : panierArticles) {
            Article article = detail.getArticle();
            article.setUpdatedAt(LocalDateTime.now());
            if (article.getDetails() == null) {
                article.setDetails(new ArrayList<>());
            }
            article.getDetails().add(detail);
            update(article);
        }
    }

    public List<Article> listeArticlesDispo() {
        return list().stream()
                .filter(article -> article.getQteStock() > 0)
                .toList();
    }

    public List<Article> listeArticlesIndispo() {
        return list().stream()
                .filter(article -> article.getQteStock() == 0)
                .toList();
    }

    // public static List<Article> listArticles(){
    //     return articleRepository.selectAll();
    // }
    public Article findById(int article_id) {
        return list().stream()
                .filter(article -> article_id == article.getId())
                .findFirst()
                .orElse(null);
    }

    public List<Article> isDispo(boolean choix) {
        if (choix) {
            return list().stream()
                    .filter(article -> article.getQteStock() != 0)
                    .toList();
        } else {
            return list().stream()
                    .filter(article -> article.getQteStock() == 0)
                    .toList();
        }
    }

    public Article findArticle(String libelle) {
        return list().stream()
                .filter(article -> article.getLibelle().compareTo(libelle) == 0)
                .findFirst()
                .orElse(null);
    }

    public boolean changeStock(int quantite, Article article) {
        article.setQteStock(quantite);
        return true;
    }

}
