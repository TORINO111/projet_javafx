package dette.boutique.core;

import java.util.List;

public abstract class Service<T> {

    public void afficherListe(List<T> liste) {
        for (int i = 0; i < liste.size(); i++) {
            System.out.println((i + 1) + ". " + liste.get(i));
        }
    }


}
