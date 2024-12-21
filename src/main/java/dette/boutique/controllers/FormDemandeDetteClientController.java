package dette.boutique.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.entities.User;
import dette.boutique.services.ArticleService;
import dette.boutique.services.ClientService;
import dette.boutique.services.DetailsService;
import dette.boutique.services.DetteService;
import dette.boutique.services.EtatService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

public class FormDemandeDetteClientController extends ClientController {
    ArticleService articleService = App.getArticleService();
    List<Article> listArticlesChoisis = new ArrayList<>();
    List<Article> listArticlesOG = new ArrayList<>();
    List<Details> listDetails = new ArrayList<>();
    List<Details> listDetailsArticle = new ArrayList<>();

    private final ClientService clientService = App.getClientService();
    private final DetteService detteService = App.getDetteService();
    private final EtatService etatService = App.getEtatService();
    private final DetailsService detailsService = App.getDetailsService();

    private User userConnected;
    private Client clientDette;

    private int positionDetail;
    private int positionDetailArticle;
    private int positionArticleChoisi;

    @FXML
    private ComboBox<Article> combo_article;

    @FXML
    private Spinner<Integer> spinner_quantite;

    @FXML
    private TreeTableView<Article> champ_treeTableView;

    @FXML
    private TreeTableColumn<Article, String> champ_article;

    @FXML
    private TreeTableColumn<Article, Integer> champ_quantite;

    @FXML
    private TreeTableColumn<Article, Integer> champ_prixUnitaire;

    @FXML
    private TreeTableColumn<Article, Integer> champ_total;

    @FXML
    private Label label_total;

    @FXML
    private Label label_erreur;

    @FXML
    private void initialize() {
        int positionDetail = 0;
        userConnected = ConnexionController.getUserConnected();
        if (userConnected == null) {
            throw new IllegalStateException("Aucun utilisateur connecté trouvé.");
        }

        // Trouver le client associé
        clientDette = clientService.findClientByUserId(userConnected.getId());
        if (clientDette == null) {
            throw new IllegalStateException("Aucun client associé à l'utilisateur connecté.");
        }
        List<Article> articles = articleService.listeArticlesDispo();
        listArticlesChoisis = new ArrayList<>();
        // Initialisation treeItem
        TreeItem<Article> root = new TreeItem<>(null);
        root.setExpanded(true);
        champ_treeTableView.setRoot(root);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000000, 0, 1);

        // Associer la factory au Spinner
        spinner_quantite.setValueFactory(valueFactory);


        // Convertir la liste en ObservableList et l'ajouter au ComboBox
        ObservableList<Article> observableArticles = FXCollections.observableArrayList(articles);
        combo_article.setItems(observableArticles);

