package dette.boutique.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.services.ClientService;
import dette.boutique.services.DetteService;
import dette.boutique.services.EtatService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class ListeDettesBoutiquierController extends BoutiquierController {

    private final DetteService detteService = App.getDetteService();
    private final EtatService etatService = App.getEtatService();
    private final ClientService clientService = App.getClientService();

    private List<Dette> listDettes;
    private Dette selectedDette;

    int montantVersement;

    @FXML
    private TreeTableView<Dette> champ_treeTableView;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_numero;

    @FXML
    private TreeTableColumn<Dette, String> champ_nom;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montantTotal;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montantVerse;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montantRestant;

    @FXML
    private TreeTableColumn<Dette, String> champ_versement;

    @FXML
    private AnchorPane anchor_versement;

    @FXML
    private Spinner<Integer> spinner_versement;

    @FXML
    private Button button_validerVersement;

    @FXML
    private Label label_erreur;

    @FXML
    public void initialize() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            // Charger la liste des Dettes
            listDettes = detteService.listeDettesNonSoldees();

            // Initialiser les colonnes du TreeTableView
            champ_numero.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getId()));
            champ_nom.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getClient().getPrenom() + " "
                            + param.getValue().getValue().getClient().getNom()));
            champ_montantTotal.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMontant()));
            champ_montantVerse.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMontantVerse()));
            champ_montantRestant.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMontantRestant()));
            champ_versement.setCellFactory(col -> new TreeTableCell<Dette, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Button button = new Button("Enregistrer versement");
                        button.setOnMouseClicked(event -> {
                            // Afficher le formulaire de versement pour le Dette sélectionné
                            @SuppressWarnings("deprecation")
                            Dette selectedDette = getTreeTableRow().getItem();
                            showVersement(selectedDette);
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
        TreeItem<Dette> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);

        for (Dette dette : listDettes) {
            TreeItem<Dette> detteItem = new TreeItem<>(dette);
            rootItem.getChildren().add(detteItem);
        }

        champ_treeTableView.setRoot(rootItem);
        champ_treeTableView.setShowRoot(false);
    }

    private void showVersement(Dette selectedDette) {
        this.selectedDette = selectedDette;
        anchor_versement.setDisable(false);
        anchor_versement.setOpacity(1);

        // Vérifier si le spinner a déjà un ValueFactory défini, sinon l'initialiser
        if (spinner_versement.getValueFactory() == null) {
            spinner_versement
                    .setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        }

        // Ajout de l'action du bouton pour valider la mise à jour du stock
        button_validerVersement.setOnAction(event -> validerVersement());

    }

    @FXML
    private void validerVersement() {
        // Saisie montant versé
        this.montantVersement = spinner_versement.getValue();
        int montantPrecedent = this.selectedDette.getMontant();
        int oldMontantVerse = this.selectedDette.getMontantVerse();
        int montantBiDess = montantPrecedent - oldMontantVerse;

        if (this.montantVersement <=0 ){
            label_erreur.setText("Veuillez saisir un montant supérieur à 0");
        } else if (this.montantVersement > montantBiDess){
            label_erreur.setText("Il reste uniquement "+ montantBiDess + " Francs CFA à payer");
            return;
        } else{
            this.selectedDette.setMontantVerse(oldMontantVerse + this.montantVersement);
            this.selectedDette.setMontantRestant(montantBiDess - this.montantVersement);
                // MAJ de l'état si la dette est soldée
                if (this.selectedDette.getMontantRestant() == 0) {
                    this.selectedDette.setEtat(etatService.findByNom("SOLDEE"));
                }
            detteService.update(this.selectedDette);

            // Client clientDette = clientService.findClientDette(this.selectedDette);
            
            spinner_versement.setValueFactory(null);
            listDettes = detteService.listeDettesNonSoldees();
            populateTreeTable();
            champ_treeTableView.refresh();
            fermerAnchor();

        }
    }
    @FXML
    private void fermerAnchor(){
        anchor_versement.setDisable(true);
        anchor_versement.setOpacity(0);
    }

}