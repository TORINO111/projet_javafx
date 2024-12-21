package dette.boutique.controllers;

import dette.boutique.App;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import dette.boutique.services.DetteService;
import dette.boutique.services.EtatService;
import dette.boutique.services.DetailsService;

import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeDemandesDetteBoutiquierController extends BoutiquierController {

    private final DetteService detteService = App.getDetteService();
    private final EtatService etatService = App.getEtatService();
    private final DetailsService detailsService = App.getDetailsService();

    @FXML
    private TreeTableView<Dette> champ_treeTableView;

    @FXML
    private TreeTableColumn<Dette, String> champ_numero;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montant;

    @FXML
    private TreeTableColumn<Dette, String> champ_dateDemande;

    @FXML
    private TreeTableColumn<Dette, String> champ_detailsDemandeDette;

    @FXML
    private TreeTableColumn<Dette, String> champ_valider;

    @FXML
    private TreeTableColumn<Dette, String> champ_refuser;

    @FXML
    private AnchorPane anchor_afficheDettes;

    @FXML
    private TextArea champ_afficheDettes;

    @FXML
    private Button button_fermerAfficheDettes;

    @FXML
    private Label label_succes;

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Dette> demandes = detteService.listeDettesEnAttenteDeValidation();

        champ_numero.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                String.valueOf(param.getValue().getValue().getId())));

        champ_montant.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getValue().getMontant()));

        champ_dateDemande.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getValue().getCreatedAt().format(formatter)));

        champ_detailsDemandeDette.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button button = new Button("Détails");
                    button.setOnMouseClicked(event -> {
                        @SuppressWarnings("deprecation")
                        Dette selectedDette = getTreeTableRow().getItem();
                        showDetails(selectedDette);
                    });
                    setGraphic(button);
                }
            }
        });

        champ_valider.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button button = new Button("Valider");
                    button.setOnMouseClicked(event -> {
                        @SuppressWarnings("deprecation")
                        Dette selectedDette = getTreeTableRow().getItem();
                        validerDette(selectedDette);
                    });
                    setGraphic(button);
                }
            }
        });

        champ_refuser.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button button = new Button("Refuser");
                    button.setOnMouseClicked(event -> {
                        @SuppressWarnings("deprecation")
                        Dette selectedDette = getTreeTableRow().getItem();
                        refuserDette(selectedDette);
                    });
                    setGraphic(button);
                }
            }
        });

        populateTreeTable(demandes);
    }

    private void populateTreeTable(List<Dette> demandes) {
        TreeItem<Dette> root = new TreeItem<>(null);
        demandes.forEach(dette -> root.getChildren().add(new TreeItem<>(dette)));
        champ_treeTableView.setRoot(root);
        champ_treeTableView.setShowRoot(false);
    }

    private void showDetails(Dette dette) {
        try {
        
        anchor_afficheDettes.setDisable(false);
        anchor_afficheDettes.setOpacity(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<Details> detailsDette = detailsService.findDetailsDette(dette);

        StringBuilder detailsText = new StringBuilder();
        for (Details detail : detailsDette) {
                // Ajouter chaque détail à la chaîne, en formatant comme souhaité
                detailsText.append("Article: ").append(detail.getArticle().getLibelle()).append("; ");
                detailsText.append("Prix Unitaire: ").append(detail.getArticle().getPrixUnitaire()).append("; ");
                detailsText.append("Quantité: ").append(detail.getQuantite()).append("; ");
                detailsText.append("Sous-Total: ").append(detail.getPrixTotal()).append("\n");
                
            }
            // Afficher le texte dans le TextArea
            champ_afficheDettes.setText(detailsText.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void validerDette(Dette dette) {
        dette.setEtat(etatService.findByNom("EN_COURS"));
        detteService.update(dette);
        label_succes.setText("Demande de dette validée avec succès");
        refreshTable();
        label_succes.setText("");
    }

    private void refuserDette(Dette dette) {
        dette.setEtat(etatService.findByNom("ANNULEE"));
        detteService.update(dette);
        label_succes.setText("Demande de dette annulée avec succès");
        refreshTable();
        label_succes.setText("");
    }

    @FXML
    private void fermerAnchor() {
        anchor_afficheDettes.setDisable(true);
        anchor_afficheDettes.setOpacity(0);
    }

    private void refreshTable() {
        List<Dette> updatedDemandes = detteService.listeDettesEnAttenteDeValidation();
        populateTreeTable(updatedDemandes);
    }
}
