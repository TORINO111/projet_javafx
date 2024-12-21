package dette.boutique.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.User;
import dette.boutique.services.ClientService;
import dette.boutique.services.DetailsService;
import dette.boutique.services.DetteService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class ListeDettesClientController extends ClientController {

    private final ClientService clientService = App.getClientService();
    private final DetteService detteService = App.getDetteService();
    private final DetailsService detailsService = App.getDetailsService();

    private User userConnected;
    private Client client;
    private List<Dette> listDettesClient;

    @FXML
    private TreeTableView<Dette> champ_treeTableView;

    @FXML
    private TreeTableColumn<Dette, String> champ_numero;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montantTotal;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montantVerse;

    @FXML
    private TreeTableColumn<Dette, Integer> champ_montantRestant;

    @FXML
    private TreeTableColumn<Dette, String> champ_etat;

    @FXML
    private TreeTableColumn<Dette, String> champ_date;

    @FXML
    private TreeTableColumn<Dette, String> champ_details;

    @FXML
    private TextArea champ_afficheDetails;

    @FXML
    private Button button_fermerChampDetails;

    @FXML
    private AnchorPane anchor_details;

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            // Récupérer l'utilisateur connecté
            userConnected = ConnexionController.getUserConnected();
            if (userConnected == null) {
                throw new IllegalStateException("Aucun utilisateur connecté trouvé.");
            }

            // Trouver le client associé
            client = clientService.findClientByUserId(userConnected.getId());
            if (client == null) {
                throw new IllegalStateException("Aucun client associé à l'utilisateur connecté.");
            }

            // Dettes du client
            listDettesClient = detteService.listDette(client);

            // Initialiser les colonnes
            champ_numero.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue().getValue().getId())));
            champ_montantTotal.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMontant()));
            champ_montantVerse.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMontantVerse()));
            champ_montantRestant.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMontantRestant()));
            champ_etat.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getEtat().getNom()));
            champ_date.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getCreatedAt().format(formatter)));

            populateTreeTable();

            champ_details.setCellFactory(col -> new TreeTableCell<Dette, String>() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTreeTable() {
        TreeItem<Dette> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);

        for (Dette dette : listDettesClient) {
            TreeItem<Dette> detteItem = new TreeItem<>(dette);
            rootItem.getChildren().add(detteItem);
        }

        champ_treeTableView.setRoot(rootItem);
        champ_treeTableView.setShowRoot(false);
    }

    private void showDetails(Dette dette) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            anchor_details.setDisable(false);
            champ_afficheDetails.setDisable(false);
            button_fermerChampDetails.setDisable(false);

            anchor_details.setOpacity(1);
            champ_afficheDetails.setOpacity(1);
            button_fermerChampDetails.setOpacity(1);

            List<Details> detailsDette = detailsService.findDetailsDette(dette);

            StringBuilder detailsText = new StringBuilder();
            for (Details detail : detailsDette) {
                // Ajouter chaque détail à la chaîne, en formatant comme souhaité
                detailsText.append("Article: ").append(detail.getArticle().getLibelle()).append("; ");
                detailsText.append("Prix Unitaire: ").append(detail.getArticle().getPrixUnitaire()).append("; ");
                detailsText.append("Quantité: ").append(detail.getQuantite()).append("; ");
                detailsText.append("Sous-Total:: ").append(detail.getPrixTotal()).append("\n");
            }

            // Afficher le texte dans le TextArea
            champ_afficheDetails.setText(detailsText.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fermerAnchor() {
        anchor_details.setOpacity(0);
        anchor_details.setDisable(true);
        champ_afficheDetails.setDisable(true);
        champ_afficheDetails.setOpacity(0);
        button_fermerChampDetails.setDisable(true);
        button_fermerChampDetails.setOpacity(0);
    }
}
