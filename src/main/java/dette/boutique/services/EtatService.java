package dette.boutique.services;

import java.util.List;

import dette.boutique.core.Service;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.EtatRepository;

public class EtatService extends Service<Etat> {
    private EtatRepository etatRepository;

    public EtatService(EtatRepository etatRepository) {
        this.etatRepository = etatRepository;
    }

    public List<Etat> list() {
        return etatRepository.selectAll();
    }

    public Etat findByNom(String nom) {
        return list().stream()
                .filter(etat -> etat.getNom().trim().compareTo(nom.trim()) == 0)
                .findFirst()
                .orElse(null);
    }
}