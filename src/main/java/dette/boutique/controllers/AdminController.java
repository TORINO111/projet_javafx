package dette.boutique.controllers;

import java.io.IOException;

import dette.boutique.App;

public abstract class AdminController {
    
    public void FormDemandeDette() throws IOException {
        App.setRoot("ListeUsersAdmin");
    }
    
    public void ListeUsers() throws IOException {
        App.setRoot("ListeUsersAdmin");
    }

    public void ListeArticles() throws IOException {
        App.setRoot("ListeArticlesAdmin");
    }

    public void ListeDettes() throws IOException {
        App.setRoot("ListeDettesArchiverAdmin");
    }

    public void FormCreerBoutiquierOuAdmin() throws IOException {
        App.setRoot("FormCreerUserBoutiquierOuAdmin");
    }
    
    public void FormCreerUserForClientWithoutCompte() throws IOException {
        App.setRoot("FormCreerUserPourClientSansCompteAdmin");
    }

    public void FormCreerArticle() throws IOException {
        App.setRoot("FormCreerArticleAdmin");
    }

    public void Deconnexion() throws IOException {
        App.setRoot("LoginForm");
    }

}
