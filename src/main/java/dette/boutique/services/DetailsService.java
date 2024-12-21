package dette.boutique.services;

import java.time.LocalDateTime;
import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.core.Service;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.DetailsRepository;

public class DetailsService extends Service<Etat> implements Item<Details> {
    private DetailsRepository detailsRepository;

    public DetailsService(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    public void assignerDette(List<Details> details, Dette dette) throws Exception {
        for (Details detail : details) {
            detail.setUpdatedAt(LocalDateTime.now());
            detail.setDette(dette);
            update(detail);
            if (!detail.getDette().equals(dette)) {
                throw new Exception(
                        "Le détail avec ID " + detail.getId() + " n'est pas correctement associé à la dette.");
            }
        }
    }

    public List<Details> findDetailsDette(Dette dette) {
        return list().stream()
                .filter(detail -> detail.getDette().getId() == dette.getId())
                .toList();
    }

    public List<Details> findDetailsArticle(Article article) {
        return list().stream()
                .filter(details -> details.getArticle().getId() == article.getId())
                .toList();
    }

    @Override
    public void create(Details element) {
        detailsRepository.insert(element);
    }

    @Override
    public void update(Details element) {
        detailsRepository.insert(element);
    }

    public void updateList(List<Details> elements) {
        for (Details details : elements) {
            detailsRepository.insert(details);
        }
    }

    public void createList(List<Details> elements) {
        for (Details details : elements) {
            detailsRepository.insert(details);
        }
    }

    @Override
    public List<Details> list() {
        return detailsRepository.selectAll();
    }

}