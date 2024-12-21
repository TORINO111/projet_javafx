package dette.boutique.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.Article;
import dette.boutique.services.ArticleService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ListeArticlesAdminController extends AdminController {

    private final ArticleService articleService = App.getArticleService();
    private List<Article> listArticles;
    private Article selectedArticle;

    @FXML
    private ComboBox<String> combo_filtre;

    @FXML
    private TreeTableView<Article> champ_treeTableView;

    @FXML
    private TreeTableColumn<Article, Integer> champ_numero;

    @FXML
    private TreeTableColumn<Article, String> champ_libelleArticle;

    @FXML
    private TreeTableColumn<Article, Integer> champ_prixUnitaire;

    @FXML
    private TreeTableColumn<Article, Integer> champ_enStock;

    @FXML
    private TreeTableColumn<Article, String> champ_date;

    @FXML
    private TreeTableColumn<Article, String> champ_majStockArticle;

    @FXML
    private AnchorPane anchor_majStock;

    @FXML
    private Spinner<Integer> spinner_majStock;

    @FXML
    private TextField label_majStock;

    @FXML
    private Button button_validerMajStock;

    @FXML
    private Label label_erreur;

    @FXML
    public void initialize() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            ObservableList<String> options = FXCollections.observableArrayList(
                    "Tous",
                    "En Stock",
                    "En rupture de stock");
                    combo_filtre.setItems(options);

                    // (Optionnel) Sélectionner un élément par défaut
                    combo_filtre.getSelectionModel().select(0);
            // Charger la liste des articles
            listArticles = articleService.list();

            // Initialiser les colonnes
            champ_numero.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getId()));
            champ_libelleArticle.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getLibelle()));
            champ_prixUnitaire.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getPrixUnitaire()));
            champ_enStock.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getQteStock()));
            champ_date.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getCreatedAt().format(formatter)));
            champ_majStockArticle.setCellFactory(col -> new TreeTableCell<Article, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Button button = new Button("MAJ Stock");
                        button.setOnMouseClicked(event -> {
                            // Sélectionner l'article et afficher le formulaire de mise à jour
                            @SuppressWarnings("deprecation")
                            Article selectedArticle = getTreeTableRow().getItem();
                            showMajStock(selectedArticle);
                        });
                        setGraphic(button);
                    }
                }
            });
            populateTreeTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTreeTable() {
        TreeItem<Article> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);

        for (Article article : listArticles) {
            TreeItem<Article> articleItem = new TreeItem<>(article);
            rootItem.getChildren().add(articleItem);
        }

        champ_treeTableView.setRoot(rootItem);
        champ_treeTableView.setShowRoot(false);
    }

    private void showMajStock(Article selectedArticle) {
        this.selectedArticle = selectedArticle;
        anchor_majStock.setDisable(false);
        anchor_majStock.setOpacity(1);
        label_majStock.setText(selectedArticle.getLibelle());
    
        // Vérifier si le spinner a déjà un ValueFactory défini, sinon l'initialiser
        if (spinner_majStock.getValueFactory() == null) {
            spinner_majStock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        }
    
        // Si la quantité du stock est différente de 0, on met à jour la valeur du spinner
        if (selectedArticle.getQteStock() != 0) {
            spinner_majStock.getValueFactory().setValue(selectedArticle.getQteStock());
        } else {
            // Si la quantité est 0, on définit la valeur du spinner à 0
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
            spinner_majStock.setValueFactory(valueFactory);
        }
    
        // Ajout de l'action du bouton pour valider la mise à jour du stock
        button_validerMajStock.setOnAction(event -> validerMajStock());
    }
    

    @FXML
    private void validerMajStock() {
        if (selectedArticle != null ) {
            Integer nouvelleQuantite = spinner_majStock.getValue();

            if (nouvelleQuantite >0) {
                selectedArticle.setQteStock(nouvelleQuantite);
                articleService.update(selectedArticle);
                if(selectedArticle.getQteStock() ==0){
                    FiltreRuptureStock();
                } else{
                    populateTreeTable();
                    champ_treeTableView.refresh();
                }
                anchor_majStock.setDisable(true);
                anchor_majStock.setOpacity(0);
            } else{
                label_erreur.setText("**Veuillez saisir une quantité supérieur à 0**");
                return;
            }
        } else {
            System.out.println("mbe");
        }
    }

    private void FiltreEnStock() {
        // Mettre à jour la liste des articles disponibles
        listArticles = articleService.listeArticlesDispo();

        // Ajouter les articles disponibles dans la TreeTableView
        populateTreeTable();

        // Rafraîchir la TreeTableView pour afficher les nouvelles données
        champ_treeTableView.refresh();
    }

    private void FiltreRuptureStock() {
        listArticles = articleService.listeArticlesIndispo();
        populateTreeTable();
        champ_treeTableView.refresh();
    }

    private void filtreTout() {
        listArticles = articleService.list();
        populateTreeTable();
        champ_treeTableView.refresh();
    }

    @FXML
    private void filtreCase(){
        String choixFiltre = combo_filtre.getValue();

        switch (choixFiltre) {
            case "En Stock"-> {
                FiltreEnStock();
            }
            case "En rupture de stock" -> {
                FiltreRuptureStock();
            }
            default-> {
                filtreTout();
            }
        }
    }
}