        combo_article.setCellFactory(
                (Callback<ListView<Article>, ListCell<Article>>) new Callback<ListView<Article>, ListCell<Article>>() {
                    @Override
                    public ListCell<Article> call(ListView<Article> param) {
                        return new ListCell<>() {
                            @Override
                            protected void updateItem(Article item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    setText(item.getLibelle());
                                }
                            }
                        };
                    }
                });

        combo_article.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Article item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getLibelle());
                }
            }
        });

        if (listArticlesChoisis != null) {
            // Initialiser les colonnes
            champ_article.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue().getValue().getLibelle())));
            champ_quantite.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getQteStock()));
            champ_prixUnitaire.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getPrixUnitaire()));
            champ_total.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(
                            param.getValue().getValue().getDetails().get(positionDetailArticle).getPrixTotal()));
        }
        populateTreeTable();

        // Écoute des changements de sélection
        // combo_article.valueProperty().addListener((observable, oldValue, newValue) ->
        // {
        // if (newValue != null) {
        // System.out.println("Article sélectionné : " + newValue.getLibelle());
        // System.out.println("Quantité en stock : " + newValue.getQteStock());
        // }
        // });
    }

    public void enregistrerDette() throws Exception {
        try {
            Dette dette = new Dette();
            // Assignation des articles aux détails
            articleService.assignerArticlesAuDetail(listDetails);

            // Récupération de l'état initial
            Etat etatEnAttente = etatService.findByNom("EN_ATTENTE_DE_VALIDATION");
            if (etatEnAttente == null) {
                throw new IllegalStateException("État 'EN_ATTENTE_DE_VALIDATION' introuvable.");
            }
            dette.setClient(clientDette);
            // Création de la dette et sauvegarde immédiate
            dette = detteService.defarDette(clientDette, listDetails, dette);
            dette.setEtat(etatEnAttente);
            System.out.println(dette);
            detteService.create(dette);

            // Vérifiez que l'ID de la dette a été généré
            if (dette.getId() <= 0) {
                throw new IllegalStateException("L'ID de la dette n'a pas été généré.");
            }

            // Association des détails à la dette (l'ID est maintenant disponible)
            detailsService.assignerDette(listDetails, dette);

            // Mise à jour du client
            clientDette.getDettes().add(dette);
            clientDette.setUpdatedAt(LocalDateTime.now());
            clientService.update(clientDette);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'enregistrement de la dette.", e);
        }

    }

    private void populateTreeTable() {
        TreeItem<Article> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);

        for (Article article : listArticlesChoisis) {
            TreeItem<Article> articleItem = new TreeItem<>(article);
            rootItem.getChildren().add(articleItem);
        }

        champ_treeTableView.setRoot(rootItem);
        champ_treeTableView.setShowRoot(false);
    }

    public void validerArticle() {
        Article articleCombo = combo_article.getValue();
        Integer quantiteVoulue = spinner_quantite.getValue();

        // Validation des entrées
        if (articleCombo == null) {
            afficherErreur("Veuillez sélectionner un article.");
            return;
        }
        if (quantiteVoulue == null || quantiteVoulue <= 0) {
            afficherErreur("Veuillez entrer une quantité valide.");
            return;
        }
        if (quantiteVoulue > articleCombo.getQteStock()) {
            afficherErreur(
                    "Quantité insuffisante : il ne reste que " + articleCombo.getQteStock() + " pièces en stock.");
            return;
        }

        // Réinitialisation du message d'erreur
        label_erreur.setText("");

        // Vérification de la racine de la TreeTableView
        if (champ_treeTableView.getRoot() == null) {
            afficherErreur("Erreur : la structure de la table est incorrecte.");
            return;
        }

        // Création et ajout d'un nouvel article
        Article nouvelArticle = new Article(
                articleCombo.getId(),
                articleCombo.getLibelle(),
                articleCombo.getPrixUnitaire(),
                quantiteVoulue);

        boolean dejaPresent = listArticlesChoisis.stream()
                .anyMatch(article -> article.getId() == (nouvelArticle.getId()));

        if (!dejaPresent) {
            // Ajout du détail
            Details newDetail = new Details(articleCombo, quantiteVoulue);
            listDetailsArticle.add(newDetail);
            listDetails.add(newDetail);
            positionDetailArticle = listDetailsArticle.indexOf(newDetail);
            positionDetail = listDetails.indexOf(newDetail);
            articleCombo.getDetails().add(newDetail);
            nouvelArticle.getDetails().add(newDetail);
            listArticlesChoisis.add(nouvelArticle);
            listArticlesOG.add(articleCombo);

            TreeItem<Article> nouvelItem = new TreeItem<>(nouvelArticle);
            champ_treeTableView.getRoot().getChildren().add(nouvelItem);
        } else {
            System.out.println("mbe");
            for (Details detail : listDetails) {
                System.out.println(detail);
            }
            Details detail = new Details();
            Details detailArticle = new Details();
            Article article = new Article();

            for (int i = 0; i < listArticlesOG.size(); i++) {
                article = listArticlesOG.get(i);
                // Vérifie si ce détail est associé à l'article sélectionné
                if (article.getId() == nouvelArticle.getId()) {
                    break;
                }
            }

            positionDetail = -1;
            for (int i = 0; i < listDetails.size(); i++) {
                detail = listDetails.get(i);
                // Vérifie si ce détail est associé à l'article sélectionné
                if (detail.getArticle().getId() == articleCombo.getId()) {
                    positionDetail = i;
                    break;
                }
            }

            positionDetailArticle = -1;
            for (int i = 0; i < listDetails.size(); i++) {
                try {
                    if (!article.getDetails().isEmpty()) {
                        detailArticle = article.getDetails().get(i);
                        // Vérifie si ce détail est associé à l'article sélectionné
                        if (detailArticle.getArticle().getId() == articleCombo.getId()) {
                            positionDetailArticle = i;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (positionDetailArticle != -1 && positionDetail != -1) {
                try {
                    article.setQteStock(article.getQteStock() + quantiteVoulue);
                    articleCombo.setQteStock(articleCombo.getQteStock() - quantiteVoulue);
                    // Details de l'article
                    detailArticle.setQuantite(detailArticle.getQuantite() + quantiteVoulue);
                    nouvelArticle.setQteStock(nouvelArticle.getQteStock() + quantiteVoulue);
                    System.out.println(nouvelArticle);
                    // Liste des détails
                    detail.setQuantite(detail.getQuantite() + quantiteVoulue);
    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Mise à jour du total général
        int totalGeneral = calculerTotalGeneral();
        label_total.setText("Total : " + totalGeneral + " FCFA");

        // Réinitialisation des champs
        reinitialiserChamps();
    }

    // private int positionDetailArticle( Article articleCombo) {
    // positionDetailArticle = -1;
    // for (int i = 0; i < listDetailsArticle.size(); i++) {
    // Details detail = listDetailsArticle.get(i);
    // // Vérifie si ce détail est associé à l'article sélectionné
    // if (detail.getArticle().getId() == articleCombo.getId()) {
    // positionDetailArticle = i;
    // break;
    // }
    // }
    // return positionDetailArticle;
    // }

    // private int positionDetail( Article article) {
    // positionDetail = -1;
    // for (int i = 0; i < listDetails.size(); i++) {
    // Details detail = listDetails.get(i);
    // // Vérifie si ce détail est associé à l'article sélectionné
    // if (detail.getArticle().getId() == article.getId()) {
    // positionDetail = i;
    // break;
    // }
    // }
    // return positionDetail;
    // }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void reinitialiserChamps() {
        spinner_quantite.getValueFactory().setValue(0);
        combo_article.setValue(null);
    }

    // Méthode utilitaire pour calculer le total général à partir de la
    // TreeTableView
    private int calculerTotalGeneral() {
        int totalGeneral = 0;

        if (champ_treeTableView.getRoot() != null) {
            for (TreeItem<Article> item : champ_treeTableView.getRoot().getChildren()) {
                Article article = item.getValue();
                totalGeneral += article.getQteStock() * article.getPrixUnitaire();
            }
        }

        return totalGeneral;
    }

}
