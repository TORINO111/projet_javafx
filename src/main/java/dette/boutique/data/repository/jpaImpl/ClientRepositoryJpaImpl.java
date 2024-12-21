package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ClientRepositoryJpaImpl extends RepositoryJpaImpl<Client> implements ClientRepository {

    public ClientRepositoryJpaImpl(EntityManager em) {
        super(Client.class, em);
    }

    @Override
    public Client findByTel(String tel) {
        Client client = null;
        try {

            TypedQuery<Client> query = this.em.createQuery("SELECT c FROM Client c WHERE c.telephone = :tel",
                    Client.class);
            query.setParameter("tel", tel);

            client = query.getSingleResult();

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du client par téléphone : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return client;
    }

}