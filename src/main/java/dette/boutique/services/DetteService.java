package dette.boutique.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.core.Service;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.DetteRepository;

public class DetteService extends Service<Dette> implements Item<Dette> {
    ArticleService articleService;
    ClientService clientService;

    private DetteRepository detteRepository;

    public DetteService(DetteRepository detteRepository, ArticleService articleService, ClientService clientService) {
        this.detteRepository = detteRepository;
        this.articleService = articleService;
        this.clientService = clientService;
    }

    @Override
    public void create(Dette element) {
        detteRepository.insert(element);
    }

    @Override
    public void update(Dette dette) {
        detteRepository.insert(dette);
    }

    public List<Dette> list() {
        return detteRepository.selectAll();
    }

    public Dette creerEtAffecterDette(Client clientDette, List<Details> panierArticles, EtatService etatService,
            DetailsService detailsService) throws Exception {
        // Création Dette
        int montant = defarMontant(panierArticles);
        String etat = "EN_COURS";
        Etat etatEnCours = etatService.findByNom(etat);
        System.out.println("L'etat: " + etatEnCours);
        Dette dette = new Dette(montant, clientDette, etatEnCours);
        create(dette);

        // Assigner la dette aux détails
        detailsService.assignerDette(panierArticles, dette);

        // Mettre à jour la dette avec les détails
        if (dette.getDetails() == null) {
            dette.setDetails(new ArrayList<>());
        }
        dette.getDetails().addAll(panierArticles);
        update(dette);
        System.out.println("Dette créée et assignée avec succès au client: " + clientDette.getPrenom() + " "
                + clientDette.getNom());
        return dette;
    }

    public List<Dette> listDette(Client client) {
        return list().stream()
                .filter(dette -> dette.getClient() != null && dette.getClient().getId() == client.getId())
                .toList();
    }

    public List<Dette> listeDettesSoldees() {
        return list().stream()
                .filter(dette -> "SOLDEE".equals(dette.getEtat().getNom()))
                .toList();
    }

    public List<Dette> listeDettesNonSoldees() {
        return list().stream()
                .filter(dette -> dette.getMontantRestant() > 0 && !dette.isIsarchive())
                .toList();
    }

    public void afficherDettesEnCours() {
        List<Dette> listDettesEnCours = listeDettesEnAttenteDeValidation();
        if (!listDettesEnCours.isEmpty()) {
            System.out.println("----Liste des demandes de dette en cours----");
            afficherListe(listDettesEnCours);
        } else {
            System.out.println("Aucune demande de dette en cours");
        }
    }

    public void afficherDettesAnnulees() {
        List<Dette> listDettesAnnulees = listeDettesAnnulees();
        if (!listDettesAnnulees.isEmpty()) {
            System.out.println("----Liste des demandes de dette annulées----");
            afficherListe(listDettesAnnulees);
        } else {
            System.out.println("Aucune dette annulée");
        }
    }

    public List<Dette> listeDettesEnAttenteDeValidation() {
        return list().stream()
                .filter(dette -> dette.getEtat().getNom().compareTo("EN_ATTENTE_DE_VALIDATION") == 0)
                .toList();
    }

    public List<Dette> listeDettesEnAttenteDeValidation(Client client) {
        return list().stream()
                .filter(dette -> dette.getClient() != null && dette.getClient().getId() == client.getId()
                        && dette.getEtat() != null && "EN_ATTENTE_DE_VALIDATION".equals(dette.getEtat().getNom()))
                .toList();
    }

    public List<Dette> listeDettesNonSoldees(Client client) {
        return list().stream()
                .filter(dette -> dette.getClient() != null && dette.getClient().getId() == client.getId()
                        && dette.getEtat() != null && "EN_COURS".equals(dette.getEtat().getNom()))
                .toList();
    }

    public Dette findById(int dette_id) {
        return list().stream()
                .filter(dette -> dette_id == dette.getId())
                .findFirst()
                .orElse(null);
    }

    public void updateDette(Dette detteChoisie, int oldMontantVerse, int montantVerse, int montantRestant,
            EtatService etatService) {
        detteChoisie.setMontantVerse(oldMontantVerse + montantVerse);
        detteChoisie.setMontantRestant(montantRestant);
        if (montantRestant == 0) {
            detteChoisie.setEtat(etatService.findByNom("SOLDEE"));
        }
        update(detteChoisie);
    }

    public boolean updateDetteClient(Client client, Dette detteChoisie, EtatService etatService, ClientService clientService) {
        for (Dette dette : client.getDettes()) {
            if (dette.getId() == detteChoisie.getId()) {
                // MAJ des montants
                dette.setMontantVerse(detteChoisie.getMontantVerse());
                dette.setMontantRestant(detteChoisie.getMontantRestant());
                // MAJ de l'état si la dette est soldée
                if (dette.getMontantRestant() == 0) {
                    dette.setEtat(etatService.findByNom("SOLDEE"));
                }
                // MAJ updatedAt
                dette.setUpdatedAt(LocalDateTime.now());
                // MAJ dette
                update(dette);
                // MAJ client
                client.setUpdatedAt(LocalDateTime.now());
                clientService.update(client);
                return true;
            }
        }
        System.out.println("Amoul");
        return false;
    }

    public List<Dette> listeDettesAnnulees() {
        return list().stream()
                .filter(dette -> dette.getEtat().getNom().compareTo("ANNULEE") == 0)
                .toList();
    }

    public List<Dette> listeDettesNonSoldeesClient(Client client) {
        return list().stream()
                .filter(dette -> dette.getClient().getTelephone().equals(client.getTelephone()) && "EN_COURS".equals(dette.getEtat().getNom()))
                .toList();
    }

    public void archiverDettesSoldees(List<Dette> dettes) {
        for (Dette dette : dettes) {
            dette.setIsarchive(true);
            dette.setUpdatedAt(LocalDateTime.now());
            detteRepository.insert(dette);
        }
    }

    public void archiverDetteSoldee(Dette detteBi) {
            detteBi.setIsarchive(true);
            detteBi.setUpdatedAt(LocalDateTime.now());
            update(detteBi);
    }

    public int defarMontant(List<Details> details) {
        int montantTotal = details.stream()
                .mapToInt(detail -> detail.getPrixTotal())
                .sum();
        return montantTotal;
    }

    public Dette defarDette(Client client, List<Details> details, Dette dette) {
        int montantTotal = details.stream()
                .mapToInt(detail -> detail.getPrixTotal())
                .sum();
        dette.setClient(client);
        dette.setDetails(details);
        dette.setMontant(montantTotal);
        return dette;
    }

    public void ajouterDetteAuClient(Client clientBi, Dette dette) {
        List<Dette> dettes = clientBi.getDettes();
        if (dettes == null) {
            dettes = new ArrayList<>();
        }
        dettes.add(dette);

        clientBi.setDettes(dettes);
    }

}