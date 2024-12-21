package dette.boutique.controllers;

import dette.boutique.App;
import dette.boutique.data.entities.Article;
import dette.boutique.services.ArticleService;
import javafx.fxml.FXML;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class FormCreerArticleAdminController extends AdminController{
    ArticleService articleService = App.getArticleService();

    List<Article> listArticles = articleService.list();

    // Déclaration des éléments FXML
    @FXML
    private Spinner<Integer> spinner_quantite;
    
    @FXML
    private Label label_erreur;
    
    @FXML
    private TextField champ_libelle;
    
    @FXML
    private Spinner<Integer> spinner_prixUnitaire;
    
    @FXML
    private void initialize(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000, 0, 1);
        SpinnerValueFactory<Integer> valueFactoryUnitaire = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000, 0, 1);

        // Associer la factory au Spinner
        spinner_quantite.setValueFactory(valueFactory);
        spinner_prixUnitaire.setValueFactory(valueFactoryUnitaire);

    }
    
    // Méthode pour valider l'article
    @FXML
    private void validerArticle() {
        // Récupérer les valeurs des champs
        String libelle = champ_libelle.getText();
        boolean goodLibelle = validerLibelle(libelle);
    
        // Validation de la quantité
        Integer quantite = spinner_quantite.getValue();
        boolean goodQuantite = quantite != null && quantite > 0;
        if (!goodQuantite) {
            label_erreur.setText("La quantité doit être supérieure à 0.");
        }
    
        // Validation du prix unitaire
        Integer prixUnitaire = spinner_prixUnitaire.getValue();
        boolean goodPrixUnitaire = prixUnitaire != null && prixUnitaire > 0;
        if (!goodPrixUnitaire) {
            label_erreur.setText("Le prix unitaire doit être supérieur à 0.");
        }
    
        // Vérifier que toutes les validations sont correctes
        if (goodLibelle && goodQuantite && goodPrixUnitaire) {
            // Créer l'article si toutes les validations sont passées
            Article article = new Article(libelle, prixUnitaire, quantite);
            articleService.create(article); // Ajouter l'article dans la base de données ou autre logique
            label_erreur.setText("Article ajouté avec succès."); // Message de succès
            System.out.println("Article validé : " + libelle + ", Quantité : " + quantite + ", Prix unitaire : " + prixUnitaire);
        }
    }
    
    private boolean validerLibelle(String libelle) {
        boolean good = false;
        if (libelle.isEmpty()) {
            label_erreur.setText("Le libellé ne peut pas être vide.");
        } else if (existeDansListe(libelle)) {
            label_erreur.setText("Un article avec ce libellé existe déjà.");
        } else {
            good = true;
        }
        return good;
    }
    
    private boolean existeDansListe(String libelle) {
        return listArticles.stream()
                .anyMatch(a -> a.getLibelle().toLowerCase().equals(libelle.toLowerCase()));
    }

}
