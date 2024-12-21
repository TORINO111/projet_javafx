package dette.boutique.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.User;
import dette.boutique.services.ClientService;
import dette.boutique.services.DetteService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class ListeDettesSoldeesClientAdminController extends AdminController {

    private final ClientService clientService = App.getClientService();
    private final DetteService detteService = App.getDetteService();

    private User userConnected;
    private Client client;
    private List<Dette> listDettes;

    @FXML
    private TreeTableView<Dette> champ_treeTableView;

    @FXML
    private TreeTableColumn<Dette, String> champ_numero;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montantTotal;

    @FXML
    private TreeTableColumn<Dette, String> champ_client;

    @FXML
    private TreeTableColumn<Dette, String> champ_date;

    @FXML
    private TreeTableColumn<Dette, String> champ_dateSolde;

    @FXML
    private TreeTableColumn<Dette, String> champ_etat;

    @FXML
    private TreeTableColumn<Dette, String> champ_archiver;

    @FXML
    private Label label_archive;

    @FXML
    private void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            // Dettes soldées
            listDettes = detteService.listeDettesSoldees();

            // Initialiser les colonnes
            champ_numero.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue().getValue().getId())));
            champ_montantTotal.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMontant()));
            champ_client.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getClient().getPrenom() + " "
                            + param.getValue().getValue().getClient().getNom()));
            champ_date.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getCreatedAt().format(formatter)));
            champ_dateSolde.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getUpdatedAt().format(formatter)));
            champ_etat.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().isIsarchive() ? "Archivée" : "Non archivée"));

            populateTreeTable();

            champ_archiver.setCellFactory(col -> new TreeTableCell<Dette, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Button button = new Button("Archiver");
                        button.setOnMouseClicked(event -> {
                            @SuppressWarnings("deprecation")
                            Dette selectedDette = getTreeTableRow().getItem();
                            archiver(selectedDette);
                        });
                        setGraphic(button);
                    }
                }
            });

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

    private void archiver(Dette selectedDette) {
        detteService.archiverDetteSoldee(selectedDette);
        label_archive.setText("Dette archivée avec succès!");

        populateTreeTable();
        champ_treeTableView.refresh();
    }
}
