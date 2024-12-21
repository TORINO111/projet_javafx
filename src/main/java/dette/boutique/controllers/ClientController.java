package dette.boutique.controllers;

import java.io.IOException;

import dette.boutique.App;

public abstract class ClientController {
    public void ListeDemandesDettes() throws IOException {
        App.setRoot("ListeDemandesDettesClient");
    }

    public void ListeDettes() throws IOException {
        App.setRoot("ListeDettesClient");
    }

    public void FormDemandeDette() throws IOException {
        App.setRoot("FormDemandeDetteClient");
    }

    public void FormRelancerDette() throws IOException {
        
    }

    public void Deconnexion() throws IOException {
        App.setRoot("LoginForm");
    }

}
