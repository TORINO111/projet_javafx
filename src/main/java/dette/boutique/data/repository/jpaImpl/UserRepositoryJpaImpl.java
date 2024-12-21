package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;
import jakarta.persistence.EntityManager;

public class UserRepositoryJpaImpl extends RepositoryJpaImpl<User> implements UserRepository {

    public UserRepositoryJpaImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public void updateClientForUser(User user) {
        try {
            em.getTransaction().begin();

            User existingUser = em.find(User.class, user.getId());

            if (existingUser != null) {
                existingUser.setClient(user.getClient());

                em.merge(existingUser);
            } else {
                System.out.println("user non trouvé avec l'ID : " + user.getId());
            }
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de la mise à jour du client de l'utilisateur  : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User selectByLogin(String login) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectByLogin'");
    }

    @Override
    public User selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

}
