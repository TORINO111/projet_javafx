package dette.boutique.controllers;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.services.ClientService;
import dette.boutique.services.DetteService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class ListeClientsBoutiquierController extends BoutiquierController {
    ClientService clientService = App.getClientService();
    DetteService detteService = App.getDetteService();

    List<Client> clients;

    @FXML
    private TreeTableView<Client> champ_treeTableView;

    @FXML
    private TreeTableColumn<Client, String> champ_numero;

    @FXML
    private TreeTableColumn<Client, String> champ_nom;

    @FXML
    private TreeTableColumn<Client, String> champ_prenom;

    @FXML
    private TreeTableColumn<Client, String> champ_telephone;

    @FXML
    private TreeTableColumn<Client, String> champ_adresse;

    @FXML
    private TreeTableColumn<Client, String> champ_Login;

    @FXML
    private TreeTableColumn<Client, String> champ_dettesNonSoldees;

    @FXML
    private Label label_erreurRecherche;

    @FXML
    private TextField champ_rechercherClient;

    @FXML
    private AnchorPane anchor_afficheDettes;

    @FXML
    private TextArea champ_afficheDettes;

    @FXML
    private Button button_fermerAfficheDettes;

    @FXML
    public void initialize() {
        clients = clientService.list();
        for (Client client : clients) {
            System.out.println(client);
        }
        TreeItem<Client> root = new TreeItem<>(null);
        root.setExpanded(true);
        champ_treeTableView.setRoot(root);
        
        if (clients != null) {
            champ_numero.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue().getValue().getId())));
            champ_nom.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue().getValue().getNom())));
            champ_prenom.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getPrenom()));
            champ_telephone.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getTelephone()));
            champ_adresse.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getAdresse()));
            champ_Login.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getUser()!=null ? param.getValue().getValue().getUser().getLogin() : "Aucun utilisateur affilié"));
            champ_dettesNonSoldees.setCellFactory(col -> new TreeTableCell<Client, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Button button = new Button("Voir dettes");
                        button.setOnMouseClicked(event -> {
                            @SuppressWarnings("deprecation")
                            Client selectedClient = getTreeTableRow().getItem();
                            showDetails(selectedClient);
                        });
                        setGraphic(button);
                    }
                }
            });
            }

            populateTreeTable();
    }

    private void populateTreeTable() {
        TreeItem<Client> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);

        for (Client client : clients) {
            TreeItem<Client> articleItem = new TreeItem<>(client);
            rootItem.getChildren().add(articleItem);
        }

        champ_treeTableView.setRoot(rootItem);
        champ_treeTableView.setShowRoot(false);
    }

    private void showDetails(Client client) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            anchor_afficheDettes.setDisable(false);
            anchor_afficheDettes.setOpacity(1);

            List<Dette> dettesClient = detteService.listeDettesNonSoldeesClient(client);

            StringBuilder detailsText = new StringBuilder();
            for (Dette dette : dettesClient) {
                // Ajouter chaque détail à la chaîne, en formatant comme souhaité
                detailsText.append("Montant Total: ").append(dette.getMontant()).append("; ");
                detailsText.append("Montant Versé: ").append(dette.getMontantVerse()).append("; ");
                detailsText.append("Montant Restant: ").append(dette.getMontantRestant()).append("; ");
                detailsText.append("Date de création: ").append(dette.getCreatedAt().format(formatter)).append("; \n");
            }

            // Afficher le texte dans le TextArea
            champ_afficheDettes.setText(detailsText.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthodes liées aux événements (fonctions associées)
    // @FXML
    // private void FormCreerClient() throws IOException {
    //     App.setRoot("FormCreerClientBoutiquier");
    // }

    @FXML
    private void findClientByTelephone() {
        if (!champ_rechercherClient.getText().isEmpty()) {
            String numero = champ_rechercherClient.getText();
            Client client = clientService.findClientByTelephone(numero);
            if (client != null){
                clients = new ArrayList<>();
                clients.add(client);
                populateTreeTable();
                champ_treeTableView.refresh();
            } else{
                label_erreurRecherche.setText("Client non trouvé");
            }
    
        }
    }

    @FXML
    private void voirClientsAll(){
        clients = clientService.list();
        populateTreeTable();
        champ_treeTableView.refresh();
    }

    @FXML
    public void fermerAnchor() {
        anchor_afficheDettes.setOpacity(0);
        anchor_afficheDettes.setDisable(true);
    }

    // Ajoutez d'autres méthodes ou initialisations si nécessaire

}
